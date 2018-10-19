/**
 * FileName: MsgInitPlatformConfig
 * Author: wangtao
 * Date: 2018/5/3 12:15
 * Description:
 */
package com.fuzamei.bonuspoint.configuration;

import com.fuzamei.bonuspoint.blockchain.bc.InitBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 平台初始化
 *
 * @author wangtao
 * @create 2018/6/13
 */

@Slf4j
@Component
@RefreshScope
public class MsgInitPlatformListener implements ApplicationRunner {

    @Value("${platform.id}")
    private Long platformId;
    @Value("${key.init.privateKey}")
    private String privateKey;

    @Value("${key.init.publicKey}")
    private String publicKey;

    @Value("${key.platform.privateKey}")
    private String platformPrivateKey;

    @Value("${key.platform.publicKey}")
    private String platformPublicKey;

    private static boolean isStart = false;

    private final BlockChainUtil blockChainUtil;

    private InitBC initBC;

    @Autowired
    public MsgInitPlatformListener(BlockChainUtil blockChainUtil, InitBC initBC) {
        this.blockChainUtil = blockChainUtil;
        this.initBC = initBC;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!isStart) {
            isStart = true;
            // 获取种子
            initBC.seed();
            // 初始化
            ResponseBean<String> responseBean = initBC.initTPlatform();
            // 校验
            if (responseBean == null) {
                log.info("总平台初始化失败：null");
                return;
            }
            if (responseBean.getError() != null || responseBean.getResult() == null) {
                log.info("总平台初始化失败：{}", responseBean.getError());
                return;
            }

            ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
            if (queryTransaction == null) {
                log.info("总平台初始化失败");
                return;
            }
            if (StringUtil.isNotBlank(queryTransaction.getError())) {
                log.info("总平台初始化失败:{}" + queryTransaction.getError());
            }

        }
    }
}
