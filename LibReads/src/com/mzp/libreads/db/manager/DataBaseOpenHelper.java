package com.mzp.libreads.db.manager;

import com.mzp.libreads.common.log.OperLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = DataBaseOpenHelper.class.getName();
	
	private DataBaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	private DataBaseOpenHelper(Context context, String name,int version) {
		this(context, name, null,version);
	}

	public DataBaseOpenHelper(Context context, String name) {
		this(context, name,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		OperLog.error(TAG+" : onCreate", "create database for libreads");
		/**创建用户表*/
		DataBaseOpenHelperCreate.getInstence().createUser(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		OperLog.error(TAG+" : onUpgrade", "database for libreads is onUpgrade , the oldVersion is "+oldVersion+" and the newVersion is "+newVersion);
	}

}
