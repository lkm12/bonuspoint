package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fzm.blockchain.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * lkm
 */
@Slf4j
@Component
@RefreshScope
public class MainPlatformBC {

    private final BlockChainUtil blockChainUtil;

    public MainPlatformBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    /**
     * 创建平台
     *
     * @param id 平台id
     * @param cashRate 备付金比率
     * @param pointRate 积分兑换比率
     * @param mainPlatformPrivateKey 总平台私钥
     * @param mainPlatformPublicKey 总平台公钥
     * @throws Exception
     */
    public ResponseBean<String> createPlatform(String id, String cashRate, String pointRate, String mainPlatformPublicKey, String mainPlatformPrivateKey)  {

        Brokerpoints.PointsCreatePlatform.Builder builder = Brokerpoints.PointsCreatePlatform.newBuilder();
        //平台id
        builder.setId(id);
        //备付金比率
        builder.setCashRate(cashRate);
        //商户积分兑换比率
        builder.setPointRate(pointRate);

        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();

        request.setCreatePlatform(builder);
        request.setTy(Brokerpoints.ActionType.CreatePlatform_VALUE);

        //发送区块链
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(mainPlatformPublicKey, mainPlatformPrivateKey, request.build());

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
