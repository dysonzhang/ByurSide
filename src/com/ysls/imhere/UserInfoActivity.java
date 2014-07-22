package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity; 
import com.ysls.imhere.widget.TitleBarView;
 
import android.content.Context;
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener; 

public class UserInfoActivity extends BaseActivity {
 
	private Context mContext;
	
	private TitleBarView mTitleBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		mContext = this;
		
		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView)findViewById(R.id.title_bar);
	}
	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.menu_user_info);
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
		// TODO Auto-generated method stub
	}
}
