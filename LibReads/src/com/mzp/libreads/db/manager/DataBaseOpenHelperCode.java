package com.mzp.libreads.db.manager;
public class DataBaseOpenHelperCode {

	private DataBaseOpenHelperCode() {}
	
	public static interface TableCode{
		
		/**用户表*/
		String USERINFO = "userinfo";
	}
	
	/**用户信息表*/
	public static interface UserInfo{
		
		String USERNAME ="username";
		
		String ACCOUNT = "account";
		
		String PHONE ="phone";
		
		String EMAIL ="email";
		
		String TOKEN = "token";
		
		String PASSWORD ="password";
		
		String LASTTIME ="lasttime";
	}

}
