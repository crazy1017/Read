package com.mzp.libreads;

import com.mzp.libreads.common.AppCache;

import android.app.Application;

public class CustomApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AppCache.setContext(this);
	}
}
