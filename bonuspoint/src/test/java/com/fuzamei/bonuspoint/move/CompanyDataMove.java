package com.fuzamei.bonuspoint.move;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.constant.UserStatus;
import com.fuzamei.bonuspoint.dao.OldCompanyInfoDao;
import com.fuzamei.bonuspoint.dao.OldUserDao;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRelationMapper;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRelationPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.util.RandomUtil;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.fzm.blockchain.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @program: bonus-point-cloud
 * @description: 商户信息迁移
 * @author: WangJie
 * @create: 2018-08-17 10:25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CompanyDataMove {
    @Autowired
    OldCompanyInfoDao oldCompanyInfoDao;
    @Autowired
    OldUserDao oldUserDao;
    @Autowired
    PlatformBC platformBC;
    @Autowired
    BlockChainUtil blockChainUtil;
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountDao accountDao;
    @Autowired
    GeneralPointRelationMapper generalPointRelationMapper;
    @Autowired
    BlockInfoDao blockInfoDao;

    @Test
    public void companyInfoMove() {
        CompletableFuture<List<CompanyInfoPO>> future = CompletableFuture.supplyAsync(() -> oldCompanyInfoDao.getAllCompanyInfo());
        CompletableFuture<Void> f = future.thenApply(Collection::stream).thenAccept(
                companyInfoStream -> companyInfoStream.forEach(
                        companyInfoPO -> {
                            //集团数据迁移
                            companyInfoPO.setCashRate(companyInfoPO.getCashRate()*0.01F);
                            CompletableFuture<Void> f1 = CompletableFuture.supplyAsync(() -> companyInfoMapper.insert(companyInfoPO)).thenAccept(result -> {
                                if (result == 0) {
                                    log.info("插入集团:{}失败", companyInfoPO);
                                    throw new RuntimeException("插入集团失败");
                                }
                            });

                            CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> oldUserDao.getUserById(companyInfoPO.getUid())).thenAccept(
                                    accountPO -> {
                                        accountPO.setIsInitialize(UserStatus.UNINIT);
                                        accountPO.setRole(Roles.COMPANY);
                                        accountPO.setPId(1L);
                                        accountPO.setStatus(UserStatus.AVAILABLE);
                                        accountPO.setPrivateKey(KeyUtil.privateKey(accountPO.getPasswordHash(), RandomUtil.getRandomString(32)));
                                        accountPO.setPublicKey(KeyUtil.publicKey(accountPO.getPrivateKey()));
                                        //集团管理员数据迁移
                                        CompletableFuture<Void> f21 = CompletableFuture.supplyAsync(() -> accountMapper.insert(accountPO))
                                                .thenAccept(result -> {
                                                            if (result == 0) {
                                                                log.info("插入集团管理员{}失败", accountPO);
                                                                throw new RuntimeException("插入集团管理员失败");
                                                            }
                                                        }
                                                );

                                        //添加集团通用积分数量
                                        CompletableFuture<Integer> f22 = CompletableFuture.supplyAsync(() -> {
                                            GeneralPointRelationPO generalPointRelationPO = new GeneralPointRelationPO();
                                            generalPointRelationPO.setNum(BigDecimal.ZERO);
                                            generalPointRelationPO.setPlatformId(accountPO.getPId());
                                            generalPointRelationPO.setUserId(accountPO.getId());
                                            return generalPointRelationMapper.insertSelective(generalPointRelationPO);
                                        });
                                        //集团上链
                                        CompletableFuture<Void> f23 = CompletableFuture.supplyAsync(() -> accountDao.getUserKeyById(accountPO.getPId())).thenAccept(platformKey -> {
                                            ResponseBean<String> responseBean = platformBC.createGroup(companyInfoPO.getId(), accountPO.getPId(), companyInfoPO.getCashRate().toString(), companyInfoPO.getPointRate().toString(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                            ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
                                            if (queryTransaction == null) {
                                                log.info("上链失败");
                                                throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                                            }
                                            if (StringUtil.isNotBlank(queryTransaction.getError())) {
                                                log.info("上链失败");
                                                throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
                                            }
                                            log.info("上链成功");
                                            Long height = queryTransaction.getResult().getHeight();
                                            String hash = responseBean.getResult();
                                            BlockInfoPO blockInfoPO = new BlockInfoPO(accountPO.getPId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);
                                            blockInfoDao.insertBlockInfo(blockInfoPO);
                                        });
                                        CompletableFuture.allOf(f21, f22, f23).join();


                                    }
                            );
                            CompletableFuture.allOf(f1, f2).join();
                        }));
        f.join();

    }

}
