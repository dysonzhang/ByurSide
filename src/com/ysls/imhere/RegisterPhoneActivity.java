package com.ysls.imhere;

import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.TextURLView;
import com.ysls.imhere.widget.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterPhoneActivity extends BaseActivity {

	private Context mContext;
	private TitleBarView mTitleBarView;
	private TextURLView mTextViewURL;
	private Button next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		mContext=this;
		
		findView();
		initTitleView();
		initTvUrl();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		mTextViewURL=(TextURLView) findViewById(R.id.tv_url);
		next=(Button) findViewById(R.id.btn_next);
	}
	
	private void init(){
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity(RegisterInfoActivity.class);
			}
		});
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setTitleText(R.string.title_phoneNumber);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}
	
	private void initTvUrl(){
		mTextViewURL.setText(R.string.tv_xieyi_url);
	}
}
