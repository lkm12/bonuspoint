package com.fuzamei.bonuspoint.util;


/**
 * 加密工具类
 *
 * @author wangtao
 * @create 2018/5/29
 */

public class BlowfishUtil {

    private BlowfishUtil() {
        throw new AssertionError("不能实例化 BlowfishUtil");
    }

    /**
     * 加密
     *
     * @param sourceString 待加密
     * @param encryptKey 加密密钥
     * @return
     */
    public static String encryptString(String sourceString, String encryptKey){
        Blowfish blowfish = new Blowfish(encryptKey);
        return blowfish.encryptString(sourceString);
    }

    /**
     * 解密
     *
     * @param signString 待解密
     * @param decryptKey 解密密钥
     * @return
     */
    public static String decryptString(String signString, String decryptKey){
        Blowfish blowfish = new Blowfish(decryptKey);
        return blowfish.decryptString(signString);
    }

}
