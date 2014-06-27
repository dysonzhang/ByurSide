package com.ysls.imhere.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.litesuits.http.LiteHttpClient;
import com.litesuits.http.async.HttpAsyncExcutor;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.umeng.analytics.MobclickAgent;
import com.ysls.imhere.MyApplication;
import com.ysls.imhere.R;

public abstract class BaseFragmentActivity extends FragmentActivity {

	/**************HttpClient******************/
	protected LiteHttpClient client;
	
	protected HttpAsyncExcutor asyncExcutor = new HttpAsyncExcutor();

	public abstract void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod);

	/**************HttpClient******************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		client = LiteHttpClient.getInstance(this);
		
		MyApplication.getInstance().add(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void defaultFinish() {
		super.finish();
	}
}
