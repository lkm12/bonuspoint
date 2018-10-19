package com.fuzamei.bonuspoint.service.impl.point;

import com.fuzamei.bonuspoint.constant.CashRecordConstant;
import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.po.asset.CashRecordPO;
import com.fuzamei.bonuspoint.entity.po.point.*;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Slf4j
public class MemberPointServiceMethodDan {

    private final MemberPointDao memberPointDao;
    private final UserDao userDao;
    private final PlatformInfoMapper platformInfoMapper;
    private final PointRelationMapper pointRelationMapper;
    private final GeneralPointRecordMapper generalPointRecordMapper;
    private final GeneralPointRelationMapper generalPointRelationMapper;
    private final PointRecordMapper pointRecordMapper;
    private final CashRecordMapper cashRecordMapper;
    @Autowired
    public MemberPointServiceMethodDan(MemberPointDao memberPointDao, UserDao userDao, PlatformInfoMapper platformInfoMapper,
                                       PointRelationMapper pointRelationMapper, GeneralPointRecordMapper generalPointRecordMapper,
                                       GeneralPointRelationMapper generalPointRelationMapper, PointRecordMapper pointRecordMapper, CashRecordMapper cashRecordMapper){
        this.memberPointDao = memberPointDao;
        this.userDao = userDao;
        this.platformInfoMapper = platformInfoMapper;
        this.pointRelationMapper = pointRelationMapper;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.pointRecordMapper = pointRecordMapper;
        this.cashRecordMapper = cashRecordMapper;
    }
    /**
     * 将平台积分的接收对象与发送对象赋值（两者同为兑换者，但需区别以方便插表）
     * @param pointList
     * @param pointOpList
     * @param
     * @param iNum
     * @param platformId
     * @param platformUid
     * @return
     */
    public Map createPointObjExchange(List<PointPO> pointList, List<PointPO> pointOpList, BigDecimal exNum, int iNum, Long platformId, Long platformUid) {
        for (Integer i = 0; i < pointList.size(); i++) {
            //接收者积分对象
            PointPO pointOp = pointOpList.get(i);
            //发送者积分对象
            PointPO point = pointList.get(i);

            //接受者的发送对象
            pointOp.setOpUserId(point.getUserId());
            //接收者的平台id
            pointOp.setPlatform(platformId);
            //接受者对象，方便之后插入bp_point_record表
            pointOp.setUserId(point.getUserId());
            //接收者 set积分交换比率
            pointOp.setPointRate(point.getPointRate());

            //发送者的发送对象，也就是平台
            point.setOpUserId(platformUid);
            BigDecimal num1 = point.getNum();
            BigDecimal k = num1.subtract(exNum);
            //此id的积分不够发的情况
            if (k .compareTo(BigDecimal.ZERO)==-1) {

                exNum = exNum.subtract( num1);
                //发送者的此id积分全部发出，即变为0
                point.setNum(BigDecimal.ZERO);
                //发送者发送的积分数量
                point.setNumSend(num1);
                pointOp.setPointId(point.getPointId());
                //接收者接到此id的积分数量为num1
                pointOp.setNum(num1);
                pointOp.setNumSend(num1);

            } else {
                //此id的积分够发的情况
                num1 =num1.subtract( exNum);
                //发送者的此id积分变为num1
                point.setNum(num1);
                //发送者发送的积分数量
                point.setNumSend(exNum);
                pointOp.setPointId(point.getPointId());
                //接收者接到此id的积分数量为exNum
                pointOp.setNum(exNum);
                pointOp.setNumSend(exNum);
                iNum = i + 1;
                //积分发送完毕，即为0
                exNum = BigDecimal.ZERO;
                break;
            }

        }
        Map map = new HashMap();
        map.put("exNum", exNum);
        map.put("iNum", iNum);
        map.put("pointList", pointList);
        map.put("pointOpList", pointOpList);
        return map;
    }

