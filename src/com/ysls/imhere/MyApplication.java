package com.ysls.imhere;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Build;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.radiusnetworks.ibeacon.IBeaconManager;
import com.ysls.imhere.baidu.BaiduLocation;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.ibeacon.ShopplayIBeaconService;
import com.ysls.imhere.utils.LogUtil;

/**
 * Global Application
 * 
 * @author dyson
 * 
 */
public class MyApplication extends Application {

	private static String TAG = "MyApplication";
	public static Context ctx;
	private static MyApplication mApp;

	private IBeaconManager iBeaconManager;

	public static MyApplication getInstance() {
		if (mApp == null) {
			return new MyApplication();
		}
		return mApp;
	}

	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
		iBeaconManager = IBeaconManager.getInstanceForApplication(ctx);

		// Baidu location api init
		BaiduLocation.getBaiduLocationInstance(ctx).sendLocationReq();

		// JPush api init
		JPushInterface.setDebugMode(true);
		JPushInterface.init(ctx);

		// ShareSdk api init
		ShareSDK.initSDK(ctx, Constants.ShareSdk_Appkey);

		// iBeacon service background and foreground control
		iBeaconBackAndforeControl(ctx);
	}

	/**
	 * iBeacon service background and foreground control
	 * 
	 * @param context
	 */
	private void iBeaconBackAndforeControl(Context context) {
		if (!isBackground(context)) {
			if (iBeaconManager.isBound(ShopplayIBeaconService.getInstance())) {
				iBeaconManager.setBackgroundMode(
						ShopplayIBeaconService.getInstance(), false);
				LogUtil.i(TAG, "set it in the foreground");
			}
		} else {
			if (iBeaconManager.isBound(ShopplayIBeaconService.getInstance())) {
				iBeaconManager.setBackgroundMode(
						ShopplayIBeaconService.getInstance(), true);
				LogUtil.i(TAG, "set it in the background");

			}
		}
	}

	/**
	 * Exit application
	 */
	public static void exitApp() {

		if (Global.CHECKIN_MODE == Constants.BT_GENREAL_CHECKIN_MODE) {
			// exitBTserachThread();
		}

		finishProgram();

		android.os.Process.killProcess(android.os.Process.myPid());

		ActivityManager activityMgr = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		int currentVersion = Build.VERSION.SDK_INT;
		if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
			activityMgr.killBackgroundProcesses(ctx.getPackageName());
		} else {
			activityMgr.restartPackage(ctx.getPackageName());
		}

		BaiduLocation.getBaiduLocationInstance(ctx).stopLocationReq();
		ShareSDK.stopSDK(ctx);

		System.exit(0);
	}

	/**
	 * Check application isBackground
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					LogUtil.i(TAG, "app is background");
					return true;
				} else {
					LogUtil.i(TAG, "app is foreground");
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * isAppOnForeground
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				LogUtil.i(TAG, "app is foreground");
				return true;
			}
		}
		LogUtil.i(TAG, "app is background");
		return false;
	}

	/**
	 * List<Activity>
	 */
	public static List<Activity> activityList = new LinkedList<Activity>();

	/**
	 * Get activity list size
	 * 
	 * @return
	 */
	public int getActivitySize() {
		return activityList.size();
	}

	/**
	 * Add an init activity to activity list
	 * 
	 * @param activity
	 */
	public void add(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * Finish all activity that in activity list
	 */
	public static void finishProgram() {

		for (Activity activity : activityList) {
			activity.finish();
		}
	}

	/**
	 * finish someone activity by name
	 */
	public void finishActivityByName(String name) {
		for (Activity ac : activityList) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				ac.finish();
			}
		}
	}

	/**
	 * Check someone activity is active by name
	 */
	public boolean isActiveActivityByName(String name) {
		for (Activity ac : activityList) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return true;
			}
		}
		return false;
	}
}