package com.mzp.libreads.common.log;

import com.mzp.libreads.common.constant.UrlUtil;

public class OperLog {

	private OperLog() {}
	
	/**
	 * Info.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * 
	 */
	public static void info(String tag, String msg) {
		if (UrlUtil.isPrintLog) {
			info(tag, msg, null);
		}
	}

	/**
	 * Info.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * @param e
	 *            the e
	 * 
	 */
	public static void info(String tag, String msg, Exception e) {
		if (UrlUtil.isPrintLog) {
			android.util.Log.i(tag, msg, e);
		}
	}

	/**
	 * Warning.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * 
	 */
	public static void warning(String tag, String msg) {
		if (UrlUtil.isPrintLog) {
			warning(tag, msg, null);
		}
	}

	/**
	 * Warning.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * @param e
	 *            the e
	 * 
	 */
	public static void warning(String tag, String msg, Exception e) {
		if (UrlUtil.isPrintLog) {
			android.util.Log.w(tag, msg, e);
		}
	}

	/**
	 * Debug.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * 
	 */
	public static void debug(String tag, String msg) {
		if (UrlUtil.isPrintLog) {
			debug(tag, msg, null);
		}
	}

	/**
	 * Debug.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * @param e
	 *            the e
	 * 
	 */
	public static void debug(String tag, String msg, Exception e) {
		if (UrlUtil.isPrintLog) {
			android.util.Log.d(tag, msg, e);
		}
	}

	/**
	 * Error.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * 
	 */
	public static void error(String tag, String msg) {
		if (UrlUtil.isPrintLog) {
			error(tag, msg, null);
		}
	}

	/**
	 * Error.
	 * 
	 * @param tag
	 *            the tag
	 * @param msg
	 *            the msg
	 * @param e
	 *            the e
	 * 
	 */
	public static void error(String tag, String msg, Exception e) {
		if (UrlUtil.isPrintLog) {
			android.util.Log.e(tag, msg, e);
		}
	}
}
