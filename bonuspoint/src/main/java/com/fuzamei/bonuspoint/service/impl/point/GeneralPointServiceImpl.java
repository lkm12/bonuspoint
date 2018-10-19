package com.fuzamei.bonuspoint.service.impl.point;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;

import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRecordMapper;

import com.fuzamei.bonuspoint.dao.common.mapper.PlatformInfoMapper;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRelationDao;
import com.fuzamei.bonuspoint.entity.dto.point.SendPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.point.GeneralPointService;
import com.fuzamei.bonuspoint.service.point.PlatformPointService;
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
import tk.mybatis.mapper.entity.Example;

import java.util.concurrent.CompletableFuture;

/**
 * @program: bonus-point-cloud
 * @description: 通用积分
 * @author: WangJie
 * @create: 2018-07-04 10:29
 **/
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class GeneralPointServiceImpl implements GeneralPointService {
    @Value("${method.chain}")
    private boolean chainSwitch;
    private final GeneralPointRecordMapper generalPointRecordMapper;
    private final GeneralPointRelationDao generalPointRelationDao;
    private final BlockInfoDao blockInfoDao;
    private final AccountDao accountDao;
    private final BlockChainUtil blockChainUtil;
    private final PlatformBC platformBC;
    private final PlatformPointService platformPointService;

    @Autowired
    public GeneralPointServiceImpl(GeneralPointRecordMapper generalPointRecordMapper, GeneralPointRelationDao generalPointRelationDao,
                                   BlockInfoDao blockInfoDao, AccountDao accountDao, BlockChainUtil blockChainUtil,
                                   PlatformBC platformBC, PlatformPointService platformPointService) {
        this.generalPointRecordMapper = generalPointRecordMapper;
        this.generalPointRelationDao = generalPointRelationDao;
        this.blockInfoDao = blockInfoDao;
        this.accountDao = accountDao;
        this.blockChainUtil = blockChainUtil;
        this.platformBC = platformBC;
        this.platformPointService = platformPointService;
    }

    @Override
    public ResponseVO sendGeneralPoint(SendPointDTO sendPointDTO) {
        //检查用户是否在该平台下
        AccountPO accountPO = new AccountPO();
        accountPO.setPId(sendPointDTO.getFromId());
        accountPO.setId(sendPointDTO.getToId());
        accountPO.setRole(Roles.MEMBER);
        int count = accountDao.selectCount(accountPO);
        if (count != 1) {
            return new ResponseVO(UserResponseEnum.USER_NOT_EXIST);
        }

        GeneralPointInfoPO generalPointInfoPO = platformPointService.getGeneralPointInfoByPlatformId(sendPointDTO.getFromId());
        sendPointDTO.setPointId(generalPointInfoPO.getId());

        KeyDTO platformKey = accountDao.getUserKeyById(sendPointDTO.getFromId());

        ResponseBean<String> responseBean = platformBC.sendGeneralPointToMember(sendPointDTO.getFromId(), sendPointDTO.getPointId(), sendPointDTO.getNum(), sendPointDTO.getToId(), platformKey.getPublicKey(), platformKey.getPrivateKey());

        ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
        if (queryTransaction == null) {
            log.info("上链失败");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (StringUtil.isNotBlank(queryTransaction.getError())) {
            log.info("上链失败");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        log.info("上链成功");
        Long height = queryTransaction.getResult().getHeight();
        String hash = responseBean.getResult();

        CompletableFuture.runAsync(() -> {
            BlockInfoPO blockInfoPO = new BlockInfoPO(sendPointDTO.getFromId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.GiveUserPoints_VALUE);
            blockInfoDao.insertBlockInfo(blockInfoPO);
            blockInfoPO.setUid(sendPointDTO.getToId());
            int result = blockInfoDao.insertBlockInfo(blockInfoPO);
            if (result != 1) {
                log.error("平台发通用积分给用户，插入上链信息表失败，待插入信息为{}", blockInfoPO);
            }
        });

        //插入发起方减少记录
        GeneralPointRecordPO recordFrom = new GeneralPointRecordPO();
        BeanUtils.copyProperties(sendPointDTO,recordFrom);
        recordFrom.setUid(sendPointDTO.getFromId());
        recordFrom.setOppositeUid(sendPointDTO.getToId());
        recordFrom.setType(GeneralPointRecordConstant.POINT_SUB);
        recordFrom.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
        recordFrom.setHeight(height);
        recordFrom.setHash(hash);
        CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(recordFrom))
        .whenComplete((result,e)->{
            if (e!=null || result!=1){
                if (e!=null){
                    log.error(e.getMessage());
                }
                log.error("上链成功，写入数据库失败！平台发通用积分给用户,记录发起方减少积分记录写入数据库失败，待写入信息为:{}",recordFrom);
            }
        });
        //插入接收方积分增加记录
        GeneralPointRecordPO recordTo = new GeneralPointRecordPO();
        BeanUtils.copyProperties(sendPointDTO,recordTo);
        recordTo.setCreatedAt(TimeUtil.timestamp());
        recordTo.setUid(sendPointDTO.getToId());
        recordTo.setOppositeUid(sendPointDTO.getFromId());
        recordTo.setType(GeneralPointRecordConstant.POINT_ADD);
        recordTo.setCategory(GeneralPointRecordConstant.CATEGORY_USER_GRANT_IN);
        recordTo.setHeight(height);
        recordTo.setHash(hash);
        CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(recordTo)
        ).whenComplete((result,e)->{
            if (e!=null || result!=1){
                if (e!=null){
                    log.error(e.getMessage());
                }
                log.error("上链成功，写入数据库失败！平台发通用积分给用户,记录接收方增加积分记录写入数据库失败，待写入信息为:{}",recordTo);
            }
        });
        //用户通用积分数量增加
        boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(sendPointDTO.getToId(), sendPointDTO.getFromId());
        if (!isGeneralRelationExist) {
            generalPointRelationDao.createGeneralPointRelation(sendPointDTO.getToId(), sendPointDTO.getFromId());
        }
        generalPointRelationDao.updateGeneralPointNumByUid(sendPointDTO.getToId(), sendPointDTO.getNum());
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }
}
