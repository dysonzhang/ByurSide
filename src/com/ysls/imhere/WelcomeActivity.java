package com.ysls.imhere;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import cn.jpush.android.api.JPushInterface;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.test.SDManager;
import com.ysls.imhere.utils.SharedPreferencesUtil;

/**
 * 欢迎界面
 * 
 * @author dyson
 * 
 */
public class WelcomeActivity extends BaseActivity {

	private Boolean isFirstUse;
	private Boolean isLogin;
	private Handler mHandler = new Handler();

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		View localView = View.inflate(this, R.layout.activity_welcome, null);

		setContentView(localView);
		Animation localAnimation = AnimationUtils.loadAnimation(this,
				R.anim.alpha);
		localView.startAnimation(localAnimation);
		localAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation paramAnimation) {
				mHandler.postDelayed(new Runnable() {
					public void run() {
						goHome();
					}
				}, 500L);
			}

			public void onAnimationRepeat(Animation paramAnimation) {
			}

			public void onAnimationStart(Animation paramAnimation) {
			}
		});
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	public void goHome() {

		isFirstUse = (Boolean) SharedPreferencesUtil.getParam(this,
				"isFirstUse", true);
		isLogin = (Boolean) SharedPreferencesUtil.getParam(this,
				"isLogin", false);
		Global.isLogin = isLogin;
		
		if (isFirstUse) {
			
			SDManager manager = new SDManager(this);
			manager.moveUserIcon();
			
			SharedPreferencesUtil.setParam(this, "isFirstUse", false);
			openActivity(GuideActivity.class);
		} else {
			if(isLogin){
				openActivity(HomeActivity.class);
			}else{
				openActivity(LoginActivity.class);
			}
		}
		defaultFinish();
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}