    /**
     * 更新发送者或兑换者的集团积分数
     * @param li
     * @param iNum
     * @param
     */
    public void updateSendOjbCpmpany(List<PointPO> li, int iNum) {

        //至少使用两个pointId的集团积分且最后的那批集团积分没有用完
        if (iNum > 1 && li.get(iNum - 1).getNum() .compareTo(BigDecimal.ZERO)!=0) {

            List<PointPO> liPO = li.subList(0, iNum - 1);

               //将之前用完的积分置为0
              Integer i =  memberPointDao.updatePointList(liPO);
              if(i < 0 ){
                  log.info("积分更新失败");
                  throw new RuntimeException("积分更新失败");
              }
                //修改发送者中数量不为0的固定集团的积分
             i =  memberPointDao.updatePointByPointIdAndUserId(li.get(iNum - 1));
              if(i < 0){
                  log.info("积分更新失败");
                  throw new RuntimeException("积分更新失败");
              }

        }
        //只使用一批集团积分且那批积分已用完
        if (iNum == 1 && li.get(0).getNum() .compareTo(BigDecimal.ZERO)== 0) {

            List<PointPO> liPO = li.subList(0, 1);

          int i =  memberPointDao.updatePointByPointIdAndUserId(liPO.get(iNum - 1));
          if(i < 0 ){
              log.info("积分更新失败");
              throw new RuntimeException("积分更新失败");
          }
        }
            //只使用一批集团积分且那批积分没有用完
            if (iNum == 1 && li.get(0).getNum().compareTo(BigDecimal.ZERO) != 0) {

                    //修改发送者中数量不为0的固定集团的积分
                  int i = memberPointDao.updatePointByPointIdAndUserId(li.get(iNum - 1));
                  if(i < 0){
                      log.info("积分更新失败");
                      throw new RuntimeException("积分更新失败");
                  }

            }

        //至少使用两个pointId的集团积分且最后的那批集团积分用完
        if (iNum > 1 && li.get(iNum - 1).getNum().compareTo(BigDecimal.ZERO) == 0) {
            List<PointPO> liPO = li.subList(0, iNum);


                //将之前用完的积分置为0
               int i = memberPointDao.updatePointList(liPO);
               if(i < 0){
                   log.info("积分更新失败");
                   throw new RuntimeException("积分更新失败");
               }
        }
    }

