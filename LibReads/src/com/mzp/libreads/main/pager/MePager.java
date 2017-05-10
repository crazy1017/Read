package com.mzp.libreads.main.pager;

import android.app.Activity;

public class MePager extends BasePager {

	public MePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("我的");
	}

}
