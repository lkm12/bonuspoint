package com.fuzamei.bonuspoint.redis;

import com.fuzamei.bonuspoint.service.good.GoodService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @program: bonus-point-cloud
 * @description: redis测试
 * @author: WangJie
 * @create: 2018-05-02 15:28
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private GoodService goodService;

    @Test
    public void stringTest(){
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hello", "redis");
        System.out.println("useRedisDao = " + valueOperations.get("hello"));
    }
    @Test
    public void cacheTest(){
        goodService.getCompanyGoodsInfo(5L);
        goodService.getCompanyGoodsInfo(5L);
        goodService.getCompanyGoodsInfo(4L);
        goodService.getCompanyGoodsInfo(4L);
    }

}
