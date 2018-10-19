package com.fuzamei.bonuspoint.blockchain.bc;

import brokerpoints.Brokerpoints;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.util.HttpClientUtil;
import com.fuzamei.bonuspoint.util.SnowFlakeUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fzm.blockchain.constant.BlankChainUrlConst;
import com.fzm.blockchain.entity.DataBean;
import com.fzm.blockchain.entity.RequestBean;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.SeedResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author wangtao
 * @create 2018/7/23
 */
@Component
@Slf4j
@RefreshScope
public class InitBC {

    private final BlockChainUtil blockChainUtil;
    @Autowired
    public InitBC(BlockChainUtil blockChainUtil) {
        this.blockChainUtil = blockChainUtil;
    }

    @Value("${blockChain.url}")
    private String blockChainUrl;
    @Value("${blockChain.pwd}")
    private String blockChainPwd;

    @Value("${key.init.publicKey}")
    private String initPublicKey;
    @Value("${key.init.privateKey}")
    private String initPrivateKey;

    @Value("${key.platform.publicKey}")
    private String platformPublicKey;


    public Boolean seed() {
        // lang=0:英语，lang=1:简体汉字
        DataBean requestSeedBean = new DataBean();
        requestSeedBean.setLang(0);
        RequestBean requestBean = new RequestBean<>(requestSeedBean, BlankChainUrlConst.GEN_SEED);
        String requestBeanStr = JSON.toJSONString(requestBean);
        // 发送区块链
        String httpPostResult = HttpClientUtil.httpPost(blockChainUrl, requestBeanStr);
        log.info("区块链返回结果：\n\r{}", httpPostResult);
        ResponseBean<SeedResultBean> responseSeedBean = JSONObject.parseObject(httpPostResult, new TypeReference<ResponseBean<SeedResultBean>>(){});
        if(responseSeedBean == null || responseSeedBean.getResult() == null || StringUtil.isBlank(responseSeedBean.getResult().getSeed())){
            return false;
        }
        String seed = responseSeedBean.getResult().getSeed();
        requestSeedBean = new DataBean();
        requestSeedBean.setSeed(seed);
        requestSeedBean.setPasswd(blockChainPwd);
        requestBean = new RequestBean<>(requestSeedBean, BlankChainUrlConst.SAVE_SEED);
        requestBeanStr = JSON.toJSONString(requestBean);
        // 发送区块链
        httpPostResult = HttpClientUtil.httpPost(blockChainUrl, requestBeanStr);
        log.info("区块链返回结果：\n\r{}", httpPostResult);
        responseSeedBean = JSONObject.parseObject(httpPostResult, new TypeReference<ResponseBean<SeedResultBean>>(){});
        return !(responseSeedBean == null || responseSeedBean.getResult() == null || !responseSeedBean.getResult().getIsOK());
    }


    public ResponseBean<String> initTPlatform() throws Exception{
        log.info("总平台初始化");
        // 设置具体业务的参数
        Brokerpoints.PointsCreateTPlatform.Builder builder = Brokerpoints.PointsCreateTPlatform.newBuilder();
        // 设置请求的参数
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setCreateTPlatform(builder);
        request.setTy(Brokerpoints.ActionType.CreateTPlatform_VALUE);
       // request.setInstructionId(1234567);
        return blockChainUtil.sendTransaction(initPublicKey, initPrivateKey, request.build());
    }


    public ResponseBean<String> initPlatform() throws Exception{
        log.info("平台初始化");
        // 设置具体业务的参数
        Brokerpoints.PointsCreatePlatform.Builder builder = Brokerpoints.PointsCreatePlatform.newBuilder();
        builder.setId("1");
        builder.setCashRate("1");
        builder.setPointRate("0.5");
        // 设置请求的参数
        Brokerpoints.PointsAction.Builder request = Brokerpoints.PointsAction.newBuilder();
        request.setCreatePlatform(builder);
        request.setTy(Brokerpoints.ActionType.CreatePlatform_VALUE);
        return blockChainUtil.sendTransaction(initPublicKey, initPrivateKey, request.build());
    }




}
