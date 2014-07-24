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
import android.widget.EditText;

import android.widget.Toast;

public class FeedbackActivity extends BaseActivity {

	private Context mContext;

	private Button feedback_btn;
	private EditText et_feedback_content;

	private TitleBarView mTitleBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		mContext = this;

		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		feedback_btn = (Button) findViewById(R.id.feedback_btn);
		et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.menu_feedback);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
		feedback_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (et_feedback_content.getText().toString().equals("")) {
					Toast.makeText(mContext, "请先输入您的反馈内容。", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(mContext, "提交成功！我们会尽快解决您的问题。",
							Toast.LENGTH_LONG).show();
					defaultFinish();
				}

			}
		});
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
