package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fzm.blockchain.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-30 16:09
 **/
@Component
@Slf4j
@RefreshScope
public class CompanyBC {

    @Value("${blockChain.url}")
    private String blockChainUrl;
    private final BlockChainUtil blockChainUtil;

    @Autowired
    public CompanyBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    /**
     * @param companyId  商户id
     * @param cash       金额
     * @param add        存取款类型
     * @param publicKey  商户公钥
     * @param privateKey 商户私钥
     * @return 上链结果
     * @author qbanxiaoli
     * @description 商户存取款
     */
    public ResponseBean<String> accessCash(String companyId, String cash, Boolean add, String publicKey, String privateKey) {
        log.info("商户存取款上链开始：");
        Brokerpoints.PointsAccessCash.Builder builder = Brokerpoints.PointsAccessCash.newBuilder();
        builder.setId(companyId);
        builder.setCash(cash);
        builder.setAdd(add);
        builder.setAccountType(Brokerpoints.AccountType.MERCHANT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setAccessCash(builder);
        request.setTy(Brokerpoints.ActionType.AccessCash_VALUE);
        ResponseBean<String> responseBean = null;
        try {
            responseBean = blockChainUtil.sendTransaction(publicKey, privateKey, request.build());
        } catch (Exception e) {
            log.error(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }
        if (responseBean == null) {
            log.error("商户存取款上链失败：null");
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        } else if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("商户存取款上链失败：{}", responseBean.getError());
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }

    /**
     * @param companyId  商户id
     * @param pointId    积分id
     * @param quantity   积分数量
     * @param expiration 过期时间
     * @param userId     用户id
     * @param publicKey  商户公钥
     * @param privateKey 商户私钥
     * @return 上链结果
     * @author qbanxiaoli
     * @description 向用户发放积分
     */
    public ResponseBean<String> giveUserPoints(String companyId, String pointId, Long expiration,
                                               String quantity, String userId, String publicKey, String privateKey) {
        log.info("向用户发放积分上链开始：");
        Brokerpoints.PointsGiveUserPoints.Builder builder = Brokerpoints.PointsGiveUserPoints.newBuilder();
        builder.setId(companyId);
        builder.setPointId(pointId);
        builder.setExpiration(expiration);
        builder.setQuantity(quantity);
        builder.setUserId(userId);
        builder.setAccountType(Brokerpoints.AccountType.MERCHANT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setGiveUserPoints(builder);
        request.setTy(Brokerpoints.ActionType.GiveUserPoints_VALUE);
        ResponseBean<String> responseBean = null;
        try {
            responseBean = blockChainUtil.sendTransaction(publicKey, privateKey, request.build());
        } catch (Exception e) {
            log.error(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }
        if (responseBean == null) {
            log.error("向用户发放积分上链失败：null");
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        } else if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("向用户发放积分上链失败：{}", responseBean.getError());
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }

    /**
     * @param companyId  商户id
     * @param points     积分数量
     * @param cash       兑换金额数量
     * @param publicKey  商户公钥
     * @param privateKey 商户私钥
     * @return 上链结果
     * @author qbanxiaoli
     * @description 商户结算通用积分
     */
    public ResponseBean<String> sellGenerelPoints(String companyId, String points, String cash, String publicKey, String privateKey) {
        log.info("商户结算通用积分上链开始：");
        Brokerpoints.PointsSellGeneralPoints.Builder builder = Brokerpoints.PointsSellGeneralPoints.newBuilder();
        builder.setMerchantId(companyId);
        builder.setPoints(points);
        builder.setCash(cash);
        builder.setPointType(Brokerpoints.PointType.PPOINT);
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setSellGeneralPoints(builder);
        request.setTy(Brokerpoints.ActionType.SellGeneralPoints_VALUE);
        ResponseBean<String> responseBean = null;
        try {
            responseBean = blockChainUtil.sendTransaction(publicKey, privateKey, request.build());
        } catch (Exception e) {
            log.error(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }
        if (responseBean == null) {
            log.error("商户结算通用积分上链失败：null");
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        } else if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.error("商户结算通用积分上链失败：{}", responseBean.getError());
            log.error(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }

}
