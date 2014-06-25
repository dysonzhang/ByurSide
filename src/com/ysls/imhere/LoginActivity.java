package com.ysls.imhere;

import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.utils.ShareSDKUtil;
import com.ysls.imhere.widget.TextURLView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Login Activity
 * 
 * @author dyson
 * 
 */
public class LoginActivity extends BaseActivity {

	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
	private TextURLView mTextViewURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;

		findView();
		initTvUrl();
		init();

	}

	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		mTextViewURL = (TextURLView) findViewById(R.id.tv_forget_password);
	}

	private void init() {
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
		mTextViewURL.setOnClickListener(fogetOnClickListener);
	}

	private OnClickListener loginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// save user info to sharedPreference
			openActivity(HomeActivity.class);
			defaultFinish();
		}
	};

	private OnClickListener registerOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			openActivity(RegisterPhoneActivity.class);
		}
	};
	private OnClickListener fogetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ShareSDKUtil.showShare(mContext);
		}
	};
}
