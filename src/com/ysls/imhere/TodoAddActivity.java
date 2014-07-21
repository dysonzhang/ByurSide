package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity; 
import com.ysls.imhere.widget.TitleBarView;
 
import android.content.Context; 
import android.os.Bundle;  
import android.view.View;
import android.view.View.OnClickListener;  

public class TodoAddActivity extends BaseActivity {
 
	private Context mContext;
	
	private TitleBarView mTitleBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_add);
		mContext = this;
		
		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView)findViewById(R.id.title_bar);
	}
	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setBtnRight(R.drawable.btn_ok_send);
		mTitleBarView.setTitleText(R.string.todo_add);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLongToast("任务发布成功!");
				defaultFinish();
			}
		});
	}
	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) { 
	}
}
