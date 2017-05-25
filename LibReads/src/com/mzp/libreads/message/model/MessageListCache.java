package com.mzp.libreads.message.model;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.common.utils.ImageLoader;
import com.mzp.libreads.common.utils.StringUtil;
import com.mzp.libreads.common.utils.TimerUtil;
import com.mzp.libreads.message.info.MessageInfo;

public class MessageListCache {

	private MessageListCache(){}
	
	private static MessageListCache listCache = new MessageListCache();
	
	public List<MessageInfo> messageInfos = null;
	
	public static MessageListCache getInstance(){
		return listCache;
	}
	
	public List<MessageInfo> getAllMessageList(){
		if (messageInfos == null) {
			List<MessageInfo> list = new ArrayList<>();
			
			MessageInfo info1 = new MessageInfo();
			info1.messageAccount = "100001";
			info1.messageContant ="某某教授某某专题演讲";
			info1.messageId = StringUtil.getUUID();
			info1.messageTime = TimerUtil.getCurrentTime();
			info1.messageType =1;
			info1.messageUrl ="";
			info1.unreadCount = 0;
			info1.messageName ="活动通知";
			
			list.add(info1);
			

			MessageInfo info2 = new MessageInfo();
			info2.messageAccount = "100002";
			info2.messageContant ="您借阅的图书归还期到了";
			info2.messageId = StringUtil.getUUID();
			info2.messageTime = TimerUtil.getCurrentTime();
			info2.messageType =2;
			info2.messageUrl ="";
			info2.unreadCount = 0;
			info2.messageName ="归还通知";
			
			list.add(info2);
			
			MessageInfo info3 = new MessageInfo();
			info3.messageAccount = "100003";
			info3.messageContant ="您收藏的图书到了哟,请速来阅读";
			info3.messageId = StringUtil.getUUID();
			info3.messageTime = TimerUtil.getCurrentTime();
			info3.messageType =3;
			info3.messageUrl ="";
			info3.unreadCount = 0;
			info3.messageName ="订阅通知";
			
			list.add(info3);
			
			MessageInfo info4 = new MessageInfo();
			info4.messageAccount = "100004";
			info4.messageContant ="某某考试现在已经可以查询";
			info4.messageId = StringUtil.getUUID();
			info4.messageTime = TimerUtil.getCurrentTime();
			info4.messageType =4;
			info4.messageUrl ="";
			info4.unreadCount = 0;
			info4.messageName ="查询通知";
			
			list.add(info4);
			messageInfos = list;
		}
		
		return messageInfos;
	}
	
	public void reset(){
		messageInfos = null;
	}
}
