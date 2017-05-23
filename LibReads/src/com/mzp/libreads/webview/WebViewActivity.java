package com.mzp.libreads.webview;

import com.mzp.libreads.R;
import com.mzp.libreads.common.utils.ToastUtil;
import com.mzp.libreads.login.LoginActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends AppCompatActivity {
	
	private static final String URL = "web_url";
	private WebView webview;
	private ProgressBar progressbar;
	
	public static void startWebViewActivity(Context context,String url){
		Intent intent = new Intent(context,WebViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(URL, url);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getSupportActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		initViews();
		initDatas();
	}

	private void initViews() {
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		webview = (WebView) findViewById(R.id.webview);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		initWebViews();
		String url = getIntent().getStringExtra(URL);
		loadurl(url);
	}
	
    public void loadurl(String url){
		                  
		progressbar.setVisibility(View.VISIBLE);
        //加载需要显示的网页  
        webview.loadUrl(url);  
        //设置Web视图  
        webview.setWebViewClient(new WebViewClient (){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);
        		return true;
        	}
        	
        	@Override
        	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        		// TODO Auto-generated method stub
        		super.onReceivedError(view, request, error);
        		ToastUtil.showToast(request.toString()+" : "+error.toString());
        	}
        	
        	@Override
        	public void onReceivedHttpError(WebView view, WebResourceRequest request,
        			WebResourceResponse errorResponse) {
        		// TODO Auto-generated method stub
        		super.onReceivedHttpError(view, request, errorResponse);
        		ToastUtil.showToast(request.toString()+" : "+errorResponse.toString());
        	}
        }); 
        
        webview.setWebChromeClient(new WebChromeClient(){
        	@Override
        	public void onProgressChanged(WebView view, int newProgress) {
        		super.onProgressChanged(view, newProgress);
        		if (newProgress == 100 && progressbar.isShown()) {
					progressbar.setVisibility(View.GONE);
				}else {
					progressbar.setProgress(newProgress);
				}
        	}
        	
        });
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){  
            webview.goBack();// 返回前一个页面  
            return true;  
        }  else {
        	startActivity(new Intent(WebViewActivity.this,LoginActivity.class));
        	finish();
			return true;
		}
    }

	private void initWebViews() {
		// TODO Auto-generated method stub
		webview.getSettings().setDomStorageEnabled(true);  
		 // 设置支持javascript  
		webview.getSettings().setJavaScriptEnabled(true);  
		// 启动缓存  
		webview.getSettings().setAppCacheEnabled(true);  
		// 设置缓存模式  
		if (Build.VERSION.SDK_INT >=19) {
			webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); 
		}else {
			webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); 
		}
	}
}
