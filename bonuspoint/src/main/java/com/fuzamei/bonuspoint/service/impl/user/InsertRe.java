package com.fuzamei.bonuspoint.service.impl.user;

import com.fuzamei.bonuspoint.blockchain.bc.MemberBC;
import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.*;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Map;
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InsertRe {


    private final MemberBC memberBC;

    private final BlockInfoMapper blockInfoMapper;
    private final GeneralPointRecordMapper generalPointRecordMapper;
    private final PointRecordMapper pointRecordMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final PointInfoMapper pointInfoMapper;
    private final PointRelationMapper pointRelationMapper;
    private final GeneralPointRelationMapper generalPointRelationMapper;
    private final MemberPointDao memberPointDao;

    public InsertRe(MemberBC memberBC,BlockInfoMapper blockInfoMapper, GeneralPointRecordMapper generalPointRecordMapper, PointRecordMapper pointRecordMapper,
                    CompanyInfoMapper companyInfoMapper, PointInfoMapper pointInfoMapper, PointRelationMapper pointRelationMapper,
                    GeneralPointRelationMapper generalPointRelationMapper, MemberPointDao memberPointDao) {

        this.memberBC = memberBC;
        this.blockInfoMapper = blockInfoMapper;
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.pointRecordMapper = pointRecordMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.pointInfoMapper = pointInfoMapper;
        this.pointRelationMapper = pointRelationMapper;
        this.generalPointRelationMapper = generalPointRelationMapper;
        this.memberPointDao = memberPointDao;
    }


        public void insertPoint(String s,AccountPO accountPO2){
            Long companyId = Long.valueOf(s.split(":")[0]);
            BigDecimal rewardNum = new BigDecimal(s.split(":")[1]);
            Long pointId = Long.valueOf(s.split(":")[2]);
            Long end = Long.valueOf(s.split(":")[3]);
            Long userId = Long.valueOf(s.split(":")[4]);

            CompanyInfoPO companyInfoPO = companyInfoMapper.selectByPrimaryKey(companyId);

            Long height;
            String hash;
            Map<String,Object> map;

            PointInfoPO pointInfo = new PointInfoPO();
            pointInfo.setId(pointId);
            PointInfoPO pointInfoPO = pointInfoMapper.selectOne(pointInfo);

            PointInfoPO pointInfoPORemqinNum = new PointInfoPO();
            pointInfoPORemqinNum.setId(pointId);
            pointInfoPORemqinNum.setNumRemain( pointInfoPO.getNumRemain().subtract(rewardNum));

            //修改商户的剩余积分
            int i = pointInfoMapper.updateByPrimaryKeySelective(pointInfoPORemqinNum);
            if (i < 0) {
                throw new RuntimeException("修改商户剩余积分失败");
            }

            Example examplePointRelation = new Example(PointRelationPO.class);
            Example.Criteria criteria = examplePointRelation.createCriteria();
            criteria.andEqualTo("pointId", pointId);
            criteria.andEqualTo("userId", userId);
            //查询此用户是否有该积分
            PointRelationPO pointRelationPO = pointRelationMapper.selectOneByExample(examplePointRelation);

            if (pointRelationPO == null) {
                PointRelationPO pointRelation = new PointRelationPO();
                pointRelation.setNum(rewardNum);
                pointRelation.setUserId(userId);
                pointRelation.setPointId(pointId);

                i = pointRelationMapper.insertSelective(pointRelation);
                if (i < 0) {
                    log.info("添加商户积分失败");
                    throw new RuntimeException("添加商户积分失败");
                }

            } else {
                PointRelationPO pointRelation = new PointRelationPO();
                BigDecimal numNew = pointRelationPO.getNum().add(rewardNum);
                pointRelation.setId(pointRelationPO.getId());
                pointRelation.setNum(numNew);
                i = pointRelationMapper.updateByPrimaryKeySelective(pointRelation);
                if (i < 0) {
                    log.info("更新商户积分失败");
                    throw new RuntimeException("更新商户积分失败");
                }
            }

            synchronized (UserServiceImpl.class) {
                try {
                    map = memberBC.giveUserPoints(String.valueOf(companyId), String.valueOf(pointId), end, String.valueOf(rewardNum),
                            String.valueOf(userId), accountPO2.getPublicKey(), accountPO2.getPrivateKey());

                    height = (Long) map.get("height");
                    hash = map.get("hash").toString();
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException();
                }

            }

            //接收者的记录对象
            PointRecordPO pointRecordPO = new PointRecordPO();
            pointRecordPO.setPointRate(companyInfoPO.getPointRate());
            pointRecordPO.setUid(userId);
            pointRecordPO.setOppositeUid(companyInfoPO.getUid());
            pointRecordPO.setPointId(pointId);
            pointRecordPO.setMemo("注册赠送的积分");
            pointRecordPO.setNum(rewardNum);
            pointRecordPO.setCreatedAt(TimeUtil.timestamp());
            pointRecordPO.setUpdatedAt(TimeUtil.timestamp());
            pointRecordPO.setHash(hash);
            pointRecordPO.setHeight(height);
            pointRecordPO.setCategory(PointRecordConstant.CATEGORY_USER_INCOME);
            pointRecordPO.setType(PointRecordConstant.POINT_ADD);

            int k = pointRecordMapper.insertSelective(pointRecordPO);
            if (k < 0) {
                log.info("插入用户增加积分记录失败");
                throw new RuntimeException("插入用户增加积分记录失败");
            }
            PointRecordPO pointRecordCompany = new PointRecordPO();
            pointRecordCompany.setPointRate(companyInfoPO.getPointRate());
            pointRecordCompany.setUid(companyInfoPO.getUid());
            pointRecordCompany.setOppositeUid(userId);
            pointRecordCompany.setPointId(pointId);
            pointRecordCompany.setMemo("注册发放的积分");
            pointRecordCompany.setNum(rewardNum);
            pointRecordCompany.setCreatedAt(TimeUtil.timestamp());
            pointRecordCompany.setUpdatedAt(TimeUtil.timestamp());
            pointRecordCompany.setCategory(PointRecordConstant.CATEGORY_GROUP_ISSUED);
            pointRecordCompany.setType(PointRecordConstant.POINT_SUB);
            pointRecordCompany.setHash(hash);
            pointRecordCompany.setHeight(height);

            k = pointRecordMapper.insertSelective(pointRecordCompany);
            if (k < 0) {
                log.info("插入商户减少积分记录失败");
                throw new RuntimeException("插入商户减少积分记录失败");
            }

            BlockInfoPO blockInfoPO = new BlockInfoPO();
            blockInfoPO.setUid(userId);
            blockInfoPO.setHash(hash);
            blockInfoPO.setHeight(height);
            blockInfoPO.setOperationType((Integer) map.get("type"));
            blockInfoPO.setCreatedAt(TimeUtil.timestamp());
            int result =  blockInfoMapper.insert(blockInfoPO);
            if (result < 0) {
                log.info("插入操作表失败");
                throw new RuntimeException("插入操作表失败");
            }


        }

        public void insertGeneralPoint(String s, AccountPO accountPO2, PlatformInfoPO platform){

            Long userId = Long.valueOf(s.split(":")[1]);
            BigDecimal generalNum = new BigDecimal(s.split(":")[0]);
            Long pId = Long.valueOf(s.split(":")[2]);

            Long height;
            String hash;
            Map<String, Object> map;



            //查询接收者是否拥有此平台的积分
            Example example = new Example(GeneralPointRelationPO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId", userId);
            criteria.andEqualTo("platformId", pId);

            //查询接收者的通用积分信息
            GeneralPointRelationPO generalPointRelationPOOther = generalPointRelationMapper.selectOneByExample(example);


            if (generalPointRelationPOOther == null) {

                GeneralPointRelationPO generalPointRelationPO = new GeneralPointRelationPO();
                generalPointRelationPO.setNum(generalNum);
                generalPointRelationPO.setUserId(userId);
                generalPointRelationPO.setPlatformId(pId);

                //接收者没有该平台积分则insert新数据
                int i = generalPointRelationMapper.insertSelective(generalPointRelationPO);

                if (i < 0) {
                    log.info("接收者插入通用积分失败");

                    throw new RuntimeException("接收者插入通用积分失败");
                }


            } else {
                ExchangePointDTO exchangeDTO = new ExchangePointDTO();
                BigDecimal sumPoint = generalPointRelationPOOther.getNum().add(generalNum);
                exchangeDTO.setNum(sumPoint);
                exchangeDTO.setUid(userId);
                exchangeDTO.setPlatformId(pId);
                //接收者有该平台积分则update新数据
                int i = memberPointDao.updateGeneralPointByPlatformIdAndUserIdDan(exchangeDTO);
                if (i < 0) {
                    log.info("接收者修改通用积分失败");

                    throw new RuntimeException("接收者修改通用积分失败");
                }
            }

            synchronized (UserServiceImpl.class) {
                try {

                    map = memberBC.pointsGiveUserPoints(String.valueOf(platform.getUid()), Roles.PLATFORM, userId, generalNum,
                            accountPO2.getPublicKey(), accountPO2.getPrivateKey());

                    height = (Long) map.get("height");
                    hash = map.get("hash").toString();
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new RuntimeException();
                }

            }
            //注册者的积分记录
            GeneralPointRecordPO generalPointRecordPO = new GeneralPointRecordPO();
            generalPointRecordPO.setCreatedAt(TimeUtil.timestamp());
            generalPointRecordPO.setUpdatedAt(generalPointRecordPO.getCreatedAt());
            generalPointRecordPO.setCategory(GeneralPointRecordConstant.INVITECODE_REWARD);
            generalPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
            generalPointRecordPO.setOppositeUid(platform.getUid());
            generalPointRecordPO.setUid(userId);
            generalPointRecordPO.setPointId(1L);
            generalPointRecordPO.setNum(generalNum);
            generalPointRecordPO.setHash(hash);
            generalPointRecordPO.setHeight(height);
            generalPointRecordPO.setMemo("用户接收奖励积分");

            //将奖励积分操作记录插表
            int n = generalPointRecordMapper.insertSelective(generalPointRecordPO);
            if (n < 0) {
                log.info("奖励积分操作记录插表失败");
                throw new RuntimeException("奖励积分操作记录插表失败");
            }

            //平台的积分记录
            GeneralPointRecordPO generalPointPlatform = new GeneralPointRecordPO();
            generalPointPlatform.setCreatedAt(TimeUtil.timestamp());
            generalPointPlatform.setUpdatedAt(generalPointRecordPO.getCreatedAt());
            generalPointPlatform.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
            generalPointPlatform.setType(GeneralPointRecordConstant.POINT_SUB);
            generalPointPlatform.setOppositeUid(userId);
            generalPointPlatform.setUid(platform.getUid());
            generalPointPlatform.setPointId(1L);
            generalPointPlatform.setNum(generalNum);
            generalPointPlatform.setHash(hash);
            generalPointPlatform.setHeight(height);
            generalPointPlatform.setMemo("平台发给用户奖励积分");
            //将奖励积分操作记录插表
            n = generalPointRecordMapper.insertSelective(generalPointPlatform);
            if (n < 0) {
                log.info("奖励积分操作记录插表失败");
                throw new RuntimeException("奖励积分操作记录插表失败");
            }

            BlockInfoPO blockInfoPO = new BlockInfoPO();
            blockInfoPO.setUid(userId);
            blockInfoPO.setHash(hash);
            blockInfoPO.setHeight(height);
            blockInfoPO.setOperationType((Integer) map.get("type"));
            blockInfoPO.setCreatedAt(TimeUtil.timestamp());
            int result = blockInfoMapper.insert(blockInfoPO);
            if (result < 0) {
                throw new RuntimeException("插入操作表失败");
            }

        }
}
