package com.mzp.libreads.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.text.TextUtils;

/**
 * 对字符进行编码,解码
 * @author may
 */
public class EnCodedUtil {

	private EnCodedUtil() {}

	/**解码*/
	public static String decode(String str){
		if(TextUtils.isEmpty(str)){
			return str;
		} else{
			try {
				return URLDecoder.decode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return str;
			}
		}
	}
	
	/**编码*/
	public static String encode(String str){
		if(TextUtils.isEmpty(str)){
			return str;
		}else{
			try {
				return URLEncoder.encode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return str;
			}
		}
	}
}
