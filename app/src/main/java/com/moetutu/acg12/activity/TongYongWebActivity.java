package com.moetutu.acg12.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moetutu.acg12.R;


/**
 * Description  暂时没什么卵用  通用的webview
 * Company guokeyuzhou
 * Created by chengwanying on 16/6/15.
 * version
 */
public class TongYongWebActivity extends BaseActivity{
	private WebView mWebView;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_zx_activity);
		Intent in = getIntent();
		url = in.getStringExtra("url");
		initView(this);
	}
	
	@Override
	public void initView(Activity activity) {
		// TODO Auto-generated method stub
		super.initView(activity);
		setTitleLeftImageBack();
		mWebView = (WebView) activity.findViewById(R.id.webViewpdf);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.requestFocus();
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.setInitialScale(100);

		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边

				view.loadUrl(url);

				return true;

			}
		});

		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				Log.i("tag", "url="+url);              
				Log.i("tag", "userAgent="+userAgent);   
				Log.i("tag", "contentDisposition="+contentDisposition);            
				Log.i("tag", "mimetype="+mimetype);   
				Log.i("tag", "contentLength="+contentLength);   
				Uri uri = Uri.parse(url);   
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
				startActivity(intent);   
			}
		});

		mWebView.loadUrl(url);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.view_title_left_image_btn_back:// back
			mWebView.clearCache(true);
			finish();
			break;

		default:
			break;
		}
	}

}
