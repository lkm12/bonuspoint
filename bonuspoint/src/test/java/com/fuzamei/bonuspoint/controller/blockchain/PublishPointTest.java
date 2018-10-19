package com.fuzamei.bonuspoint.controller.blockchain;

import com.fuzamei.bonuspoint.blockchain.bc.PublishPointBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.common.util.JsonUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author lmm
 * @description
 * @create 2018/7/30 17:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PublishPointTest {
    @Autowired
    private PublishPointBC publishPointBC;
    @Value("${key.init.publicKey}")
    private String initPublicKey;
    @Value("${key.init.privateKey}")
    private String initPrivateKey;
    @Autowired
    private BlockChainUtil blockChainUtil;

    @Test
    public void  publish() throws Exception {
        ResponseBean<String> res =  publishPointBC.publish(initPublicKey,initPrivateKey,535L,1L,new BigDecimal(100.1010),System.currentTimeMillis());
        log.info(JsonUtil.toJsonString(res));
        ResponseBean<TransactionResultBean> responseBean =  blockChainUtil.queryTransaction(res.getResult());
        log.info("查询：" + JsonUtil.toJsonString(responseBean));
    }

}
