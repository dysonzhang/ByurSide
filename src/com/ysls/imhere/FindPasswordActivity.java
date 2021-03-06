package com.ysls.imhere;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.TitleBarView;

public class FindPasswordActivity extends BaseActivity {

	protected String TAG = "FindPasswordActivity";

	private Context mContext;
	private TitleBarView mTitleBarView;
	private Button findPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		mContext = this;

		findView();
		initTitleView();
		init();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		findPassword = (Button) findViewById(R.id.find_password);
	}

	private void init() {
		findPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLongToast("你点击了找回密码");
			}
		});
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.find_password);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {

	}
}
