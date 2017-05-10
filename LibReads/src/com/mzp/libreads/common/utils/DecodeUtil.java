package com.mzp.libreads.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import android.text.TextUtils;

public class DecodeUtil {
	
	public static String StrEncodeOrDecode(String str, boolean flag){
		String content = "";
		if(TextUtils.isEmpty(str)){
			content = str;
		}else{
			try {
				if(flag){
					content = URLEncoder.encode(str, "utf-8");
				}else{
					content = URLDecoder.decode(str, "UTF-8");
				}
			} catch (Exception e) {
				e.printStackTrace();
				content = str;
			}
		}
		return content;
	}

}
