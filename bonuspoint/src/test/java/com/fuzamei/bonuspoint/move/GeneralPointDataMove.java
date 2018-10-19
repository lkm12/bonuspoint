package com.fuzamei.bonuspoint.move;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.dao.OldGeneralPointRecordDao;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.GeneralPointRecordMapper;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRecordDao;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRelationDao;
import com.fuzamei.bonuspoint.entity.Point;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.ThreadPoolUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @program: bonus-point-cloud
 * @description: 通用积分数据迁移
 * @author: WangJie
 * @create: 2018-08-17 15:22
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GeneralPointDataMove {
    @Autowired
    private OldGeneralPointRecordDao oldGeneralPointRecordDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PlatformBC platformBC;
    @Autowired
    private BlockInfoDao blockInfoDao;
    @Autowired
    private BlockChainUtil blockChainUtil;
    @Autowired
    private GeneralPointRecordMapper generalPointRecordMapper;
    @Autowired
    private GeneralPointRelationDao generalPointRelationDao;
    @Autowired
    private GeneralPointRecordDao generalPointRecordDao;

    @Test
    //用时86037ms
    public void sendGeneralPoint() {
        Supplier<Void> supplier = () -> {
            CompletableFuture<List<Point>> future = CompletableFuture.supplyAsync(() -> oldGeneralPointRecordDao.getAllUserHasGeneralPoint());

            CompletableFuture<Void> f = future.thenApply(Collection::stream).thenAcceptBoth(
                    CompletableFuture.supplyAsync(() -> accountDao.getUserKeyById(1L))
                    , (pointStream, platformKey) -> pointStream.parallel().forEach(
                            point -> {
                                ResponseBean<String> responseBean;
                                if (point.getCompanyId() != null) {
                                    responseBean = platformBC.giveMerchantGPoints(point.getCompanyId(), point.getNumNew(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                } else {
                                    responseBean = platformBC.sendGeneralPointToMember(platformKey.getId(), 1L, point.getNumNew(), point.getUid(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                }
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
                                BlockInfoPO blockInfoPO = new BlockInfoPO(platformKey.getId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);

                                //记录区块信息
                                CompletableFuture<Void> f1 = CompletableFuture.supplyAsync(() -> blockInfoDao.insertBlockInfo(blockInfoPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("上链信息写入数据库失败");
                                            }
                                        });

                                //插入发起方减少记录
                                GeneralPointRecordPO sendGeneralPointRecordPO = new GeneralPointRecordPO();
                                sendGeneralPointRecordPO.setCreatedAt(System.currentTimeMillis());
                                sendGeneralPointRecordPO.setId(null);
                                sendGeneralPointRecordPO.setUid(platformKey.getId());
                                sendGeneralPointRecordPO.setOppositeUid(point.getUid());
                                sendGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
                                sendGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
                                sendGeneralPointRecordPO.setNum(point.getNumNew());
                                sendGeneralPointRecordPO.setHeight(height);
                                sendGeneralPointRecordPO.setHash(hash);
                                CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(sendGeneralPointRecordPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分发放记录写入数据库失败");
                                            }
                                        });

                                //插入接收方积分增加记录
                                GeneralPointRecordPO getGeneralPointRecordPO = new GeneralPointRecordPO();
                                BeanUtils.copyProperties(sendGeneralPointRecordPO, getGeneralPointRecordPO);
                                getGeneralPointRecordPO.setId(null);
                                getGeneralPointRecordPO.setUid(point.getUid());
                                getGeneralPointRecordPO.setOppositeUid(platformKey.getId());
                                getGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
                                getGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_GRANT_IN);
                                getGeneralPointRecordPO.setNum(point.getNumNew());
                                getGeneralPointRecordPO.setHeight(height);
                                getGeneralPointRecordPO.setHash(hash);
                                CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(getGeneralPointRecordPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                                            }
                                        });
                                //用户通用积分数量增加
                                boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(point.getUid(),1L);
                                if (!isGeneralRelationExist) {
                                    generalPointRelationDao.createGeneralPointRelation(point.getUid(),1L);
                                }
                                CompletableFuture<Void> f4 = CompletableFuture.supplyAsync(() -> generalPointRelationDao.updateGeneralPointNumByUid(point.getUid(), point.getNumNew()))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                                            }
                                        });
                                CompletableFuture.allOf(f1, f2, f3, f4).join();
                            }

                    )
            );
            f.join();
            return null;
        };
        testTime(supplier);
    }

    @Test
    //用时428036ms
    public void sendGeneralPoint2() {
        Supplier<Void> supplier = () -> {
            List<Point> points = oldGeneralPointRecordDao.getAllUserHasGeneralPoint();
            KeyDTO platformKey = accountDao.getUserKeyById(1L);
            points.stream().forEach(point -> {
                ResponseBean<String> responseBean;
                if (point.getCompanyId() != null) {
                    responseBean = platformBC.giveMerchantGPoints(point.getCompanyId(), point.getNumNew(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                } else {
                    responseBean = platformBC.sendGeneralPointToMember(platformKey.getId(), 1L, point.getNumNew(), point.getUid(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                }
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
                BlockInfoPO blockInfoPO = new BlockInfoPO(platformKey.getId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);

                //记录区块信息
                CompletableFuture<Void> f1 = CompletableFuture.supplyAsync(() -> blockInfoDao.insertBlockInfo(blockInfoPO))
                        .thenAccept(result -> {
                            if (result != 1) {
                                throw new RuntimeException("上链信息写入数据库失败");
                            }
                        });

                //插入发起方减少记录
                GeneralPointRecordPO sendGeneralPointRecordPO = new GeneralPointRecordPO();
                sendGeneralPointRecordPO.setCreatedAt(System.currentTimeMillis());
                sendGeneralPointRecordPO.setId(null);
                sendGeneralPointRecordPO.setUid(platformKey.getId());
                sendGeneralPointRecordPO.setOppositeUid(point.getUid());
                sendGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
                sendGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
                sendGeneralPointRecordPO.setNum(point.getNumNew());
                sendGeneralPointRecordPO.setHeight(height);
                sendGeneralPointRecordPO.setHash(hash);
                CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(sendGeneralPointRecordPO))
                        .thenAccept(result -> {
                            if (result != 1) {
                                throw new RuntimeException("通用积分发放记录写入数据库失败");
                            }
                        });

                //插入接收方积分增加记录
                GeneralPointRecordPO getGeneralPointRecordPO = new GeneralPointRecordPO();
                BeanUtils.copyProperties(sendGeneralPointRecordPO, getGeneralPointRecordPO);
                getGeneralPointRecordPO.setId(null);
                getGeneralPointRecordPO.setUid(point.getUid());
                getGeneralPointRecordPO.setOppositeUid(platformKey.getId());
                getGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
                getGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_GRANT_IN);
                getGeneralPointRecordPO.setNum(point.getNumNew());
                getGeneralPointRecordPO.setHeight(height);
                getGeneralPointRecordPO.setHash(hash);
                CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(getGeneralPointRecordPO))
                        .thenAccept(result -> {
                            if (result != 1) {
                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                            }
                        });
                //用户通用积分数量增加
                boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(point.getUid(),1L);
                if (!isGeneralRelationExist) {
                    generalPointRelationDao.createGeneralPointRelation(point.getUid(),1L);
                }
                CompletableFuture<Void> f4 = CompletableFuture.supplyAsync(() -> generalPointRelationDao.updateGeneralPointNumByUid(point.getUid(), point.getNumNew()))
                        .thenAccept(result -> {
                            if (result != 1) {
                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                            }
                        });
                CompletableFuture.allOf(f1, f2, f3, f4).join();

            });
            return null;
        };
        testTime(supplier);
    }

    //用时76433ms
    @Test
    public void sendGeneralPoint3() {

        Supplier<Void> supplier = () -> {
            CompletableFuture<List<Point>> future = CompletableFuture.supplyAsync(() -> oldGeneralPointRecordDao.getAllUserHasGeneralPoint());

            CompletableFuture<Void> f = future.thenApply(Collection::stream).thenAcceptBoth(
                   CompletableFuture.supplyAsync(() -> accountDao.getUserKeyById(1L))
                    , (pointStream, platformKey) -> pointStream.parallel().map(
                            point -> {
                                ResponseBean<String> responseBean;
                                if (point.getCompanyId() != null) {
                                    responseBean = platformBC.giveMerchantGPoints(point.getCompanyId(), point.getNumNew(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                } else {
                                    responseBean = platformBC.sendGeneralPointToMember(platformKey.getId(), 1L, point.getNumNew(), point.getUid(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                }
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
                                BlockInfoPO blockInfoPO = new BlockInfoPO(platformKey.getId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);

                                //记录区块信息
                                CompletableFuture<Void> f1 = CompletableFuture.supplyAsync(() -> blockInfoDao.insertBlockInfo(blockInfoPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("上链信息写入数据库失败");
                                            }
                                        });

                                //插入发起方减少记录
                                GeneralPointRecordPO sendGeneralPointRecordPO = new GeneralPointRecordPO();
                                sendGeneralPointRecordPO.setCreatedAt(System.currentTimeMillis());
                                sendGeneralPointRecordPO.setId(null);
                                sendGeneralPointRecordPO.setUid(platformKey.getId());
                                sendGeneralPointRecordPO.setOppositeUid(point.getUid());
                                sendGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
                                sendGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
                                sendGeneralPointRecordPO.setNum(point.getNumNew());
                                sendGeneralPointRecordPO.setHeight(height);
                                sendGeneralPointRecordPO.setHash(hash);
                                CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(sendGeneralPointRecordPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分发放记录写入数据库失败");
                                            }
                                        });

                                //插入接收方积分增加记录
                                GeneralPointRecordPO getGeneralPointRecordPO = new GeneralPointRecordPO();
                                BeanUtils.copyProperties(sendGeneralPointRecordPO, getGeneralPointRecordPO);
                                getGeneralPointRecordPO.setId(null);
                                getGeneralPointRecordPO.setUid(point.getUid());
                                getGeneralPointRecordPO.setOppositeUid(platformKey.getId());
                                getGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
                                getGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_GRANT_IN);
                                getGeneralPointRecordPO.setNum(point.getNumNew());
                                getGeneralPointRecordPO.setHeight(height);
                                getGeneralPointRecordPO.setHash(hash);
                                CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(getGeneralPointRecordPO))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                                            }
                                        });
                                //用户通用积分数量增加
                                boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(point.getUid(),1L);
                                if (!isGeneralRelationExist) {
                                    generalPointRelationDao.createGeneralPointRelation(point.getUid(),1L);
                                }
                                CompletableFuture<Void> f4 = CompletableFuture.supplyAsync(() -> generalPointRelationDao.updateGeneralPointNumByUid(point.getUid(), point.getNumNew()))
                                        .thenAccept(result -> {
                                            if (result != 1) {
                                                throw new RuntimeException("通用积分接收记录写入数据库失败");
                                            }
                                        });
                                return CompletableFuture.allOf(f1, f2, f3, f4).join();

                            }

                    ).collect(Collectors.toList())
            );
            return f.join();

        };
        testTime(supplier);

    }

    //用时25831ms
    @Test
    public void sendGeneralPoint4() {

        Supplier<Void> supplier = () -> {
            CompletableFuture<List<Point>> future = CompletableFuture.supplyAsync(() -> oldGeneralPointRecordDao.getAllUserHasGeneralPoint());

            CompletableFuture<Void> f = future.thenApply(Collection::stream).thenAcceptBoth(
                    CompletableFuture.supplyAsync(() -> accountDao.getUserKeyById(1L))
                    , (pointStream, platformKey) -> {
                        List<CompletableFuture<Void>> futureList = pointStream.parallel().map(
                                point -> CompletableFuture.supplyAsync(() -> {
                                    ResponseBean<String> responseBean;
                                    if (point.getCompanyId() != null) {
                                        responseBean = platformBC.giveMerchantGPoints(point.getCompanyId(), point.getNumNew(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                    } else {
                                        responseBean = platformBC.sendGeneralPointToMember(platformKey.getId(), 1L, point.getNumNew(), point.getUid(), platformKey.getPublicKey(), platformKey.getPrivateKey());
                                    }
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
                                    BlockInfoPO blockInfoPO = new BlockInfoPO(platformKey.getId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreateMerchant_VALUE);

                                    //记录区块信息
                                    CompletableFuture<Void> f1 = CompletableFuture.supplyAsync(() -> blockInfoDao.insertBlockInfo(blockInfoPO))
                                            .thenAccept(result -> {
                                                if (result != 1) {
                                                    throw new RuntimeException("上链信息写入数据库失败");
                                                }
                                            });

                                    //插入发起方减少记录
                                    GeneralPointRecordPO sendGeneralPointRecordPO = new GeneralPointRecordPO();
                                    sendGeneralPointRecordPO.setCreatedAt(System.currentTimeMillis());
                                    sendGeneralPointRecordPO.setId(null);
                                    sendGeneralPointRecordPO.setUid(platformKey.getId());
                                    sendGeneralPointRecordPO.setOppositeUid(point.getUid());
                                    sendGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_SUB);
                                    sendGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
                                    sendGeneralPointRecordPO.setNum(point.getNumNew());
                                    sendGeneralPointRecordPO.setHeight(height);
                                    sendGeneralPointRecordPO.setHash(hash);
                                    CompletableFuture<Void> f2 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(sendGeneralPointRecordPO))
                                            .thenAccept(result -> {
                                                if (result != 1) {
                                                    throw new RuntimeException("通用积分发放记录写入数据库失败");
                                                }
                                            });

                                    //插入接收方积分增加记录
                                    GeneralPointRecordPO getGeneralPointRecordPO = new GeneralPointRecordPO();
                                    BeanUtils.copyProperties(sendGeneralPointRecordPO, getGeneralPointRecordPO);
                                    getGeneralPointRecordPO.setId(null);
                                    getGeneralPointRecordPO.setUid(point.getUid());
                                    getGeneralPointRecordPO.setOppositeUid(platformKey.getId());
                                    getGeneralPointRecordPO.setType(GeneralPointRecordConstant.POINT_ADD);
                                    getGeneralPointRecordPO.setCategory(GeneralPointRecordConstant.CATEGORY_USER_GRANT_IN);
                                    getGeneralPointRecordPO.setNum(point.getNumNew());
                                    getGeneralPointRecordPO.setHeight(height);
                                    getGeneralPointRecordPO.setHash(hash);
                                    CompletableFuture<Void> f3 = CompletableFuture.supplyAsync(() -> generalPointRecordMapper.insertSelective(getGeneralPointRecordPO))
                                            .thenAccept(result -> {
                                                if (result != 1) {
                                                    throw new RuntimeException("通用积分接收记录写入数据库失败");
                                                }
                                            });
                                    //用户通用积分数量增加
                                    boolean isGeneralRelationExist = generalPointRelationDao.isGeneralPointRelationExist(point.getUid(),1L);
                                    if (!isGeneralRelationExist) {
                                        generalPointRelationDao.createGeneralPointRelation(point.getUid(),1L);
                                    }
                                    CompletableFuture<Void> f4 = CompletableFuture.supplyAsync(() -> generalPointRelationDao.updateGeneralPointNumByUid(point.getUid(), point.getNumNew()))
                                            .thenAccept(result -> {
                                                if (result != 1) {
                                                    throw new RuntimeException("通用积分接收记录写入数据库失败");
                                                }
                                            });
                                    return CompletableFuture.allOf(f1, f2, f3, f4).join();
                                }, ThreadPoolUtil.getExecutorService())

                        ).collect(Collectors.toList());
                        futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                    }
            );
            return f.join();

        };
        testTime(supplier);

    }

    public void testTime(Supplier<Void> supplier) {
        long start = System.currentTimeMillis();
        supplier.get();
        long end = System.currentTimeMillis();
        System.out.println("用时：" + (end - start));
    }

}
