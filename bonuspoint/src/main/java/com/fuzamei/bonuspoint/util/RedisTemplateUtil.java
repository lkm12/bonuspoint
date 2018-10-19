package com.fuzamei.bonuspoint.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存工具
 *
 * @author wangtao
 * @create 2018/5/29
 */
@Component
public class RedisTemplateUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<Object, Object> valOpsObj;

    /**
     * 设置String缓存
     * @param key
     * @param val
     */
    public void setStr(String key, String val){
        valOpsStr.set(key, val);
    }

    /**
     * 设置String缓存
     * @param key
     * @param val
     * @param time
     */
    public void setStr(String key, String val, long time){
        valOpsStr.set(key, val, time);
    }

    /**
     * 设置String缓存
     * @param key
     * @param val
     * @param time
     * @param timeUnit
     */
    public void setStr(String key, String val, long time, TimeUnit timeUnit){
        valOpsStr.set(key, val, time, timeUnit);
    }

    /**
     * 根据指定key获取String
     * @param key
     * @return
     */
    public String getStr(String key){
        return valOpsStr.get(key);
    }

    /**
     * 删除指定key
     * @param key
     */
    public void delStr(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     */
    public void setObj(Object o1, Object o2){
        valOpsObj.set(o1, o2);
    }

    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     * @param time
     */
    public void setObj(Object o1, Object o2, long time){
        valOpsObj.set(o1, o2, time);
    }

    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     * @param time
     * @param timeUnit
     */
    public void setObj(Object o1, Object o2, long time, TimeUnit timeUnit){
        valOpsObj.set(o1, o2, time, timeUnit);
    }

    /**
     * 根据指定o获取Object
     * @param o
     * @return
     */
    public Object getObj(Object o){
        return valOpsObj.get(o);
    }

    /**
     * 删除Obj缓存
     * @param o
     */
    public void delObj(Object o){
        redisTemplate.delete(o);
    }

}
