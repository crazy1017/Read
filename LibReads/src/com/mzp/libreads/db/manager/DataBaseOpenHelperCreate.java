package com.mzp.libreads.db.manager;

import android.database.sqlite.SQLiteDatabase;

/**
 * db 创建,建表,建索引类 
 * 使用单一变量模式进行
 * @author may
 */
public class DataBaseOpenHelperCreate {

	private DataBaseOpenHelperCreate(){}
	
	private static DataBaseOpenHelperCreate baseOpenHelperCreate;
	
	public static DataBaseOpenHelperCreate getInstence(){
		if (baseOpenHelperCreate == null) {
			synchronized (DataBaseOpenHelperCreate.class) {
				if (baseOpenHelperCreate == null) {
					baseOpenHelperCreate = new DataBaseOpenHelperCreate();
				}
			}
		}
		return baseOpenHelperCreate;
	}
	
	public void createUser(SQLiteDatabase db){
		/**用户信息表*/
		String user ="create table userinfo (_id integer primary key autoincrement,"
				+ DataBaseOpenHelperCode.UserInfo.USERNAME+" varchar(32),"
				+ DataBaseOpenHelperCode.UserInfo.ACCOUNT+" varchar(32),"
				+ DataBaseOpenHelperCode.UserInfo.EMAIL+" varchar(32),"
				+ DataBaseOpenHelperCode.UserInfo.PHONE+" varchar(20),"
				+ DataBaseOpenHelperCode.UserInfo.PASSWORD+" varchar(20),"
				+ DataBaseOpenHelperCode.UserInfo.LASTTIME+" varchar(32),"
				+ DataBaseOpenHelperCode.UserInfo.TOKEN+" varchar(32)"
				+ ")";
		db.execSQL(user);
	}
}
