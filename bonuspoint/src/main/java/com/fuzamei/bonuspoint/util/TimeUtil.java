package com.fuzamei.bonuspoint.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 时间相关方法
 * @author wangtao
 * @time 2017年11月16日
 *
 */
public final class TimeUtil {

	private TimeUtil(){
		throw new AssertionError("不能实例化 TimeUtil");
	}
	/**
	 * 获取当前时间戳
	 * @return
	 */
	public static long timestamp(){
		return System.currentTimeMillis();
	}
	/**
	 * 获取当前时间，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getFormatTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(Calendar.getInstance().getTime());
	}
	/**
	 * 获取当前时间，格式：yyyyMMddHHmmss
	 * @return
	 */
	public static String get14FormatTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(Calendar.getInstance().getTime());
	}
	/**
	 * 获取当前时间，格式：yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String get17FormatTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(Calendar.getInstance().getTime());
	}

	/**
	 * 转换时间，格式：yyyy-MM-dd HH:mm:ss
	 * @param timeMillis 时间戳
	 * @return
	 */
	public static String transformTimeMillisToFormatTime(long timeMillis){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(timeMillis));
	}

}
