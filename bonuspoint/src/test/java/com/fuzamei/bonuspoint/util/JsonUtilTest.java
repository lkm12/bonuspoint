package com.fuzamei.bonuspoint.util;

import com.alibaba.fastjson.JSON;
import com.fuzamei.common.util.JsonUtil;
import com.fzm.blockchain.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: bonus-point-cloud
 * @description: JsonUtil测试
 * @author: WangJie
 * @create: 2018-04-28 17:48
 **/
@Slf4j
public class JsonUtilTest {
    @Test
    public void testJson()throws  Exception{
        Map<String,Object> map = new HashMap<String,Object>(1);

        map.put("companyLeaderMobile","  ");
        map.put("integer",34);
        map.put("companyTelephone","    ");
        String jsonString = JSON.toJSONString(map);
        log.info("old jsonString:"+jsonString);
        log.info("new jsonString:"+ JsonUtil.deleteBlankString(jsonString));
    }
    @Test
    public void testJsonNumberToStr()throws  Exception{
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("integer",34);
        map.put("float",12.5);
        map.put("str","test</p ><p class=\"ql-align-center\">< img src=\"http://120.27.240.81:9099/test1/M00/00/5D/wKgCt1tjxeaAU6sfAAEejEJo86E903.png\"></p ><p class=\"ql-align-center\">2018/8/3");
        map.put("boolean",String.valueOf(true));
        String jsonString = JSON.toJSONString(map);
        log.info(JsonUtil.jsonNumberToString(jsonString));
    }
    @Test
    public void testTimeUtil(){
        log.info(""+TimeUtil.timestamp());
    }

    @Test
    public void testMd5()throws  Exception{
        String p = "123456";
        p = MD5HashUtil.md5SaltEncrypt(""+p,"123456");
        log.info(p);

    }
    @Test
    public void  getKey()throws  Exception{
        String privateKey ="90b289fda1fb0439158f837bbe60cc1ec99616dd0bc6335d6tt0bf3d22888e20";
        String publicKey = KeyUtil.publicKey(privateKey);
        System.out.println(publicKey);
        log.info(publicKey);
    }

}
