package com.fuzamei.bonuspoint.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机数相关方法
 * @author wangtao
 * @time 2017年11月16日
 *
 */
public final class RandomUtil {

	private RandomUtil(){
		throw new AssertionError("不能实例化 RandomUtil");
	}
	
	private static final Random RANDOM = new Random();
	
	/**
	 * 获取指定长度的随机数
	 * @param length 位数 长度
	 * @return
	 */
	public static String getRandomNumber(int length) {
		String string;
		for (string = String.valueOf(Math.abs(RANDOM.nextLong())); string.length() < length; 
				string = string + String.valueOf(Math.abs(RANDOM.nextLong()))) {
			;
		}
		return string.substring(0, length);
	}
	/**
	 * 获取指定长度的字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			sb.append(str.charAt(RANDOM.nextInt(str.length())));
		}
		return sb.toString();
	}

	/**
	 *
	 * @return
	 */
	public static Long getSecureRandom(){
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextLong();
	}

}
