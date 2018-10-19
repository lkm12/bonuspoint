package com.fuzamei.bonuspoint.util;

public final class StringUtil {

	private StringUtil() {
		throw new AssertionError("不能实例化 StringUtil");
	}

	// Empty checks
	//-----------------------------------------------------------------------

	/**
	 * 检查CharSequence是否为empty ("")或null
	 *
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty("")        = true
	 * StringUtil.isEmpty(" ")       = false
	 * StringUtil.isEmpty("bob")     = false
	 * StringUtil.isEmpty("  bob  ") = false
	 *
	 * @param cs 待测字符序列
	 * @return 待测序列是否为empty或null
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 检查CharSequence是否不为empty ("")或null
	 *
	 * StringUtil.isNotEmpty(null)      = false
	 * StringUtil.isNotEmpty("")        = false
	 * StringUtil.isNotEmpty(" ")       = true
	 * StringUtil.isNotEmpty("bob")     = true
	 * StringUtil.isNotEmpty("  bob  ") = true
	 *
	 * @param cs 待测字符序列
	 * @return 待测序列是否不为empty或null
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * 检测CharSequence中是否有任意一个为empty ("")或null
	 *
	 * StringUtil.isAnyEmpty(null)             = true
	 * StringUtil.isAnyEmpty(null, "foo")      = true
	 * StringUtil.isAnyEmpty("", "bar")        = true
	 * StringUtil.isAnyEmpty("bob", "")        = true
	 * StringUtil.isAnyEmpty("  bob  ", null)  = true
	 * StringUtil.isAnyEmpty(" ", "bar")       = false
	 * StringUtil.isAnyEmpty("foo", "bar")     = false
	 *
	 * @param css 待测字符序列
	 * @return 待测序列是否有任意一个为empty或null
	 */
	public static boolean isAnyEmpty(final CharSequence... css) {
		if (ArrayUtil.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css){
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测CharSequence中是否都不为empty ("")或null
	 *
	 * StringUtil.isNoneEmpty(null)             = false
	 * StringUtil.isNoneEmpty(null, "foo")      = false
	 * StringUtil.isNoneEmpty("", "bar")        = false
	 * StringUtil.isNoneEmpty("bob", "")        = false
	 * StringUtil.isNoneEmpty("  bob  ", null)  = false
	 * StringUtil.isNoneEmpty(" ", "bar")       = true
	 * StringUtil.isNoneEmpty("foo", "bar")     = true
	 *
	 * @param css 待测字符序列
	 * @return 待测序列是否都不为empty或null
	 */
	public static boolean isNoneEmpty(final CharSequence... css) {
		return !isAnyEmpty(css);
	}

	/**
	 * 检测CharSequence中是否为whitespace空字符或empty ("")或null
	 *
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank("")        = true
	 * StringUtil.isBlank(" ")       = true
	 * StringUtil.isBlank("bob")     = false
	 * StringUtil.isBlank("  bob  ") = false
	 *
	 * @param cs 待测字符序列
	 * @return 待测序列是否为whitespace空字符或empty或null
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检测CharSequence中是否不为whitespace空字符或empty ("")或null
	 *
	 * StringUtil.isNotBlank(null)      = false
	 * StringUtil.isNotBlank("")        = false
	 * StringUtil.isNotBlank(" ")       = false
	 * StringUtil.isNotBlank("bob")     = true
	 * StringUtil.isNotBlank("  bob  ") = true
	 *
	 * @param cs 待测字符序列
	 * @return 待测序列是否不为whitespace空字符或empty或null
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 检测CharSequence中是否有任意一个为whitespace空字符或empty ("")或null
	 *
	 * StringUtil.isAnyBlank(null)             = true
	 * StringUtil.isAnyBlank(null, "foo")      = true
	 * StringUtil.isAnyBlank(null, null)       = true
	 * StringUtil.isAnyBlank("", "bar")        = true
	 * StringUtil.isAnyBlank("bob", "")        = true
	 * StringUtil.isAnyBlank("  bob  ", null)  = true
	 * StringUtil.isAnyBlank(" ", "bar")       = true
	 * StringUtil.isAnyBlank("foo", "bar")     = false
	 *
	 * @param css 待测字符序列
	 * @return 待测序列是否有任意一个为whitespace空字符或empty或null
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		if (ArrayUtil.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css){
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测CharSequence中是否都不为whitespace空字符或empty ("")或null
	 *
	 * StringUtil.isNoneBlank(null)             = false
	 * StringUtil.isNoneBlank(null, "foo")      = false
	 * StringUtil.isNoneBlank(null, null)       = false
	 * StringUtil.isNoneBlank("", "bar")        = false
	 * StringUtil.isNoneBlank("bob", "")        = false
	 * StringUtil.isNoneBlank("  bob  ", null)  = false
	 * StringUtil.isNoneBlank(" ", "bar")       = false
	 * StringUtil.isNoneBlank("foo", "bar")     = true
	 *
	 * @param css 待测字符序列
	 * @return 待测序列是否都不为whitespace空字符或empty或null
	 */
	public static boolean isNoneBlank(final CharSequence... css) {
		return !isAnyBlank(css);
	}


	/**
	 * <p>Checks if CharSequence contains a search CharSequence, handling {@code null}.
	 * This method uses {@link String#indexOf(String)} if possible.</p>
	 *
	 * <p>A {@code null} CharSequence will return {@code false}.</p>
	 *
	 * <pre>
	 * StringUtils.contains(null, *)     = false
	 * StringUtils.contains(*, null)     = false
	 * StringUtils.contains("", "")      = true
	 * StringUtils.contains("abc", "")   = true
	 * StringUtils.contains("abc", "a")  = true
	 * StringUtils.contains("abc", "z")  = false
	 * </pre>
	 *
	 * @param seq  the CharSequence to check, may be null
	 * @param searchSeq  the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence,
	 *  false if not or {@code null} string input
	 * @since 2.0
	 * @since 3.0 Changed signature from contains(String, String) to contains(CharSequence, CharSequence)
	 */
	public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
		if (seq == null || searchSeq == null) {
			return false;
		}
		return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
	}

}
