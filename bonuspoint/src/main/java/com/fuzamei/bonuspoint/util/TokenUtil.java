package com.fuzamei.bonuspoint.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuzamei.bonuspoint.constant.CacheHeader;
import com.fuzamei.bonuspoint.entity.Token;

/**
 * token工具类
 *
 * @author wangtao
 * @create 2018/5/29
 */

public class TokenUtil {

    private TokenUtil() {
        throw new AssertionError("不能实例化 TokenUtil");
    }

    /**
     * 获取加密的token字符串
     * @param token token类
     * @param encryptKey 加密密钥
     * @return 加密的token字符串
     */
    public static String getTokenString(Token token, String encryptKey){
        return BlowfishUtil.encryptString(JSON.toJSONString(token), encryptKey);
    }

    /**
     * 获取token类（注意接收异常）
     * @param tokenString 加密的token字符串
     * @param decryptKey 解密密钥
     * @return token类
     */
    public static Token getTokenObject(String tokenString, String decryptKey){
        String tokenJsonString = BlowfishUtil.decryptString(tokenString, decryptKey);
        return JSONObject.parseObject(tokenJsonString, Token.class);
    }

    /**
     * 是否在有效期（注意接收异常）
     * @param tokenString
     * @param decryptKey
     * @param redisTemplateUtil
     * @return
     */
    public static Boolean isActiveToken(String tokenString, String decryptKey, RedisTemplateUtil redisTemplateUtil){
        String tokenJsonString = BlowfishUtil.decryptString(tokenString, decryptKey);
        return isActiveToken(JSONObject.parseObject(tokenJsonString, Token.class), redisTemplateUtil);
    }

    /**
     * 是否在有效期（注意接收异常）
     * @param token
     * @param redisTemplateUtil
     * @return
     */
    public static Boolean isActiveToken(Token token, RedisTemplateUtil redisTemplateUtil){
        if(StringUtil.isBlank(token.getTokenStr())){
            return false;
        }
        String tokenStr = redisTemplateUtil.getStr(CacheHeader.TOKEN + token.getUid());
        return token.getTokenStr().equals(tokenStr);
    }
}
