package com.mzp.libreads.main;

import java.util.ArrayList;
import java.util.List;

import com.mzp.libreads.R;
import com.mzp.libreads.common.log.OperLog;
import com.mzp.libreads.common.sys.ScreenUtil;
import com.mzp.libreads.main.adapter.MainPagerAdapter;
import com.mzp.libreads.main.pager.BasePager;
import com.mzp.libreads.main.pager.BooksPager;
import com.mzp.libreads.main.pager.MePager;
import com.mzp.libreads.main.pager.MessagePager;
import com.mzp.libreads.main.pager.ReadPager;
import com.mzp.libreads.main.view.NoScrollViewPager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener, OnPageChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private NoScrollViewPager vp_content;
	private RadioGroup rg_group;
	private RadioButton rb_read,rb_books,rb_message,rb_setting;
	private List<BasePager> mList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);
		
		initViews();
		initData();
	}
	
	private void initViews() {
		vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
		rg_group = (RadioGroup) findViewById(R.id.rg_group);
		
		rb_read = (RadioButton) findViewById(R.id.rb_read);
		rb_books = (RadioButton) findViewById(R.id.rb_books);
		rb_message = (RadioButton) findViewById(R.id.rb_message);
		rb_setting = (RadioButton) findViewById(R.id.rb_setting);
		
		rg_group.setOnCheckedChangeListener(this);
	}
	
	private void initData(){
		mList = new ArrayList<>();
		mList.add(new ReadPager(this));
		mList.add(new BooksPager(this));
		mList.add(new MessagePager(this));
		mList.add(new MePager(this));
		vp_content.setAdapter(new MainPagerAdapter(mList));
		vp_content.addOnPageChangeListener(this);
		mList.get(0).initData();
		
		showTabIcon(rb_read, R.drawable.tab_read_selector);
		showTabIcon(rb_books, R.drawable.tab_books_selector);
		showTabIcon(rb_message, R.drawable.tab_message_selector);
		showTabIcon(rb_setting, R.drawable.tab_me_selector);
	}
	
	@SuppressWarnings("deprecation")
	public void showTabIcon(RadioButton view,int id){
		Drawable drawableRead = getResources().getDrawable(id);  
		drawableRead.setBounds(0, 0, ScreenUtil.dip2px(28), ScreenUtil.dip2px(28));  
		view.setCompoundDrawables(null, drawableRead, null, null);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch (checkedId) {
		
		case R.id.rb_read:
			vp_content.setCurrentItem(0,false);
			OperLog.error(TAG, "加载读书界面");
			break;
		case R.id.rb_books:
			vp_content.setCurrentItem(1,false);
			OperLog.error(TAG, "加载书城界面");
			break;
		case R.id.rb_message:
			vp_content.setCurrentItem(2,false);
			OperLog.error(TAG, "加载消息界面");
			break;
		case R.id.rb_setting:
			vp_content.setCurrentItem(3,false);
			OperLog.error(TAG, "加载我的界面");
			break;
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		BasePager basePager = mList.get(position);
		basePager.initData();
	}
}
