package com.fuzamei.bonuspoint.util;

/**
 * CharSequenceUtils
 *
 * @author wangtao
 * @create 2018/6/4
 */

public class CharSequenceUtils {

    private static final int NOT_FOUND = -1;

    public CharSequenceUtils() {
        super();
    }

    static int indexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
        }
        //supplementary characters (LANG1300)
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            for (int i = start; i < sz - 1; i++) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (high == chars[0] && low == chars[1]) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }

}
