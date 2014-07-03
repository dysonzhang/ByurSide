package com.ysls.imhere;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.widget.ProgressWebView;
import com.ysls.imhere.widget.TitleBarView;

public class WebViewActivity extends BaseActivity {

	private ProgressWebView webview;
	private TitleBarView mTitleBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		initView();
		initTitleView();
	}

	private void initView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		webview = (ProgressWebView) findViewById(R.id.webview);
		webview.loadUrl(Global.WebView_URL);
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
//		mTitleBarView.setTitleText(null);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
	}

}
