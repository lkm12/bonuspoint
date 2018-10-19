package com.fuzamei.bonuspoint.util;

/**
 * @author wangtao
 * @create 2018/6/15
 */

public class HexUtil {

    /**
     * 16进制转字节数组
     * @param s
     * @return
     */
    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 字节数组转16进制
     * @param raw
     * @return
     */
    public static String bytesToHex(byte[] raw) {
        if ( raw == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(Character.forDigit((b & 0xF0) >> 4, 16))
                    .append(Character.forDigit((b & 0x0F), 16));
        }
        return hex.toString();
    }

}

