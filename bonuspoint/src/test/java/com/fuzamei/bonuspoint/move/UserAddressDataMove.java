package com.fuzamei.bonuspoint.move;

import com.fuzamei.bonuspoint.dao.OldUserAddressDao;
import com.fuzamei.bonuspoint.dao.common.mapper.UserAddressMapper;
import com.fuzamei.bonuspoint.entity.po.user.UserAddressPO;
import com.fuzamei.bonuspoint.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-17 14:47
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserAddressDataMove {
    @Autowired
    OldUserAddressDao oldUserAddressDao;
    @Autowired
    UserAddressMapper userAddressMapper;

    @Test
    public void addressMove() {
        log.info("收货地址迁移开始");
        CompletableFuture<List<UserAddressPO>> future = CompletableFuture.supplyAsync(() -> oldUserAddressDao.getAllAddress());
        CompletableFuture<Void> t = future.thenApply(Collection::stream)
                .thenAccept(userAddressPOStream -> userAddressPOStream.forEach(
                        userAddressPO -> {
                            log.info("正在迁移收货地址{}",userAddressPO);
                            userAddressMapper.insert(userAddressPO);

                        })
                );
        t.join();

    }
    @Test
    public void testDB(){
        oldUserAddressDao.getAllAddress();
        log.info(oldUserAddressDao.getAllAddress().size()+"");
        log.error("test");
    }

}
