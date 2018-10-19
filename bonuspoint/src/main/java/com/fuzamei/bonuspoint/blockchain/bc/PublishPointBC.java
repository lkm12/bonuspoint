package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.util.SnowFlakeUtil;
import com.fzm.blockchain.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author lmm
 * @description 商户发行积分
 * @create 2018/7/30 16:37
 */
@Component
@Slf4j
@RefreshScope
public class PublishPointBC {

    private final BlockChainUtil blockChainUtil;

    @Autowired
    public PublishPointBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    /**
     * 商户放行积分
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @param merchantId 商户id
     * @param pointId 发行的积分的id
     * @param points 发行的积分到期时间（以秒计数的Unix时间）
     * @param outTime
     * @return
     * @throws Exception
     */
    public ResponseBean<String> publish(String publicKey, String privateKey, Long merchantId,
                                        Long pointId, BigDecimal points, Long outTime) throws Exception {
        log.info("集团发行积分上链");
        //具体业务
        Brokerpoints.PointsCreateMerchantPoint.Builder builder = Brokerpoints.PointsCreateMerchantPoint.newBuilder();
        builder.setMerchantId(merchantId.toString());
        builder.setPointId(pointId.toString());
        builder.setPoints(points.toString());
        if (outTime != null){
            builder.setExpiration(outTime);
        }else {
            builder.setExpiration(Integer.MAX_VALUE);
        }

        // 设置请求的参数
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setCreateMerchantPoint(builder);
        request.setTy(Brokerpoints.ActionType.CreateMerchantPoint_VALUE);
        //request.setInstructionId(SnowFlakeUtil.CHAIN.nextId());

        return blockChainUtil.sendTransaction(publicKey, privateKey, request.build());
    }
    
}
