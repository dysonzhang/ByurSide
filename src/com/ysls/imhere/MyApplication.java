package com.ysls.imhere;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.ysls.imhere.baidu.BaiduLocation;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.db.DbOpenHelper;
import com.ysls.imhere.db.UserDao;
import com.ysls.imhere.domain.User;
import com.ysls.imhere.ibeacon.BluetoothController;
import com.ysls.imhere.ibeacon.ShopplayIBeaconService;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.utils.PreferenceUtils;

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

	// login user name
	public final String PREF_USERNAME = "username";
	private String userName = null;
	// login password
	private static final String PREF_PWD = "pwd";
	private String password = null;
	private Map<String, User> contactList;

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
		BluetoothController.openBTAdapter();
		iBeaconManager = IBeaconManager.getInstanceForApplication(ctx);

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk 3.3
			// 百度定位sdk3.3，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

		// Baidu location api init
		BaiduLocation.getBaiduLocationInstance(ctx).sendLocationReq();

		// JPush api init
		JPushInterface.setDebugMode(true);
		JPushInterface.init(ctx);

		// ShareSdk api init
		ShareSDK.initSDK(ctx, Constants.ShareSdk_Appkey);

		// iBeacon service background and foreground control
		iBeaconBackAndforeControl(ctx);

		// 初始化环信SDK,一定要先调用init()
		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChat.getInstance().init(ctx);
		// debugmode设为true后，就能看到sdk打印的log了
		EMChat.getInstance().setDebugMode(true);

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(PreferenceUtils.getInstance(ctx)
				.getSettingMsgNotification());
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(PreferenceUtils.getInstance(ctx)
				.getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(PreferenceUtils.getInstance(ctx)
				.getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(PreferenceUtils.getInstance(ctx)
				.getSettingMsgSpeaker());

		// 取消注释，app在后台，有新消息来时，标题栏的消息提示换成自己写的
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				// 可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
				return "你的好友" + message.getFrom() + "发来了一条消息";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
			}
		});

	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(ctx);
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(ctx);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(ctx);
			password = preferences.getString(PREF_PWD, null);
		}
		return password;
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(ctx);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}

	/**
	 * 设置密码
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PWD, pwd).commit()) {
			password = pwd;
		}
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		EMChatManager.getInstance().logout();
		DbOpenHelper.getInstance(ctx).closeDB();
		// reset password to null
		setPassword(null);
		setContactList(null);

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
	@SuppressWarnings("deprecation")
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

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
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