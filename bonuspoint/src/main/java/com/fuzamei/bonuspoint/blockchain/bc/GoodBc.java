package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bean.Commodity;
import com.fuzamei.bonuspoint.blockchain.bean.MerchantPoint;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fzm.blockchain.entity.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author lmm
 * @description 购买商品
 * @create 2018/7/31 14:45
 */

@Component
@Slf4j
@RefreshScope
public class GoodBc {

    private final BlockChainUtil blockChainUtil;

    @Autowired
    public GoodBc(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    /**
     * 购买商品上链
     * @param uid uid
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @param commodity 积分信息
     * @return
     */
    public ResponseBean<String> buyCommodity(Long uid ,String publicKey, String privateKey, Commodity commodity) throws Exception{
        log.info("购买商品上链");
        Brokerpoints.PointsBuyCommodity.Builder builder = Brokerpoints.PointsBuyCommodity.newBuilder();
        builder.setUserId(uid.toString());
        builder.setMerchantId(commodity.getMerchantId().toString());
        Brokerpoints.PointType pointType = null;
        if (commodity.getPointType().intValue() == Brokerpoints.PointType.TPOINT_VALUE){
            pointType = Brokerpoints.PointType.TPOINT;
            builder.setPointType(pointType);
            builder.setTgeneral(commodity.getTgeneral().toString());

        }else if (commodity.getPointType().intValue() == Brokerpoints.PointType.PPOINT_VALUE){
            pointType = Brokerpoints.PointType.PPOINT;
            builder.setPointType(pointType);
            builder.setGeneral(commodity.getGeneral().toString());
        }else  if (commodity.getPointType().intValue() == Brokerpoints.PointType.MPOINT_VALUE){
            pointType = Brokerpoints.PointType.MPOINT;
            builder.setPointType(pointType);
            for (MerchantPoint merchantPoint:commodity.getMerchantPoints()) {
                Brokerpoints.MerchantPoint.Builder  point = Brokerpoints.MerchantPoint.newBuilder();
                point.setId(merchantPoint.getId().toString());
                point.setQuantity(merchantPoint.getQuantity().floatValue());
                if (merchantPoint.getExpiration() != null) {
                    point.setExpiration(merchantPoint.getExpiration());
                }
                builder.addMerchantPoints(point);
            }
        }else {
            throw new Exception("积分类型错误");
        }
        // 设置请求的参数
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setBuyCommodity(builder);
        request.setTy(Brokerpoints.ActionType.BuyCommodity_VALUE);
        return blockChainUtil.sendTransaction(publicKey,privateKey,request.build());
    }


}
