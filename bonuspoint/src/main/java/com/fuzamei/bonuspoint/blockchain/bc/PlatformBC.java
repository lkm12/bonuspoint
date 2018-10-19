package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.util.SnowFlakeUtil;
import com.fzm.blockchain.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-30 16:12
 **/
@Component
@Slf4j
@RefreshScope
public class PlatformBC {


    @Value("${blockChain.url}")
    private String blockChainUrl;

    private final BlockChainUtil blockChainUtil;

    @Autowired
    public PlatformBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    /**
     * 平台添加集团
     *
     * @param companyId          集团信息id
     * @param platformId         平台id
     * @param cashRate           备付金比例
     * @param pointRate          积分兑换比例
     * @param platformPublicKey  平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */
    public ResponseBean<String> createGroup(Long companyId, Long platformId, String cashRate, String pointRate, String platformPublicKey, String platformPrivateKey) {
        log.info("平台添加集团上链");
        Brokerpoints.PointsCreateMerchant.Builder builder = Brokerpoints.PointsCreateMerchant.newBuilder();
        builder.setPointRate(pointRate);
        builder.setCashRate(cashRate);
        builder.setId(companyId.toString());
        builder.setPlatformId(platformId.toString());
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setCreateMerchant(builder);
        request.setTy(Brokerpoints.ActionType.CreateMerchant_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());

        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }


    /**
     * 平台设置集团备付金比例
     *
     * @param companyId          平台id
     * @param cashRate           备付金比例
     * @param platformPublicKey  平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */

    public ResponseBean<String> setCashRate(Long companyId, String cashRate, String platformPublicKey, String platformPrivateKey) {
        log.info("平台设置集团备付金比例上链开始：");
        Brokerpoints.PointsSetCashRate.Builder builder = Brokerpoints.PointsSetCashRate.newBuilder();
        builder.setCashRate(cashRate);
        builder.setId(companyId.toString());
        builder.setAccountType(Brokerpoints.AccountType.MERCHANT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setSetCashRate(builder);
        request.setTy(Brokerpoints.ActionType.SetCashRate_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;

    }

    /**
     * 平台设置平台积分兑换人民币比率
     *
     * @param platformId          平台id
     * @param pointRate          积分兑换比例
     * @param platformPublicKey  平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */

    public ResponseBean<String> setPlatformPointRate(Long platformId, String pointRate, String platformPublicKey, String platformPrivateKey) {
        log.info("平台设置集团积分兑换比例上链开始：");
        Brokerpoints.PointsSetPointRate.Builder builder = Brokerpoints.PointsSetPointRate.newBuilder();
        builder.setId(platformId.toString());
        builder.setPointRate(pointRate);
        builder.setAccountType(Brokerpoints.AccountType.PLATFORM);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setSetPointRate(builder);
        request.setTy(Brokerpoints.ActionType.SetPointRate_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }


    /**
     * 平台设置集团积分兑换比例
     *
     * @param companyId          商户id
     * @param pointRate          积分兑换比例
     * @param platformPublicKey  平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */
    public ResponseBean<String> setCompanyPointRate(Long companyId, String pointRate, String platformPublicKey, String platformPrivateKey) {
        log.info("平台设置集团积分兑换比例上链开始：");
        Brokerpoints.PointsSetPointRate.Builder builder = Brokerpoints.PointsSetPointRate.newBuilder();
        builder.setId(companyId.toString());
        builder.setPointRate(pointRate);
        builder.setAccountType(Brokerpoints.AccountType.MERCHANT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setSetPointRate(builder);
        request.setTy(Brokerpoints.ActionType.SetPointRate_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }


    /**
     * 平台发积分给用户
     * @param platformId 平台id
     * @param pointId 积分id
     * @param num 积分数量
     * @param receiverId 接收用户id
     * @param platformPublicKey 平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */
    public ResponseBean<String> sendGeneralPointToMember(Long platformId, Long pointId, BigDecimal num, Long receiverId, String platformPublicKey, String platformPrivateKey) {
        log.info("转积分上链");
        Brokerpoints.PointsGiveUserPoints.Builder builder = Brokerpoints.PointsGiveUserPoints.newBuilder();
        builder.setId(platformId.toString());
        builder.setAccountType(Brokerpoints.AccountType.PLATFORM);
        builder.setPointId(pointId.toString());
        builder.setQuantity(num.toString());
        builder.setUserId(receiverId.toString());
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setGiveUserPoints(builder);
        request.setTy(Brokerpoints.ActionType.GiveUserPoints_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            log.error("失败信息接收人id{},",receiverId);
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }

    /**
     * 平台发通用积分给商户
     * @param companyId 集团信息id
     * @param num 积分数量
     * @param platformPublicKey 平台公钥
     * @param platformPrivateKey 平台私钥
     * @return
     */
    public ResponseBean<String> giveMerchantGPoints(Long companyId , BigDecimal num,String platformPublicKey, String platformPrivateKey){
        log.info("转积分上链");
        Brokerpoints.PointsGiveMerchantGPoints.Builder builder = Brokerpoints.PointsGiveMerchantGPoints.newBuilder();
        builder.setMerchantId(companyId.toString());
        builder.setPointType(Brokerpoints.PointType.PPOINT);
        builder.setPoints(num.toString());
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setGiveMerchantGPoints(builder);
        request.setTy(Brokerpoints.ActionType.GiveMerchantGPoints_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.error("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }
}
