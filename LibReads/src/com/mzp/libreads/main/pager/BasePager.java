package com.mzp.libreads.main.pager;

import com.mzp.libreads.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class BasePager {

	public Activity mActivity;

	public TextView tvTitle;
	public FrameLayout flContainer;//空的帧布局, 由子类动态填充布局
	public ImageButton btnDisplay;//组图切换按钮
	public View mRootView;//当前页面的根布局
	public BasePager(Activity activity) {
		mActivity = activity;
		//在页面对象创建时就初始化了布局
		mRootView = initViews();
	}

	//初始化布局
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		flContainer = (FrameLayout) view.findViewById(R.id.fl_container);
		btnDisplay = (ImageButton) view.findViewById(R.id.btn_display);
		return view;
	}

	//初始化数据
	public void initData() {

	}

}
