package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.util.SnowFlakeUtil;
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
 * @create: 2018-07-31 11:24
 **/
@Component
@Slf4j
@RefreshScope
public class PointBC {
    @Value("${blockChain.url}")
    private String blockChainUrl;

    private final BlockChainUtil blockChainUtil;

    @Autowired
    public PointBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }



    public ResponseBean<String> expirePoints(Long companyId , Long pointId ,  String platformPublicKey, String platformPrivateKey){
        log.info("积分过期上链");
        Brokerpoints.PointsExpirePoints.Builder builder = Brokerpoints.PointsExpirePoints.newBuilder();
        builder.setMerchantId(companyId.toString());
        builder.setPointId(pointId.toString());
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setExpirePoints(builder);
        request.setTy(Brokerpoints.ActionType.ExpirePoints_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());
        ResponseBean<String> responseBean;
        try {
            responseBean = blockChainUtil.sendTransaction(platformPublicKey, platformPrivateKey, request.build());
        } catch (Exception e) {
            throw new RuntimeException(BlockChainResponseEnum.BLOCK_CHAIN_ERROR.getMessage());
        }

        if (responseBean == null) {
            log.info("上链失败：null");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (responseBean.getError() != null || responseBean.getResult() == null) {
            log.info("上链失败：{}", responseBean.getError());
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        return responseBean;
    }

}
