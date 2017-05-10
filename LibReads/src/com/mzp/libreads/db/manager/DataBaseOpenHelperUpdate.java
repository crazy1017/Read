package com.mzp.libreads.db.manager;

import android.database.sqlite.SQLiteDatabase;

/**
 * db升级管理类,采用单例设计模式
 * @author may
 *
 */
public class DataBaseOpenHelperUpdate {

	private DataBaseOpenHelperUpdate (){}
	
	private static DataBaseOpenHelperUpdate baseOpenHelperUpdate;
	
	public static DataBaseOpenHelperUpdate getInstence(){
		if (baseOpenHelperUpdate == null) {
			synchronized (DataBaseOpenHelperUpdate.class) {
				if (baseOpenHelperUpdate == null) {
					baseOpenHelperUpdate = new DataBaseOpenHelperUpdate();
				}
			}
		}
		return baseOpenHelperUpdate;
	}
	
	/**
	 * 更新db结构
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 * @param db db引用
	 */
	public void UpdateDataBase(int oldVersion,int newVersion, SQLiteDatabase db){
		
	}
}
