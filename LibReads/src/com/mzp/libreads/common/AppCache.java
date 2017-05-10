package com.mzp.libreads.common;

import android.content.Context;

public class AppCache {
	private static Context context;
	
    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppCache.context = context.getApplicationContext();
    }
}
