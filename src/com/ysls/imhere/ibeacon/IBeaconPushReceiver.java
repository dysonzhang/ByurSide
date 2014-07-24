package com.ysls.imhere.ibeacon;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ysls.imhere.MainActivity;
import com.ysls.imhere.MyApplication;
import com.ysls.imhere.WelcomeActivity;

/**
 * IBeaconPushReceiver
 * 
 * @author Administrator
 * 
 */

public class IBeaconPushReceiver extends BroadcastReceiver {

	public static final String IBEACON_PUSH_RECEVIER_ACTION = "com.ysls.imhere.ibeacon.IBeaconPushReceiver";
	private ShopBeaconPush mShopBeaconPush;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent != null) {
			mShopBeaconPush = (ShopBeaconPush) intent
					.getParcelableExtra("mShopBeaconPush");
		}

		if (intent.getAction().equals(IBEACON_PUSH_RECEVIER_ACTION)) {
			Intent intent2;
			if (isStartup(context)) {
				Log.i("State", "--------->" + " app has started...");

				if (isApplicationBroughtToBackground(context)) {
					intent2 = new Intent(context, MainActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent2);

					showPushDialog(context);

				} else {
					intent2 = intent;
				}

			} else {

				intent2 = new Intent(context, WelcomeActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent2);
			}
		}
	}

	public void showPushDialog(Context context) {

		Intent in = new Intent(context, IBeaconPushActivity.class);

		Bundle mBundle = new Bundle();
		mBundle.putParcelable("ShopBeaconPush", mShopBeaconPush);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		in.putExtras(mBundle);
		MyApplication.activityList.get(
				MyApplication.getInstance().getActivitySize() - 1)
				.startActivity(in);
	}

	public static boolean isStartup(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> mRunningTaskInfo = activityManager
				.getRunningTasks(100);
		for (RunningTaskInfo mRTI : mRunningTaskInfo) {
			if (mRTI.baseActivity.getPackageName().equals(
					"com.ysls.imhere")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
