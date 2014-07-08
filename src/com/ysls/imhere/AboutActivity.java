package com.ysls.imhere;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity; 
import com.ysls.imhere.widget.TitleBarView;
 
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle; 
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
 
	private Context mContext;
	
	private TextView mWeixin;
	private TextView mWeibo;

	private TitleBarView mTitleBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mContext = this;
		
		findView();
		initTitleView();
	}

	private void findView() {
		mTitleBarView = (TitleBarView)findViewById(R.id.title_bar);
		
		
		mWeixin = (TextView) findViewById(R.id.about_textview_weixin);
		String htmlLinkText = "<a href=\"http://weixin.qq.com/r/7HX_8R7EfiABhw_SnyDI\"> "
				+ getResources().getString(R.string.about_weixin) + "</a>";
		String htmlLinkTextWeibo = "<a href=\"#\"> "
				+ getResources().getString(R.string.about_sina) + "</a>";
		mWeixin.setText(Html.fromHtml(htmlLinkText));
		mWeixin.setMovementMethod(LinkMovementMethod.getInstance());
		mWeibo = (TextView) findViewById(R.id.about_textview_weibo);
		mWeibo.setText(Html.fromHtml(htmlLinkTextWeibo));
		mWeibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri
						.parse("http://e.weibo.com/eoeandroid00?ref=http%3A%2F%2Fwww.weibo.com%2Fu%2F1959452825%3Fwvr%3D5%26");
				intent.setData(content_url);
				startActivity(intent);
			}
		});
	}
	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.about);
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
