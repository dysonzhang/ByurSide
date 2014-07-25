package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.TitleBarView;

import android.content.Context; 
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout; 

public class PublicationActivity extends BaseActivity implements OnClickListener {

	private Context mContext;

	private TitleBarView mTitleBarView;
	
	private RelativeLayout rl_notice_one;
	private RelativeLayout rl_notice_two;
	private RelativeLayout rl_notice_three;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publication);
		mContext = this;

		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		
		rl_notice_one = (RelativeLayout) findViewById(R.id.rl_notice_one);
		rl_notice_two = (RelativeLayout) findViewById(R.id.rl_notice_two);
		rl_notice_three = (RelativeLayout) findViewById(R.id.rl_notice_three);
		
		rl_notice_one.setOnClickListener(this);
		rl_notice_two.setOnClickListener(this);
		rl_notice_three.setOnClickListener(this);
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.menu_publication);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
