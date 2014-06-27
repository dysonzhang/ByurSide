package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.TextURLView;
import com.ysls.imhere.widget.TitleBarView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterResultActivity extends BaseActivity {
	private Context mContext;
	private TitleBarView mTitleBarView;
	private TextURLView url;
	private Button complete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_result);
		mContext = this;

		findView();
		initTitleView();
		initTvUrl();
		init();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		url = (TextURLView) findViewById(R.id.tv_tiaokuan);
		complete = (Button) findViewById(R.id.register_success);
	}

	private void init() {
		complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(LoginActivity.class);
				defaultFinish();
			}
		});
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setTitleText(R.string.tv_register_success);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}

	private void initTvUrl() {
		url.setText(R.string.tv_tiaokuan);
		url.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
