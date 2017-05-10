package com.mzp.libreads.main.pager;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.R;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import io.reactivex.internal.schedulers.NewThreadScheduler;

public class ReadPager extends BasePager {

	private GridView gv_list;

	private int[] ids= {
			R.drawable.t1,
			R.drawable.t2,
			R.drawable.t3,
			R.drawable.t4,
			R.drawable.t5,
			R.drawable.t6,
			R.drawable.t7};
	
	private String[] names ={
			"百炼成神",
			"冰与火之歌",
			"大秦帝国",
			"古董局中局",
			"明朝那些事",
			"万道成神",
			"至尊神魔"
	};
	
	private List<Integer> idsList = new ArrayList<>();
	private List<String> namesList = new ArrayList<>();
	
	public ReadPager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("读书");
		
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		View child = View.inflate(mActivity, R.layout.pager_read, null);
		gv_list = (GridView) child.findViewById(R.id.gv_list);
		
		flContainer.addView(child);
	}
	
	private void loadData() {
		idsList.add(R.drawable.t1);
		idsList.add(R.drawable.t2);
		idsList.add(R.drawable.t3);
		idsList.add(R.drawable.t4);
		idsList.add(R.drawable.t5);
		idsList.add(R.drawable.t6);
		idsList.add(R.drawable.t7);
		
		namesList.add("百炼成神");
		namesList.add("冰与火之歌");
		namesList.add("大秦帝国");
		namesList.add("古董局中局");
		namesList.add("明朝那些事");
		namesList.add("万道成神");
		namesList.add("至尊神魔");
		
//		gv_list.setAdapter(adapter);
	}

}
