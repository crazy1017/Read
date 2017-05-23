package com.mzp.libreads.common.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerUtil {

	/** 数据库储存时间格式 年到秒. */
    public static final String DB_DATE_FORMAT_YEAR2SECOND = "yyyy-MM-dd HH:mm:ss";

    /** 显示时间格式 年到秒. */
    public static final String DATE_FORMAT_YEAR2SECOND = "MM-dd-yyyy HH:mm:ss";

    /** 月日年时分. */
    public static final String DATE_FORMAT_MONTH2MINUTES = "MM-dd-yyyy HH:mm";

    /** 显示时间格式 年到日. */
    public static final String DATE_FORMAT_YEAR2DAY = "MM-dd-yyyy";

    /** The Constant DATE_FORMAT_YYYYMMDD. */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";

    /** 显示时间格式 星期，年到日. */
    public static final String DATE_FORMAT_WEEK_YEAR2DAY = "EEE, MM-dd-yyyy";

    /** 显示时间格式 小时到秒. */
    public static final String DATE_FORMAT_HOUR2SECONDE = "HH:mm:ss";

    /** 显示时间格式 小时到分. */
    public static final String DATE_FORMAT_HOUR2MINUTE = "HH:mm";

    /** The Constant DATE_FORMAT_DDMMYYYY. */
    public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";

	private TimerUtil() {
	}
	
	public static String getCurrentTime(){
		Date curDate = new Date();
		Format formator = new SimpleDateFormat(DB_DATE_FORMAT_YEAR2SECOND, Locale.getDefault());
		String date = formator.format(curDate); 
		return date;
	}
	
	/**
	 * 根据指定格式获取当前时间
	 * @param format
	 * @return
	 */
	public static String getCurrentTime(String format){
		Date curDate = new Date();
		Format formator = new SimpleDateFormat(format, Locale.getDefault());
		String date = formator.format(curDate); 
		return date;
	}
	
	/**
	 * 将指定格式的日期获取当前时间对应的long值
	 * @param time
	 * @param format
	 * @return
	 */
	public static long getTimeToData(String time,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date dt = null;
		try {
			dt = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dt!= null) {
			return dt.getTime();
		}
		return 0;
	}
}
