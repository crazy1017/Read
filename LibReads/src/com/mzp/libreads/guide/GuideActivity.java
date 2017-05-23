package com.mzp.libreads.guide;

import com.mzp.libreads.R;
import com.mzp.libreads.common.constant.CustomConstantCode;
import com.mzp.libreads.common.utils.SharePrefUtil;
import com.mzp.libreads.login.LoginActivity;
import com.mzp.libreads.main.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 向导页面
 * @author may
 */
public class GuideActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//隐藏actionbar
		getActionBar().hide();
		setContentView(R.layout.activity_guide);
		
		initViews();
		initData();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		boolean isGuide = SharePrefUtil.create(this).getBoolean(CustomConstantCode.ISGUIDE, false);
		
		if (isGuide) {
			startActivity(new Intent(GuideActivity.this, LoginActivity.class));
			finish();
		}
		startActivity(new Intent(GuideActivity.this, LoginActivity.class));
		SharePrefUtil.create(this).saveBoolean(CustomConstantCode.ISGUIDE, true);
	}
}
