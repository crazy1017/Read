package com.mzp.libreads.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by lxj on 2016/8/18.
 */
public class SharePrefUtil {

	private String name = "RCSpwd";
	private SharedPreferences sp;
	private SharedPreferences dynamicSP;

	private static SharePrefUtil mInstance = null;

	private SharePrefUtil(Context context) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static SharePrefUtil create(Context context) {
		if (mInstance == null) {
			mInstance = new SharePrefUtil(context);
		}
		return mInstance;
	}

	public void saveInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void saveBoolean(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void saveLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public void saveFloat(String key, float value) {
		sp.edit().putFloat(key, value).commit();
	}

	public float getFloat(String key, float defValue) {
		return sp.getFloat(key, defValue);
	}

	public void saveString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public void remove(String key) {
		sp.edit().remove(key).commit();
	}

	/**
	 * 保存序列化过的对象
	 * 
	 * @param key
	 * @param obj
	 */
	public void saveObj(String key, Object obj) {
		if (obj == null)
			return;
		if (!(obj instanceof Serializable)) {
			throw new IllegalArgumentException("The object should implements Serializable!");
		}

		// 1.write obj to bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);

			// 2.convert obj to string via Base64
			byte[] bytes = Base64.encode(baos.toByteArray(), Base64.DEFAULT);

			// 3.save string
			saveString(key, new String(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取序列化过的对象
	 * 
	 * @param key
	 * @return
	 */
	public Object getObj(String key) {
		// 1.get string
		String string = sp.getString(key, null);

		if (TextUtils.isEmpty(string))
			return null;

		// 2.decode string
		byte[] bytes = Base64.decode(string, Base64.DEFAULT);

		// 3.read bytes to Object
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void clearSP() {
		Editor editor = sp.edit();
		editor.putString("password", "");
		editor.putString("address", "");
		editor.putString("cardEnable", "0");
		editor.putString("company", "");
		editor.putString("ecpAccount", "");
		editor.putString("email", "");
		editor.putString("headImg", "");
		editor.putString("name", "");
		editor.putString("nickname", "");
		editor.putString("remark", "");
		editor.putString("sex", "");
		editor.putString("birthday", "");
		editor.commit();
	}

}
