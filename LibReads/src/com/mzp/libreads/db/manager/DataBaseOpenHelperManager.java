package com.mzp.libreads.db.manager;

import java.util.concurrent.atomic.AtomicInteger;

import com.mzp.libreads.common.AppCache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * db 管理类 ,获取单一实例
 * @author may
 */
public class DataBaseOpenHelperManager {

	private static DataBaseOpenHelperManager mBaseOpenHelperManager;
	
	/**线程安全*/
	private AtomicInteger mAtomicInteger = new AtomicInteger(0);
	
	private SQLiteDatabase writableDatabase = null;
	
	private DataBaseOpenHelper baseOpenHelper;
	
	private DataBaseOpenHelperManager(Context mContext,String name) {
		baseOpenHelper = new DataBaseOpenHelper(mContext, name);
	}
	
	public static DataBaseOpenHelperManager getInstance(String name) {
		if (mBaseOpenHelperManager == null) {
			synchronized (DataBaseOpenHelperManager.class) {
				if (mBaseOpenHelperManager == null) {
//					mBaseOpenHelperManager = new DataBaseOpenHelperManager();
					return new DataBaseOpenHelperManager(AppCache.getContext(),name);
				}
			}
		}
		return mBaseOpenHelperManager;
	}
	
	public synchronized SQLiteDatabase getDataBase(){
		if (baseOpenHelper == null) {
			throw new IllegalStateException(DataBaseOpenHelperManager.class.getSimpleName() +  
                    " is not initialized, call initBaseOpenHelper(..) method first.");  
		}
		if (mAtomicInteger.incrementAndGet() == 1) {
			writableDatabase = baseOpenHelper.getWritableDatabase();
		}
		
		return writableDatabase;
	}
	
	public synchronized void closeDataBase(){
		if (writableDatabase != null && mAtomicInteger.decrementAndGet() == 0) {
			writableDatabase.close();
		}
	}
}
