package com.mzp.libreads.main.pager;

import android.app.Activity;

public class BooksPager extends BasePager {

	public BooksPager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("书库");
	}
}
