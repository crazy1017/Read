package com.mzp.libreads.db.model;

import com.mzp.libreads.common.AppCache;
import com.mzp.libreads.common.log.OperLog;
import com.mzp.libreads.common.utils.EnCodedUtil;
import com.mzp.libreads.common.utils.SharePrefUtil;
import com.mzp.libreads.db.manager.DataBaseOpenHelperCode;
import com.mzp.libreads.db.manager.DataBaseOpenHelperManager;
import com.mzp.libreads.login.info.UserInfo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 用户表db的处理逻辑
 * @author may
 */
public class DataBaseUserInfo {
	
	private DataBaseUserInfo(){}
	
	private static final String TAG = DataBaseUserInfo.class.getSimpleName();
	
	private static DataBaseUserInfo baseUserInfo = new DataBaseUserInfo();
	
	private static String name;
	
	public static DataBaseUserInfo getInstance(){
		name = SharePrefUtil.create(AppCache.getContext()).getString("userinfo", "reading.db");
		return baseUserInfo;
	}
	
	/**
	 * 保存用户信息
	 * @param userInfo
	 */
	public void saveUserInfo(UserInfo userInfo){
		if (userInfo == null) {
			return ;
		}
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			String account = EnCodedUtil.encode(userInfo.account);
			String email = EnCodedUtil.encode(userInfo.email);
			String password = EnCodedUtil.encode(userInfo.password);
			String phone = EnCodedUtil.encode(userInfo.phone);
			String userName = EnCodedUtil.encode(userInfo.userName);
			String token = userInfo.token;
			String lastTime = userInfo.lastTime;
			
			ContentValues values = new ContentValues();
			if (TextUtils.isEmpty(password)) {
				return;
			}else {
				values.put(DataBaseOpenHelperCode.UserInfo.PASSWORD, password);
			}
			
			if (!TextUtils.isEmpty(account)) {
				values.put(DataBaseOpenHelperCode.UserInfo.ACCOUNT, account);
			}
			
			if (!TextUtils.isEmpty(email)) {
				values.put(DataBaseOpenHelperCode.UserInfo.EMAIL, email);
			}
			
			if (!TextUtils.isEmpty(phone)) {
				values.put(DataBaseOpenHelperCode.UserInfo.PHONE, phone);
			}
			
			if (!TextUtils.isEmpty(userName)) {
				values.put(DataBaseOpenHelperCode.UserInfo.USERNAME, userName);
			}
			
			if (!TextUtils.isEmpty(token)) {
				values.put(DataBaseOpenHelperCode.UserInfo.TOKEN, token);
			}
			
			if (!TextUtils.isEmpty(lastTime)) {
				values.put(DataBaseOpenHelperCode.UserInfo.LASTTIME, lastTime);
			}
			
			initDataBase.insert(DataBaseOpenHelperCode.TableCode.USERINFO, null, values);
		} catch (Exception e) {
			e.printStackTrace();
			OperLog.error(TAG +" : saveUserInfo()", e.toString());
		}finally {
			closed(initDataBase);
		}
	}
	
	/**
	 * 修改密码
	 * @param info
	 */
	public boolean updateUserPassword(UserInfo info){
		if (info == null || TextUtils.isEmpty(info.account) || TextUtils.isEmpty(info.password)) {
			OperLog.error(TAG, "在逗我玩呢，出入的参数有问题，请仔细检测");
			return false;
		}
		SQLiteDatabase initDataBase = null;
		try {
			initDataBase = initDataBase();
			ContentValues values = new ContentValues();
			values.put(DataBaseOpenHelperCode.UserInfo.PASSWORD, info.password);
			initDataBase.update(DataBaseOpenHelperCode.TableCode.USERINFO, values, "account =?", new String[]{info.account});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			closed(initDataBase);
		}
	}
	
	/**
	 * 获取db引用
	 * @return
	 */
	private SQLiteDatabase initDataBase(){
		return DataBaseOpenHelperManager.getInstance(name).getDataBase();
	}
	
	/**
	 * 关闭db
	 * @param database
	 */
	private void closed(SQLiteDatabase database){
		DataBaseOpenHelperManager.getInstance(name).closeDataBase();
	}
}
