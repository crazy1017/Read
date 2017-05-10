package com.mzp.libreads.common.utils;

import com.mzp.libreads.common.AppCache;

import android.widget.Toast;

public class ToastUtil {

	private ToastUtil(){}
	
	private static Toast mToast;
	
	private static void showToastUtil(String text,int duration){
		if (mToast == null) {
			mToast = Toast.makeText(AppCache.getContext(), text, 0);
		}
		mToast.setDuration(duration);
		mToast.setText(text);
		mToast.show();
	}
	
	public static void showToast(String text,int duration){
		showToastUtil(text,duration);
	}
	
	public static void showToast(String text){
		showToastUtil(text,Toast.LENGTH_SHORT);
	}
}
