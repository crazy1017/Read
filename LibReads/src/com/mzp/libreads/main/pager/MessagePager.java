package com.mzp.libreads.main.pager;

import android.app.Activity;

public class MessagePager extends BasePager {

	public MessagePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("消息");
	}
}
