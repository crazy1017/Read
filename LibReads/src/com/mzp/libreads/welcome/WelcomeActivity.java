package com.mzp.libreads.welcome;

import com.mzp.libreads.R;
import com.mzp.libreads.login.LoginActivity;
import com.mzp.libreads.webview.WebViewActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity implements OnClickListener {

	private ImageView iv_welcome;
	private TextView tv_timer;
	private ProgressBar pb_update;
	private TextView tv_update;
	
	/**最大的加载时间*/
	private int maxTime = 5;
	
	/**加载时间*/
	private static final int LOADING_TIMER = 1;
	private TimeRunnable mTimeRunnable = null;
	private boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		
		initViews();
		initData();
	}

	private void initViews() {
		iv_welcome = (ImageView) findViewById(R.id.iv_welcome);
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		pb_update = (ProgressBar) findViewById(R.id.pb_update);
		tv_update = (TextView) findViewById(R.id.tv_update);
		
		iv_welcome.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		setTime();
		mHandler.sendEmptyMessageDelayed(LOADING_TIMER, 1000);
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			
			switch (msg.what) {
			case LOADING_TIMER:
				if (maxTime != 0) {
					maxTime--;
					setTime();
					if (mTimeRunnable == null) {
						mTimeRunnable = new TimeRunnable();
					}
					mHandler.postDelayed(mTimeRunnable, 1000);
				}else {
					//maxT
					pb_update.setVisibility(View.GONE);
					tv_update.setVisibility(View.GONE);
					mHandler.removeCallbacks(mTimeRunnable);
					if (!isOpen) {
						startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
						finish();
					}
				}
				break;

			default:
				break;
			}
		}
	};
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_welcome:
			mHandler.removeCallbacks(mTimeRunnable);
			isOpen = true;
			WebViewActivity.startWebViewActivity(this,"http://www.baidu.com");
			finish();
			break;

		default:
			break;
		}
	}
	
	class TimeRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mHandler.sendEmptyMessage(LOADING_TIMER);
		}
	}
	
	private void setTime(){
		String time = String.format(getResources().getString(R.string.welcome_timer), maxTime);
		tv_timer.setText(time);
	}
}
