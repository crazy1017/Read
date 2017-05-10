package com.mzp.libreads.main.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.main.pager.BasePager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MainPagerAdapter extends PagerAdapter {

	public List<BasePager> mList;
	
	public  MainPagerAdapter(List<BasePager> mList) {
		// TODO Auto-generated constructor stub
		this.mList = mList == null ? new ArrayList<BasePager>() : mList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		BasePager basePager = mList.get(position);
		container.addView(basePager.mRootView);
		return basePager.mRootView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View) object);
	}

}
