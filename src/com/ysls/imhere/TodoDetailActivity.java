package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.widget.ImageViewWithText;
import com.ysls.imhere.widget.TitleBarView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TodoDetailActivity extends BaseActivity implements OnClickListener {

	private Context mContext;

	private TitleBarView mTitleBarView;

	private ImageViewWithText member_one;
	private ImageViewWithText member_two;
	private ImageViewWithText member_three;
	private ImageViewWithText member_four;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_detail);
		mContext = this;

		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);

		member_one = (ImageViewWithText) findViewById(R.id.member_one);
		member_one.setOnClickListener(this);
		member_two = (ImageViewWithText) findViewById(R.id.member_two);
		member_two.setOnClickListener(this);
		member_three = (ImageViewWithText) findViewById(R.id.member_three);
		member_three.setOnClickListener(this);
		member_four = (ImageViewWithText) findViewById(R.id.member_four);
		member_four.setOnClickListener(this);

	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.todo_detail);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.member_one:
			startActivity(new Intent(this, ChatActivity.class).putExtra(
					"userId", "bill"));
			break;
		case R.id.member_two:
			startActivity(new Intent(this, ChatActivity.class).putExtra(
					"userId", "dyson"));
			break;
		case R.id.member_three:
			startActivity(new Intent(this, ChatActivity.class).putExtra(
					"userId", "jerry"));
			break;
		case R.id.member_four:
			startActivity(new Intent(this, ChatActivity.class).putExtra(
					"userId", "sam"));
			break;
		}

	}
}
