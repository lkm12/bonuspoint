package com.fuzamei.bonuspoint.blockchain.util;

import brokerpoints.Brokerpoints;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.alibaba.fastjson.TypeReference;
import com.fuzamei.bonuspoint.util.HexUtil;
import com.fuzamei.bonuspoint.util.HttpClientUtil;
import com.fuzamei.bonuspoint.util.SnowFlakeUtil;
import com.fzm.blockchain.constant.BlankChainUrlConst;
import com.fzm.blockchain.entity.DataBean;
import com.fzm.blockchain.entity.RequestBean;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.fzm.blockchain.util.ED25519Util;
import com.google.protobuf.ByteString;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author wangtao, lkm
 * @create 2018/6/15
 */
@Slf4j
@Component
@RefreshScope
public class BlockChainUtil {

   @Value("${blockChain.url}")
    private String blockChainUrl;
    @Value("${blockChain.to}")
    private String blockChainTo;
    @Value("${blockChain.execer}")
    private String blockChainExecer;

    private static final String TX_NOT_EXIST = "tx not exist";


    public ResponseBean<String> sendTransaction(String publicKey, String privateKey, Brokerpoints.PointsAction pointsAction ) throws Exception{
        // 构建并设置Transaction
        Brokerpoints.Transaction.Builder transaction = Brokerpoints.Transaction.newBuilder();

        // 合约名
        transaction.setExecer(ByteString.copyFrom(blockChainExecer.getBytes(StandardCharsets.UTF_8)));
        // 合约内容
        transaction.setPayload(pointsAction.toByteString());
        // 手续费，随便填
        transaction.setFee(1000000);
        // 随机ID，可以防止payload相同的时候，交易重复，不用记录
        transaction.setNonce(SnowFlakeUtil.CHAIN.nextId());
        // 对方地址，如果没有对方地址，可以为空
        transaction.setTo(blockChainTo);
        // 将请求参数签名
        ByteString sign = ED25519Util.sign(transaction.build().toByteArray(), HexUtil.hexToBytes(privateKey));

        // 构建签名，并设置请求的签名参数
        Brokerpoints.Signature.Builder signature = Brokerpoints.Signature.newBuilder();
        // ty = 1 -> secp256k1
        // ty = 2 -> ed25519
        // ty = 3 -> sm2
        // ty = 4 -> OnetimeED25519
        // ty = 5 -> RingBaseonED25519
        signature.setTy(2);
        signature.setPubkey(ByteString.copyFrom(HexUtil.hexToBytes(publicKey)));
        signature.setSignature(sign);

        transaction.setSignature(signature);

        String transactionData = HexUtil.bytesToHex(transaction.build().toByteArray());
        DataBean bean = new DataBean();
        bean.setData(transactionData);
        RequestBean requestBean = new RequestBean<>(bean);
        String requestBeanStr = JSON.toJSONString(requestBean);
        // 发送区块链
        String httpPostResult = HttpClientUtil.httpPost(blockChainUrl, requestBeanStr);
        log.info("区块链返回结果：\n\r{}", httpPostResult);
        return JSONObject.parseObject(httpPostResult, new TypeReference<ResponseBean<String>>(){});
    }

    /**
     * 根据哈希查询交易信息
     */
    public ResponseBean<TransactionResultBean> queryTransaction(String hash){
        for(int i=0; i<30; i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.info("查询区块链时线程休眠出错{}",e.getMessage());
            }
            DataBean bean = new DataBean();
            bean.setHash(hash);
            RequestBean requestBean = new RequestBean<>(bean, BlankChainUrlConst.QUERY_TRANSACTION);
            String requestBeanStr = JSON.toJSONString(requestBean);
            // 发送区块链
            String httpPostResult = HttpClientUtil.httpPost(blockChainUrl, requestBeanStr);

            ResponseBean<TransactionResultBean> queryTransaction = JSONObject.parseObject(httpPostResult, new TypeReference<ResponseBean<TransactionResultBean>>(){});
            if(queryTransaction == null){
                continue;
            }
            if(queryTransaction.getError() != null || queryTransaction.getResult() == null){
                if(TX_NOT_EXIST.equals(queryTransaction.getError())){
                    continue;
                }
                log.info("区块链返回结果：\n\r{}", httpPostResult);
                return checkQueryTransaction(queryTransaction);
            }else {
                log.info("区块链返回结果：\n\r{}", httpPostResult);
                return checkQueryTransaction(queryTransaction);
            }
        }
        log.info("根据哈希查询交易信息失败：null");
        return null;
    }

    private ResponseBean<TransactionResultBean> checkQueryTransaction(ResponseBean<TransactionResultBean> queryTransaction){
        if(queryTransaction == null){
            log.info("根据哈希查询交易信息失败：null");
            return null;
        }
        if(queryTransaction.getError() != null || queryTransaction.getResult() == null){
            log.info("根据哈希查询交易信息失败：{}", queryTransaction.getError());
            return null;
        }

        if(queryTransaction.getResult().getReceipt().getTy() != 2){
            log.info("根据哈希查询交易信息失败：{}", queryTransaction.getResult().getReceipt().getLogs().get(0).getLog());
            return null;
        }
        log.info("根据哈希查询交易信息成功");
        return queryTransaction;
    }

}
