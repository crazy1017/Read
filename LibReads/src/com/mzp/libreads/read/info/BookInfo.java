package com.mzp.libreads.read.info;

import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {

	/**书名*/
	public String bookName;
	
	/**书对应的图片*/
	public String bookImg;
	
	/**书对应的唯一id*/
	public String bookId;
	
	/**书的作者*/
	public String bookAuthor;
	
	/**书对应的md5*/
	public String MD5;
	
	/**上一次读书时间*/
	public String lastTime;
	
	/**已经阅读的页数*/
	public int readPagers;
	
	/**是否已经更新完结*/
	public int updateOver;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

}
