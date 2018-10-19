package com.fzm.blockchain.util;

import java.nio.charset.StandardCharsets;

/**
 * @author wangtao
 * @create 2018/6/15
 */

public class HexUtil {

    /**
     * 16进制转字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        if ( bytes == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * bytes.length);
        for (final byte b : bytes) {
            hex.append(Character.forDigit((b & 0xF0) >> 4, 16))
                    .append(Character.forDigit((b & 0x0F), 16));
        }
        return hex.toString();
    }

    /**
     * 16进制转字符串
     *
     * @param hex
     * @return
     */
    public static String hexToStr(String hex){
        return new String(hexToBytes(hex), StandardCharsets.UTF_8);
    }

    /**
     * 字符串转16进制
     *
     * @param str
     * @return
     */
    public static String strToHex(String str){
        return bytesToHex(str.getBytes(StandardCharsets.UTF_8));
    }


}
