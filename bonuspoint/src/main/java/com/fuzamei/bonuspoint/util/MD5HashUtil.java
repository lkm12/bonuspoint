/**
 * FileName: MD5HashUtil
 * Author: wangtao
 * Date: 2018/3/26 17:46
 * Description:
 */
package com.fuzamei.bonuspoint.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 *
 * @author wangtao
 * @create 2018/3/26
 *
 */
@Slf4j
public class MD5HashUtil {

    private MD5HashUtil() {
        throw new AssertionError("不能实例化 MD5HashUtil");
    }

    /**
     * md5加密
     *
     * @param message 待加密信息
     * @return 加密结果
     * @throws Exception
     */
    public static String md5Encrypt(String message)  {
        return md5SaltEncrypt(message, "");
    }

    /**
     * md5加盐加密
     *
     * @param message 待加密信息
     * @param salt 盐
     * @return 加密结果
     * @throws Exception
     */
    public static String md5SaltEncrypt(String message, String salt) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getLocalizedMessage());
        }
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        md.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] md5Byte = md.digest();
        return HexUtil.bytesToHex(md5Byte);
    }

}
