package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.TitleBarView;
import android.content.Context; 
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterInfoActivity extends BaseActivity {
	private Context mContext;
	private Button btn_complete;
	private TitleBarView mTitleBarView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_userinfo);
		mContext=this;
		
		findView();
		initTitleView();
		init();
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		btn_complete=(Button) findViewById(R.id.register_complete);
	}
	
	private void init(){
		btn_complete.setOnClickListener(completeOnClickListener);
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.title_register_info);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}
	
	private OnClickListener completeOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		   openActivity(RegisterResultActivity.class);
		}
	};
	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