    /**
     * 更改接收者的平台积分，将接收者的操作记录插表，减少集团备付金
     * @param platformId
     * @param exchangeDTO
     * @param companyInfoDTO
     * @param pointOpList
     * @param exchangeGeneralPoint
     */
    public Map<String ,Object> changeGeneralExchangeAccept(Long platformId, ExchangePointDTO exchangeDTO, CompanyInfoPO companyInfoDTO,
                                            List<PointPO> pointOpList, BigDecimal exchangeGeneralPoint) {



        //查找平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectByPrimaryKey(platformId);

            //接收者积分对象
            PointPO po = pointOpList.get(0);

            //查询接收者是否拥有此平台的积分
            Example example = new Example(GeneralPointRelationPO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",String.valueOf(po.getUserId()));
            criteria.andEqualTo("platformId",po.getPlatform());

            //查询接收者的通用积分信息
            GeneralPointRelationPO generalPointRelationPO = generalPointRelationMapper.selectOneByExample(example);

            GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
            generalPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_EXCHANGE);
            generalPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
            generalPointRecordPO.setCreatedAt(TimeUtil.timestamp());
            generalPointRecordPO.setUpdatedAt(TimeUtil.timestamp());
            if (generalPointRelationPO == null) {

                GeneralPointRelationPO generalPointRelationPONew = new GeneralPointRelationPO();
                generalPointRelationPONew.setNum(exchangeGeneralPoint);
                generalPointRelationPONew.setUserId(po.getOpUserId());
                generalPointRelationPONew.setPlatformId(po.getPlatform());

                //接收者没有该平台积分则insert新数据
               int i =  generalPointRelationMapper.insertSelective(generalPointRelationPONew);
                if(i < 0){
                    log.info("接收者插入通用积分失败");
                    throw new RuntimeException("接收者插入通用积分失败");
                }



                generalPointRecordPO.setOppositeUid(platformInfoPO.getUid());
                generalPointRecordPO.setUid(po.getUserId());
                generalPointRecordPO.setNum(exchangeGeneralPoint);
                generalPointRecordPO.setPointId(1L);

                int n = generalPointRecordMapper.insertSelective(generalPointRecordPO);
                if(n < 0){
                    log.info("通用积分记录插入失败");
                    throw new RuntimeException("通用积分记录插入失败");
                }

            } else {
                //通用积分新值
                BigDecimal exchangeGeneral = generalPointRelationPO.getNum().add( exchangeGeneralPoint);
                exchangeDTO.setNum(exchangeGeneral);
                // //接收者有该平台积分则update新数据
               Integer n =  memberPointDao.updateGeneralPointByPlatformIdAndUserIdDan(exchangeDTO);
                if(n < 1){
                    log.info("更新通用积分失败");
                    throw new RuntimeException("更新通用积分失败");
                }
                generalPointRecordPO.setOppositeUid(platformInfoPO.getUid());
                //兑换者
                generalPointRecordPO.setUid(po.getOpUserId());
                generalPointRecordPO.setPointId(1L);
                generalPointRecordPO.setNum(exchangeGeneralPoint);

                //将接收者的记录插入general_record表
               int i = generalPointRecordMapper.insertSelective(generalPointRecordPO);
               if(i < 0){
                   log.info("通用积分记录插入失败");
                   throw new RuntimeException("通用积分记录插入失败");
               }

            }
        //备付余额
        BigDecimal amount = companyInfoDTO.getAmount().subtract(exchangeGeneralPoint.multiply(new BigDecimal(platformInfoPO.getPointRate())).setScale(4,java.math.BigDecimal.ROUND_HALF_UP));
            if(amount.compareTo(new BigDecimal(0)) == -1){

                 throw new RuntimeException("备付金不足");
            }
        exchangeDTO.setAmount(amount);
        //修改集团的备付金
       int i =  userDao.updateCompanyAmountByCompanyId(exchangeDTO);
       if(i < 0){
           log.info("修改备付金失败");
           throw new RuntimeException("修改备付金失败");
       }
        /**
         * 封装插入现金操作表的对象
         */
        CashRecordPO cashRecordPO = new CashRecordPO();
        cashRecordPO.setType(CashRecordConstant.COST_ASSETS);
        //集团支付备付金
        cashRecordPO.setCategory(CashRecordConstant.PAY_CASH);
        cashRecordPO.setStatus(CashRecordConstant.SUCCESS);
        //扣去集团的备付金
        cashRecordPO.setAmount(exchangeGeneralPoint.multiply(new BigDecimal(platformInfoPO.getPointRate())).
                setScale(4,java.math.BigDecimal.ROUND_HALF_UP));
        //现金的流出方（集团uid）
        cashRecordPO.setUid(companyInfoDTO.getUid());
        //现金的流入方（平台uid）
        cashRecordPO.setOppositeUid(platformInfoPO.getUid());
        cashRecordPO.setCreatedAt(String.valueOf(TimeUtil.timestamp()));
        cashRecordPO.setUpdatedAt(String.valueOf(TimeUtil.timestamp()));

        //将扣除集团备付金的记录插入bp_cash_record表
        int n = cashRecordMapper.insertUseGeneratedKeys(cashRecordPO);
        if(n < 0){
            log.info("扣除备付金操作记录插入失败");
            throw new RuntimeException("扣除备付金操作记录插入失败");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("cashRecordPO",cashRecordPO);
        map.put("generalPointRecordPO",generalPointRecordPO);
        return map;
    }

    /**
     * 将集团积分发送者对象与接收者对象赋值
     * @param listPointPo
     * @param pointOpList
     * @param exNum
     * @param iNum
     * @param uid
     * @return
     */
    public Map createPointObjCompany(List<PointPO> listPointPo, List<PointPO> pointOpList, BigDecimal exNum, int iNum, Long uid) {
        for (Integer i = 0; i < listPointPo.size(); i++) {
            //接收者积分对象
            PointPO pointOp = pointOpList.get(i);
            //发送者积分对象
            PointPO point = listPointPo.get(i);
            //接受者的uid
            pointOp.setOpUserId(uid);
            //发送者的uid，方便之后插入bp_point_record表
            pointOp.setUserId(point.getUserId());

            pointOp.setPointRate(point.getPointRate());

            //接收者的id，方便之后插入bp_point_record表
            point.setOpUserId(uid);
            BigDecimal num1 = point.getNum();
            BigDecimal k = num1.subtract(exNum);
            //此id的积分不够发的情况
            if (k .compareTo(BigDecimal.ZERO)== -1) {

                exNum =exNum.subtract( num1);
                //发送者的此id积分全部发出，即变为0
                point.setNum(BigDecimal.ZERO);
                //发送者发送的积分数量
                point.setNumSend(num1);
                pointOp.setPointId(point.getPointId());
                //接收者接到此id的积分数量为num1
                pointOp.setNum(num1);
                pointOp.setNumSend(num1);

            } else {
                //此id的积分够发的情况
                num1 = num1.subtract(exNum);
                //发送者的此id积分变为num1
                point.setNum(num1);
                ////发送者发送的积分数量
                point.setNumSend(exNum);
                pointOp.setPointId(point.getPointId());
                //接收者接到此id的积分数量为exNum
                pointOp.setNum(exNum);
                pointOp.setNumSend(exNum);
                iNum = i + 1;
                //积分发送完毕，即为0
                exNum = BigDecimal.ZERO;
                break;
            }
        }
        Map map = new HashMap();
        map.put("iNum", iNum);
        map.put("exNum", exNum);
        map.put("listPointPo", listPointPo);
        map.put("pointOpList", pointOpList);
        return map;
    }

    /**
     * 改变接收者的集团积分信息，将集团积分操作记录插bp_point_record表
     * @param iNum
     * @param pointOpList
     */
    public List<PointRecordPO>  changeCompanyAccept(int iNum, List<PointPO> pointOpList,ExchangePointDTO exchangePointDTO1) {

        for (int i = 0; i < iNum; i++) {
            PointPO po = pointOpList.get(i);
            Example example = new Example(PointRelationPO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",po.getOpUserId());
            criteria.andEqualTo("pointId",po.getPointId());

            //查找接收者对应的集团积分信息
            PointRelationPO pointRelationPO =  pointRelationMapper.selectOneByExample(example);
            //查看接收者对应的积分是否存在，存在则update,不存在则insert
            if (pointRelationPO == null) {

                PointRelationPO pointRelationPONew = new PointRelationPO();
                pointRelationPONew.setPointId(po.getPointId());
                pointRelationPONew.setUserId(po.getOpUserId());
                pointRelationPONew.setNum(po.getNum());

                //插入接收者集团积分
               int n = pointRelationMapper.insertSelective(pointRelationPONew);
               if(n < 0){
                   log.info("插入接收者集团积分失败");

                   throw new RuntimeException("插入接收者集团积分失败");
               }

            } else {
                BigDecimal opNum = pointRelationPO.getNum();
                BigDecimal opNumAll = opNum.add(po.getNum());
                po.setNum(opNumAll);
               int n =  memberPointDao.updatePointOpByPointIdAndUserId(po);
               if(n < 0){
                   log.info("插入接收者集团积分失败");

                   throw new RuntimeException("插入接收者集团积分失败");
               }
            }
        }


        List<PointRecordPO> pointRecordPOList = new ArrayList<>();
        //接收者接收积分
        for (int i = 0; i < iNum; i++) {

            PointPO pointOp = pointOpList.get(i);

            PointRecordPO pointRecordPO = new PointRecordPO();
            pointRecordPO.setType(PointRecordConstant.POINT_ADD);
            pointRecordPO.setCategory(PointRecordConstant.CATEGORY_USER_TRANSFER_IN);
            pointRecordPO.setCreatedAt(TimeUtil.timestamp());
            pointRecordPO.setUpdatedAt(pointRecordPO.getCreatedAt());
            pointRecordPO.setUid(pointOp.getOpUserId());
            pointRecordPO.setOppositeUid(pointOp.getUserId());
            if(String .valueOf(pointOp.getNumSend()).equals("0.0000")){
                continue;
            }
            pointRecordPO.setNum(pointOp.getNumSend());
            pointRecordPO.setPointId(pointOp.getPointId());
            pointRecordPO.setPointRate(pointOp.getPointRate());

            //插入商户积分操作记录
           int n =  pointRecordMapper.insertSelective(pointRecordPO);
           if(n < 0){
               log.info("插入商户积分操作记录失败");

               throw new RuntimeException("插入商户积分操作记录失败");
           }
            pointRecordPOList.add(pointRecordPO);
            //将接收者增加的积分信息插入bp_point_recode表
           // memberPointDao.insetPointRecodeMember(pointOp, ex);
        }
        return pointRecordPOList;
}

    /**
     * 改变发送者与接收者的通用积分数，并将两者的操作记录插表
     * @param pointPO
     * @param
     * @param exchangeDTO
     * @param platformId
     */
    public Map<String, GeneralPointRecordPO> changeGeneralSendAndAccept(PointPO pointPO, ExchangePointDTO exchangeDTO,Long platformId) {
        BigDecimal exchangeGeneral = pointPO.getNum().subtract( exchangeDTO.getExNum());
        pointPO.setNumSend(exchangeDTO.getExNum());

        Map<String,GeneralPointRecordPO> map = new HashMap<>();
        exchangeDTO.setPlatformId(platformId);

        //修改剩余积分
        exchangeDTO.setNum(exchangeGeneral);
       int n =  memberPointDao.updateGeneralPointByPlatformIdAndUserIdDan(exchangeDTO);
        if(n < 0){
            log.info("插入商户积分操作记录失败");

            throw new RuntimeException("插入商户积分操作记录失败");
        }

        GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
        generalPointRecordPO.setPointId(1L);
        generalPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
        generalPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_TRANSFER_OUT);
        generalPointRecordPO.setMemo(exchangeDTO.getMemo());
        generalPointRecordPO.setNum(pointPO.getNumSend());
        generalPointRecordPO.setCreatedAt(TimeUtil.timestamp());
        generalPointRecordPO.setUpdatedAt(TimeUtil.timestamp());
        generalPointRecordPO.setUid(pointPO.getUserId());
        generalPointRecordPO.setOppositeUid(pointPO.getOpUserId());

        //将发送者的发送通用积分记录插入general_record表
       int i =  generalPointRecordMapper.insertSelective(generalPointRecordPO);
        if(i < 0){
            log.info("通用积分记录插入失败");

            throw new RuntimeException("通用积分记录插入失败");
        }
        map.put("sendRecord",generalPointRecordPO);

        Example example = new Example(GeneralPointRelationPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",String.valueOf(exchangeDTO.getOppositeUid()));
        criteria.andEqualTo("platformId",platformId);

        //查询接收者的通用积分信息
        GeneralPointRelationPO generalPointRelationPOOtherQuery = generalPointRelationMapper.selectOneByExample(example);


        GeneralPointRecordPO generalPointRecordPOOther = new GeneralPointRecordPO();
        generalPointRecordPOOther.setCreatedAt(TimeUtil.timestamp());
        generalPointRecordPOOther.setUpdatedAt(generalPointRecordPOOther.getCreatedAt());
        generalPointRecordPOOther.setType(GeneralPointRecordConstant.POINT_ADD);
        generalPointRecordPOOther.setCategory(GeneralPointRecordConstant.CATEGORY_USER_TRANSFER_IN);

        if (generalPointRelationPOOtherQuery == null) {

            GeneralPointRelationPO generalPointRelationPO = new GeneralPointRelationPO();
            generalPointRelationPO.setPlatformId(platformId);
            generalPointRelationPO.setUserId(pointPO.getOpUserId());
            generalPointRelationPO.setNum(pointPO.getNumSend());

            //接收者插入通用积分
           int k =  generalPointRelationMapper.insertSelective(generalPointRelationPO);

           if(k < 0){
               log.info("接收者插入通用积分失败");

               throw new RuntimeException("接收者插入通用积分失败");
           }

            generalPointRecordPOOther.setOppositeUid(pointPO.getUserId());
            generalPointRecordPOOther.setUid(pointPO.getOpUserId());
            generalPointRecordPOOther.setMemo(exchangeDTO.getMemo());
            generalPointRecordPOOther.setNum(pointPO.getNumSend());
            generalPointRecordPOOther.setPointId(1L);

            //将接收者的发送通用积分记录插入general_record表
           k =  generalPointRecordMapper.insertSelective(generalPointRecordPOOther);
            if(k < 0){
                log.info("通用积分记录插入失败");

                throw new RuntimeException("通用积分记录插入失败");
            }
            map.put("acceptRecord",generalPointRecordPOOther);


        } else {

            exchangeGeneral = generalPointRelationPOOtherQuery.getNum().add(pointPO.getNumSend());
            exchangeDTO.setNum(exchangeGeneral);
            //将对方的uid传给uid，以方便对代码的复用
            exchangeDTO.setUid(exchangeDTO.getOppositeUid());
            // //接收者有该平台积分则update新数据
            memberPointDao.updateGeneralPointByPlatformIdAndUserIdDan(exchangeDTO);

            generalPointRecordPOOther.setOppositeUid(pointPO.getUserId());
            generalPointRecordPOOther.setUid(generalPointRelationPOOtherQuery.getUserId());
            generalPointRecordPOOther.setMemo(exchangeDTO.getMemo());
            generalPointRecordPOOther.setNum(pointPO.getNumSend());
            generalPointRecordPOOther.setPointId(1L);
            //将接收者的接收到的通用积分记录插入general_record表
           int k =  generalPointRecordMapper.insertSelective(generalPointRecordPOOther);
            if(k < 0){
                log.info("通用积分记录插入失败");

                throw new RuntimeException("通用积分记录插入失败");
            }
            map.put("acceptRecord",generalPointRecordPOOther);

        }
        return map;
    }


}
