package com.mzp.libreads.message.info;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageInfo implements Parcelable {

	public MessageInfo() {}
	
	public String messageName;
	
	public String messageId;
	
	public String messageAccount;
	
	/**消息类型*/
	public int messageType;
	
	/**未读消息数量*/
	public int unreadCount;
	
	/**是否置顶*/
	public int flagTop;

	/**消息内容*/
	public String messageContant;
	
	/**消息链接*/
	public String messageUrl;
	
	public String messageImg;
	
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
