/**
 * FileName: ArrayUtil
 * Author: wangtao
 * Date: 2017/12/15 11:28
 * Description:
 */
package com.fuzamei.bonuspoint.util;

/**
 *
 *
 * @author wangtao
 * @create 2017/12/15
 *
 */

public class ArrayUtil {

    private ArrayUtil() {
        throw new AssertionError("不能实例化 ArrayUtil");
    }

    // Empty checks
    //-----------------------------------------------------------------------

    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final double[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(final boolean[] array) {
        return array == null || array.length == 0;
    }

}
