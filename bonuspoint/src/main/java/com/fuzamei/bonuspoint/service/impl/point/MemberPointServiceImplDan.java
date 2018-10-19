package com.fuzamei.bonuspoint.service.impl.point;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.CompanyBC;
import com.fuzamei.bonuspoint.blockchain.bc.MemberBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.dao.user.ContactDao;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRelationPO;
import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.enums.AssetResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.bonuspoint.service.impl.user.UserServiceMethodDan;
import com.fuzamei.bonuspoint.service.point.MemberPointServiceDan;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MemberPointServiceImplDan implements MemberPointServiceDan{

    private final MemberPointDao memberPointDao;
    private final MemberPointServiceMethodDan memberPointServiceMethodDan;
    private final ContactDao contactDao;

    private final PlatformInfoMapper platformInfoMapper;
    private final AccountMapper accountMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final GeneralPointRelationMapper generalPointRelationMapper;
    private final MemberBC memberBC;
    private final BlockChainUtil blockChainUtil;
    private final PointRecordMapper pointRecordMapper;
    private final CompanyBC companyBC;
    private final CashRecordMapper cashRecordMapper;
    private final UserServiceMethodDan userServiceMethodDan;

    @Value("${platform.publicKey}")
    private String platformPublicKey;
    @Autowired
    public MemberPointServiceImplDan(MemberPointDao memberPointDao, MemberPointServiceMethodDan memberPointServiceMethodDan,
                                     ContactDao contactDao, PlatformInfoMapper platformInfoMapper, AccountMapper accountMapper,
                                     CompanyInfoMapper companyInfoMapper, GeneralPointRelationMapper generalPointRelationMapper,
                                     MemberBC memberBC, BlockChainUtil blockChainUtil, PointRecordMapper pointRecordMapper, CompanyBC companyBC,
                                     CashRecordMapper cashRecordMapper, UserServiceMethodDan userServiceMethodDan){
        this.memberPointDao = memberPointDao;
        this.memberPointServiceMethodDan = memberPointServiceMethodDan;
        this.contactDao = contactDao;
        this.platformInfoMapper = platformInfoMapper;
        this.accountMapper = accountMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.memberBC = memberBC;
        this.blockChainUtil = blockChainUtil;
        this.pointRecordMapper = pointRecordMapper;
        this.companyBC = companyBC;
        this.cashRecordMapper = cashRecordMapper;
        this.userServiceMethodDan = userServiceMethodDan;
    }

    /**
     * 兑换通用积分
     * lkm
     *
     * @param exchangeDTO 数据传输类
     * @return 响应类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO exchange(ExchangePointDTO exchangeDTO) {

        //积分数量不能为0
        if(exchangeDTO.getExNum().equals(BigDecimal.ZERO)){
            return new ResponseVO(PointResponseEnum.POINT_NUM_ZERO);
        }


        Long platformUid = exchangeDTO.getPlatformUid();

        Example example = new Example(PlatformInfoPO.class);
        example.createCriteria().andEqualTo("uid",platformUid);
        //通过平台uid查出平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(example);

        if(platformInfoPO == null){
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }

        Long platformId = platformInfoPO.getId();

        exchangeDTO.setPlatformId(platformId);

        Long groupId = exchangeDTO.getGroupId();

        //查找集团信息
        CompanyInfoPO companyInfoPO = companyInfoMapper.selectByPrimaryKey(groupId);

        /**
         * 检查集团是否存在
         * */
        if (companyInfoPO == null) {
            return new ResponseVO(CommonResponseEnum.COMPANY_NOT_EXIST);
        }

        //检查交易密码是否正确
       AccountPO accountPO = accountMapper.selectByPrimaryKey(exchangeDTO.getUid());

        if (!exchangeDTO.getPayWordHash().equals(accountPO.getPaywordHash())) {
            return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
        }

        //找出积分有效期不为永久的积分
        List<PointPO> pointList = memberPointDao.findCompanyPointByUserIdAndCompanyId(exchangeDTO.getUid(), groupId);
        //找出积分有效期为永久的积分，并将其放在最后使用
        List<PointPO> pointList1 = memberPointDao.findCompanyPointByUserIdAndCompanyIdTwo(exchangeDTO.getUid(), groupId);
        pointList.addAll(pointList1);


        //与发送者的积分对象相对应的接收者积分对象
        List<PointPO> pointOpList = new ArrayList<>(pointList.size());
        for (int i = 0; i < pointList.size(); i++) {
            PointPO po = new PointPO();
            pointOpList.add(po);
        }
        if (pointList.size() == 0) {
            return new ResponseVO(PointResponseEnum.POINT_NOT_EXIST);
        } else {
            //兑换的集团积分数
            BigDecimal exNum = exchangeDTO.getExNum();
            //兑换比率
            BigDecimal pointRate = new BigDecimal(pointList.get(0).getPointRate());

            //能兑换出来的平台积分总数
            BigDecimal exchangeGeneralPoint = exNum.divide(pointRate, 4, BigDecimal.ROUND_HALF_UP);

            //记录需要用到几批集团积分
            int iNum = 0;

            //将平台积分的接收对象与发送对象赋值
            Map map = memberPointServiceMethodDan.createPointObjExchange(pointList, pointOpList, exNum, iNum, platformId, platformUid);
            iNum = (int) map.get("iNum");
            exNum = (BigDecimal) map.get("exNum");
            pointList = (List<PointPO>) map.get("pointList");
            pointOpList = (List<PointPO>) map.get("pointOpList");

            // exNum > 0 表示只有一批集团积分且不够发，故报错（集团积分不足以兑换平台积分）
            if (exNum.compareTo(BigDecimal.ZERO) == 1) {
                return new ResponseVO(PointResponseEnum.COMPANY_POINT_NOT_ENOUGH);
            }


            /**
             * 开始更新数据库
             */
            //li表示用到的集团积分集合
            List<PointPO> li = new ArrayList<>();
            for (int i = 0; i < iNum; i++) {
                li.add(pointList.get(i));
            }
            /**
             *  更新兑换者的集团积分数
             */
            try {
                memberPointServiceMethodDan.updateSendOjbCpmpany(li, iNum);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new ResponseVO(PointResponseEnum.POINT_UPDATE_ERROR);
            }




            //集团积分中被兑换的数量
            BigDecimal numberUsed;

            //集团积分兑换记录的对象集合，用于上链时修改数据库的hash与height
            List<PointRecordPO> pointRecordPOList = new ArrayList<>();


            //将兑换者减去的集团积分变动记录插表
            for (int i = 0; i < iNum; i++) {
                PointRecordPO pointRecordPO = new PointRecordPO();
                pointRecordPO.setUpdatedAt(TimeUtil.timestamp());
                pointRecordPO.setCategory(PointRecordConstant.CATEGORY_USER_EXCHANGE);
                pointRecordPO.setCreatedAt(pointRecordPO.getUpdatedAt());
                pointRecordPO.setType(PointRecordConstant.POINT_SUB);

                PointPO pointPO = pointList.get(i);
                if(String .valueOf(pointPO.getNumSend()).equals("0.0000")){
                    continue;
                }
                pointRecordPO.setNum(pointPO.getNumSend());
                pointRecordPO.setOppositeUid(companyInfoPO.getUid());
                pointRecordPO.setPointId(pointPO.getPointId());
                pointRecordPO.setUid(pointPO.getUserId());
                pointRecordPO.setPointRate(pointPO.getPointRate());
                pointRecordPO.setPlatformPointRate(platformInfoPO.getPointRate());

                //兑换者减去的积分变动记录插入
               int n = pointRecordMapper.insertSelective(pointRecordPO);
               if(n < 1){
                   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                   throw new RuntimeException("发送者减去的积分变动记录插入失败");
               }

                pointRecordPOList.add(pointRecordPO);


                //该批次集团积分被兑换的总数
                numberUsed = pointPO.getNumUsed().add(pointPO.getNumSend());


                //修改已兑换的积分数
               Integer d =  memberPointDao.updateUserdPointByPointId(pointPO.getPointId(), numberUsed);
               if(d < 1){
                   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                   throw new RuntimeException("修改集团积分被兑换数失败");
               }
            }

            /**
             * 更改兑换者的平台积分，将兑换者的操作记录插表，减少集团备付金
             */
            Map<String,Object> mapGeneralPoint = null;
            try {
                //返回值用于上链
                mapGeneralPoint = memberPointServiceMethodDan.changeGeneralExchangeAccept(platformId, exchangeDTO, companyInfoPO, pointOpList, exchangeGeneralPoint);
            }catch (RuntimeException e){
                if(e.getMessage().equals("备付金不足")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                   return new ResponseVO(AssetResponseEnum.COMPANY_PROVISIONS_LACK);
                }else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException();
                }
            }

            /**
             * 兑换通用积分上链
             */
            GeneralPointRecordPO generalPointRecordPO = (GeneralPointRecordPO) mapGeneralPoint.get("generalPointRecordPO");
            CashRecordPO cashRecordPO = (CashRecordPO) mapGeneralPoint.get("cashRecordPO");
            if(generalPointRecordPO == null || cashRecordPO == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException();
            }

            //通过id查出用户
            AccountPO accountPOKey = accountMapper.selectByPrimaryKey(exchangeDTO.getUid());

            try {
                memberBC.pointsMerchantToGeneral(pointRecordPOList,generalPointRecordPO,String.valueOf(exchangeDTO.getUid()),String.valueOf(companyInfoPO.getId()), exchangeGeneralPoint,
                        li, accountPOKey.getPublicKey(), accountPOKey.getPrivateKey());

                 }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                   return new ResponseVO(PointResponseEnum.GENERALPOINT_BC_FAIL);
                }

                //减少备付金上链
            BigDecimal cash = exchangeGeneralPoint.multiply(new BigDecimal(platformInfoPO.getPointRate())).setScale(4,java.math.BigDecimal.ROUND_HALF_UP);
            ResponseBean<String> responseBean =  companyBC.accessCash(String.valueOf(companyInfoPO.getId()),String.valueOf(cash),false,accountPOKey.getPublicKey(),accountPOKey.getPrivateKey());

            //查询结果
            ResponseBean<TransactionResultBean> queryTransaction =  blockChainUtil.queryTransaction(responseBean.getResult());

            if(queryTransaction == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("兑换通用积分后减少备付金上链失败");
            }
            if(StringUtil.isNotBlank(queryTransaction.getError())){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("兑换通用积分后减少备付金上链失败");
            }
            log.info("兑换通用积分后减少备付金上链成功");

            String hash = responseBean.getResult();
            Long height = queryTransaction.getResult().getHeight();
            cashRecordPO.setHash(hash);
            cashRecordPO.setHeight(height);
            cashRecordPO.setUpdatedAt(String.valueOf(TimeUtil.timestamp()));
           int k =  cashRecordMapper.updateByPrimaryKeySelective(cashRecordPO);
            if(k < 0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("修改备付金减少记录失败");
            }
            /**
             * 将上链信息插表
             */
            try {
                userServiceMethodDan.addBlockInfo(hash,height, Brokerpoints.ActionType.AccessCash.getNumber(),exchangeDTO.getUid());
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new RuntimeException("插入操作哈希记录表失败");
            }



        }
        return new ResponseVO(PointResponseEnum.POINT_CONVERT_SUCCESS);
    }

    /**
     * 积分转账
     * lkm
     *
     * @param exchangeDTO 数据传输类
     * @return 响应类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO memberTranPoint(ExchangePointDTO exchangeDTO) {
            //积分数量不能为0
        if(exchangeDTO.getExNum().equals(BigDecimal.ZERO)){
            return new ResponseVO(PointResponseEnum.POINT_NUM_ZERO);
        }
        Long uid = contactDao.findOpIdByPublickey(exchangeDTO.getOpPubKey());

         //检查对方是否存在
        if (uid == null) {
            return new ResponseVO(PointResponseEnum.OTHER_NOT_EXIST);
        }
        //不能转给自己
        if (uid.equals(exchangeDTO.getUid())) {
            return new ResponseVO(PointResponseEnum.POINT_TRANSFER_FAIL);
        }
        //接收者的信息
        AccountPO accountPOAccept = accountMapper.selectByPrimaryKey(uid);

        //校验对方身份，只有对方为用户才可转积分
        if(!accountPOAccept.getRole().equals(Roles.MEMBER)){
            return new ResponseVO(PointResponseEnum.POINT_ROLE_TRANSFER_ERROR);
        }
        //检查交易密码是否正确
        AccountPO accountPO = accountMapper.selectByPrimaryKey(exchangeDTO.getUid());
        String paywordHash = accountPO.getPaywordHash();


        if(exchangeDTO.getPayWordHash() != null){
            //支付密码错误就报错
            if (!exchangeDTO.getPayWordHash().equals(paywordHash)) {
                return new ResponseVO(CommonResponseEnum.PAYWORD_WRONG);
            }
        }

        //接收者uid
        exchangeDTO.setOppositeUid(uid);

        /**
         *  当要转积分为集团积分的情况
         */
        if (exchangeDTO.getPointType() == PointInfoConstant.COMPANY_POINT) {
            Long groupId = exchangeDTO.getGroupId();

            //查找集团信息
            CompanyInfoPO companyInfoPO = companyInfoMapper.selectByPrimaryKey(groupId);

            //检查集团是否存在
            if (companyInfoPO == null) {
                return new ResponseVO(PointResponseEnum.COMPANY_NOT_EXIST);
            }

            //查询用户所拥有积分信息（包含bp_point_relation中数量为零的量）
            List<PointPO> listPointPo = memberPointDao.findCompanyPointByUserIdAndCompanyId(exchangeDTO.getUid(), exchangeDTO.getGroupId());
            //找出积分有效期为永久的积分，并将其放在最后使用（包含bp_point_relation中数量为零的量）
            List<PointPO> pointListt = memberPointDao.findCompanyPointByUserIdAndCompanyIdTwo(exchangeDTO.getUid(), groupId);
            listPointPo.addAll(pointListt);


            //与发送者的积分对象相对应的接收者积分对象
            List<PointPO> pointOpList = new ArrayList<>(listPointPo.size());

            for (int i = 0; i < listPointPo.size(); i++) {
                PointPO po = new PointPO();
                pointOpList.add(po);
            }
            if (listPointPo.size() == 0) {
                return new ResponseVO(PointResponseEnum.POINT_NOT_EXIST);
            } else {

                BigDecimal exNum = exchangeDTO.getExNum();

                //需要用到几批集团积分
                int iNum = 0;


                 //将集团积分发送者对象与接收者对象赋值
                Map numMap = memberPointServiceMethodDan.createPointObjCompany(listPointPo, pointOpList, exNum, iNum, uid);
                exNum = (BigDecimal) numMap.get("exNum");
                iNum = (int) numMap.get("iNum");
                listPointPo = (List<PointPO>) numMap.get("listPointPo");
                pointOpList = (List<PointPO>) numMap.get("pointOpList");


                if (exNum .compareTo(BigDecimal.ZERO)==1 ) {
                    return new ResponseVO(PointResponseEnum.COMPANY_POINT_NOT_ENOUGH);
                }

                //li为需要转账的集团积分集合
                List<PointPO> li = new ArrayList<>();
                for (int i = 0; i < iNum; i++) {
                    li.add(listPointPo.get(i));
                }

                //更新集团积分发送者的积分数
                try {
                    memberPointServiceMethodDan.updateSendOjbCpmpany(li, iNum);
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new ResponseVO(PointResponseEnum.POINT_UPDATE_ERROR);
                }


                /**
                 * 将发送者减去的积分信息插入bp_point_record表
                 */
                List<PointRecordPO> pointRecordPOListSend = new ArrayList<>();
                for (int i = 0; i < iNum; i++) {
                    PointPO poo = listPointPo.get(i);

                    //去掉为0的积分，防止上链出错
                    if(String .valueOf(poo.getNumSend()).equals("0.0000")){
                        continue;
                    }

                    PointRecordPO pointRecordPO = new PointRecordPO();
                    pointRecordPO.setType(PointRecordConstant.POINT_SUB);
                    pointRecordPO.setCategory(PointRecordConstant.CATEGORY_USER_TRANSFER_OUT);
                    pointRecordPO.setMemo(exchangeDTO.getMemo());
                    pointRecordPO.setPointRate(poo.getPointRate());
                    pointRecordPO.setPointId(poo.getPointId());
                    pointRecordPO.setCreatedAt(TimeUtil.timestamp());
                    pointRecordPO.setUpdatedAt(pointRecordPO.getCreatedAt());


                    pointRecordPO.setNum(poo.getNumSend());
                    pointRecordPO.setUid(poo.getUserId());
                    pointRecordPO.setOppositeUid(poo.getOpUserId());
                    //插入商户积分操作表失败
                   int n = pointRecordMapper.insertSelective(pointRecordPO);

                   if(n < 0){
                       log.info("插入商户积分操作表失败");
                       TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                       throw new RuntimeException("插入商户积分操作表失败");
                   }
                    pointRecordPOListSend.add(pointRecordPO);

                }

                List<PointRecordPO> pointRecordPOListAccept = null;
                //改变接收者的集团积分信息，将集团积分操作记录插bp_point_record表
                try {
                     pointRecordPOListAccept =  memberPointServiceMethodDan.changeCompanyAccept(iNum,pointOpList,exchangeDTO);
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException();
                }


                //上链
                try {
                    memberBC.pointsTransferPoints(null,pointRecordPOListSend,pointRecordPOListAccept,String.valueOf(exchangeDTO.getUid()),String.valueOf(uid), Roles.COMPANY,li,String.valueOf(companyInfoPO.getId()),null,null,null,
                            accountPO.getPublicKey(),accountPO.getPrivateKey());
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException(e.getMessage());
                }

            }

            //转账通用积分
        } else if(exchangeDTO.getPointType() == PointInfoConstant.GENERAL_POINT){

            Long platformUid = exchangeDTO.getPlatformUid();
            Example example = new Example(PlatformInfoPO.class);
            example.createCriteria().andEqualTo("uid",platformUid);

            PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(example);

            //检查平台是否存在

            if (platformInfoPO == null) {
                    return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
            }

            //平台id
            Long platformId = platformInfoPO.getId();
            //查询用户指定平台的通用积分
            Example examplee = new Example(GeneralPointRelationPO.class);
            Example.Criteria criteria = examplee.createCriteria();
            criteria.andEqualTo("userId",exchangeDTO.getUid());
            criteria.andEqualTo("platformId",platformId);

            //查询接收者的通用积分信息
            GeneralPointRelationPO generalPointRelationPOOther = generalPointRelationMapper.selectOneByExample(examplee);

            //接收者的通用积分信息
            PointPO pointPOAccept = new PointPO();

            BeanUtils.copyProperties(generalPointRelationPOOther,pointPOAccept);

            if (generalPointRelationPOOther == null) {
                return new ResponseVO(PointResponseEnum.GENERAL_POINT_NOT_EXIST);
            } else {

                pointPOAccept.setOpUserId(uid);
                if (generalPointRelationPOOther.getNum().compareTo( exchangeDTO.getExNum())==-1) {
                    return new ResponseVO(GoodResponseEnum.GOOD_PAY_GENERAL_POINT_OUTOF);
                }

                //转出用户
                Long from = exchangeDTO.getUid();

                Map<String ,GeneralPointRecordPO> map = null;
                /**
                 * 改变发送者与接收者的通用积分数，并将两者的操作记录插表
                 */
                try {
                    map = memberPointServiceMethodDan.changeGeneralSendAndAccept(pointPOAccept,exchangeDTO,platformId);
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException();
                }



                //上链
                try {
                    memberBC.pointsTransferPoints(map,null,null,String.valueOf(from),String.valueOf(uid), Roles.PLATFORM,null,null,
                            String.valueOf(platformId),String.valueOf(exchangeDTO.getExNum()),null,
                            accountPO.getPublicKey(),accountPO.getPrivateKey());
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException(e.getMessage());
                }


            }
        }
        return new ResponseVO(PointResponseEnum.POINT_EXCHANGE_SUCCESS);
    }

}
