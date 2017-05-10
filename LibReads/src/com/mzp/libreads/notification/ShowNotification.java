package com.mzp.libreads.notification;

import com.mzp.libreads.R;
import com.mzp.libreads.common.constant.CustomConstantCode;
import com.mzp.libreads.login.LoginActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/**
 * 显示推送通知的单例类
 * @author may
 */
public class ShowNotification {

	private ShowNotification(){}
	
	private static ShowNotification notification = new ShowNotification();
	
	public static ShowNotification getInstence(){
		return notification;
	}
	
	//showNotification(R.mipmap.ic_launcher,"紧急通知","协同通信","中华人民共和国统一世界")
	/**
	 * 
	 * @param mContext 
	 * @param icon 图标
	 * @param tickertext 标题
	 * @param title 应用名称
	 * @param content 内容
	 * @param url 消息对应的链接
	 * @param rerult 转的类
	 */
	public <T> void showNotification(Context mContext,int icon,String tickertext,String title,String content,String url,Class<T> rerult){  
        //设置一个唯一的ID，随便设置  
   
        //Notification管理器  
//        Notification notification=new Notification(icon,tickertext,System.currentTimeMillis());  
		Intent intent = new Intent(mContext,rerult);
		Bundle bundle = new Bundle();
		bundle.putString(CustomConstantCode.MESSAGE_WEB_URL, url);
		intent.putExtras(bundle);
		PendingIntent pt=PendingIntent.getActivity(mContext, 0, intent, 0);  
        @SuppressWarnings("deprecation")
		Notification notification=new Notification.Builder(mContext)
        		.setSmallIcon(icon)//图标
        		.setContentTitle(title)//标题
        		.setContentText(tickertext)
        		.setContentInfo(content)
        		.setContentIntent(pt)
        		.setWhen(System.currentTimeMillis())
        		.setAutoCancel(true)
        		.build();
        
        //后面的参数分别是显示在顶部通知栏的小图标，小图标旁的文字（短暂显示，自动消失）系统当前时间（不明白这个有什么用）  
        notification.defaults=Notification.DEFAULT_ALL;   
        //这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND  
        //振动为Notification.DEFAULT_VIBRATE;  
        //Light为Notification.DEFAULT_LIGHTS，在我的Milestone上好像没什么反应  
        //全部为Notification.DEFAULT_ALL  
        //如果是振动或者全部，必须在AndroidManifest.xml加入振动权限  
//        PendingIntent pintent = PendingIntent.getActivity(getContext(), 0, newintent, 0); 
//        notification.
        //点击通知后的动作，这里是转回main 这个Acticity  
//        notification.setLatestEventInfo(this,title,content,pt); 
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE); 
        nm.notify(0, notification);  
    }  
}
