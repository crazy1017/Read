package com.mzp.libreads.login;

import com.mzp.libreads.R;
import com.mzp.libreads.common.AppCache;
import com.mzp.libreads.common.utils.SharePrefUtil;
import com.mzp.libreads.db.manager.DataBaseOpenHelperManager;
import com.mzp.libreads.main.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

	private EditText login_et_account;
	private EditText login_et_password;
	private Button login_btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		
		initView();
		initData();
	}

	private void initView() {
		login_et_account = (EditText) findViewById(R.id.login_et_account);
		login_et_password = (EditText) findViewById(R.id.login_et_password);
		login_btn_login = (Button) findViewById(R.id.login_btn_login);
	}

	private void initData() {
		login_btn_login.setOnClickListener(this);
	}
	
	private String getAccount(){
		if (login_btn_login == null) {
			return null;
		}
		return login_et_account.getText().toString().replace(" ", "");
	}
	
	private String getPassword(){
		if (login_et_password == null) {
			return null;
		}
		return login_et_password.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		String account = getAccount();
		String password = getPassword();
		
		if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || (account != null && account.replace( " " , "").length() == 0)) {
			return ;
		}
		
		login(account, password);
	}
	
	private void login(String account,String password) {
		startActivity(new Intent(LoginActivity.this,MainActivity.class));
		DataBaseOpenHelperManager.getInstance(account+".db");
		SharePrefUtil.create(AppCache.getContext()).saveString("userinfo", account+".db");
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
