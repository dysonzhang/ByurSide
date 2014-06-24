package com.ysls.imhere.ibeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.ysls.imhere.MyApplication;
import com.ysls.imhere.config.Golbal;

public class ShopplayIBeaconService extends Service implements IBeaconConsumer {
	public static final String TAG = "ShopplayIBeaconService";
	
	private IBeaconManager iBeaconManager = IBeaconManager
			.getInstanceForApplication(this);
	
	private static ShopplayIBeaconService mShopplayIBeaconService;
	
	public static Context mContext;

	private IBeacon findValidMinDistanceIbeaconsNow;

	private static List<IBeacon> HaveSeenIBeaconHistory = new ArrayList<IBeacon>();
	private IBeacon mTheNearestIbeaconBefore;

	public static List<IBeacon> copy = new ArrayList<IBeacon>();

	public static final int NONE_OR_ENTER_IBEACON = 0;
	public static final int EXIT_IBEACON = 1;
	public static final int IN_IBEACON = 2;
	public static final int IBEACON_CHANGE = 3;

	public static int IBEACON_STATE = -1;
	private IBeaconRequest mIBeaconRequest;
	public ShopBeaconPush mShopBeaconPush;
	
	
	public boolean bCanPush = false;

	/**
	 * Command to the service to display a message
	 */
	public static final int MSG_GET_CALLBACK = 2;

	/**
	 * UUID
	 */
	private static String detectedPushUUID = "25275B5C-0210-4DD5-B290-552B0AFBD80C";
	/**
	 * UUID
	 */
	private static String detectedBroadcastUUID = "25275B5C-0210-4DD5-B290-552B0AFBD80D";
	/**
	 * UUID
	 */
	private static String proximityUUID = "";
	/**
	 * VALID_PUSH_DISTANCE
	 */
	private static double VALID_PUSH_DISTANCE = 2;
	/**
	 * VALID_BROADCAST_DISTANCE
	 */
	private static double VALID_BROADCAST_DISTANCE = 100;

	/**
	 *  oldMajorAndMinor
	 */
	private String oldMajorAndMinor;
	/**
	 * newMajorAndMinor
	 */
	private String newMajorAndMinor;
	/**
	 * 0:IOS 1:Android
	 */
	private String deviceType = "1";

	private String pushContent;

	private int pushState;

	private IBeaconUserInfo mIBeaconUserInfo = new IBeaconUserInfo();

	@Override
	public void onCreate() {
		super.onCreate();
		
		mIBeaconRequest = new IBeaconRequest();
		mIBeaconRequest.setConnectTimeout(10000);
		mIBeaconRequest.setReadTimeout(10000);
		mIBeaconRequest.setRequestUrl(IBeaconRequest.iBeaconPushUrl);
//		
//		rms = new Rms(mShopplayIBeaconService, TAG);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		iBeaconManager.bind(this);
		iBeaconManager.setBackgroundScanPeriod(1100);
		iBeaconManager.setBackgroundBetweenScanPeriod(0);
		
		Log.i("State", "--------->>>" + "findValidMinDistanceIbeacons size-->"
				+ findValidMinDistanceIbeaconsNow);
	}

	public static ShopplayIBeaconService getInstance() {
		if (mShopplayIBeaconService == null)
			mShopplayIBeaconService = new ShopplayIBeaconService();
		return mShopplayIBeaconService;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Log.i("State", "onStartCommand");
			IBeaconUserInfo pIBeaconUserInfo = (IBeaconUserInfo) intent
					.getParcelableExtra("IBeaconUserInfo");

			if (pIBeaconUserInfo != null) {
				Log.i("Intent", pIBeaconUserInfo.getDevicesId());
				Log.i("Intent", pIBeaconUserInfo.getPassword());
				Log.i("Intent", pIBeaconUserInfo.getUserKey());
				Log.i("Intent", pIBeaconUserInfo.getUserID());

				mIBeaconUserInfo.setDevicesId(pIBeaconUserInfo.getDevicesId());
				mIBeaconUserInfo.setPassword(pIBeaconUserInfo.getPassword());
				mIBeaconUserInfo.setUserID(pIBeaconUserInfo.getUserID());
				mIBeaconUserInfo.setUserKey(pIBeaconUserInfo.getUserKey());

//				rms.saveString("DevicesId", pIBeaconUserInfo.getDevicesId());
//				rms.saveString("Password", pIBeaconUserInfo.getPassword());
//				rms.saveString("UserKey", pIBeaconUserInfo.getUserKey());
//				rms.saveString("UserID", pIBeaconUserInfo.getUserID());

			} else {
				Log.i("Intent", "mIBeaconUserInfo  null in command");
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new ShopplayIBeaconServiceBinder();
	}

	@Override
	public void onIBeaconServiceConnect() {

		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons,
					Region region) {

				List<IBeacon> mIBeaconNearList = getSortIbeacon(iBeacons);

				 Log.i("TAG", "mIBeaconNearList is ---->" +
				 mIBeaconNearList.size());

				Log.i("ShopplayIBeaconService", "isDetectingBeacon --->"
						+ Golbal.isDetectingBeacon);

				if (Golbal.isDetectingBeacon) {

					if (mIBeaconNearList.size() > 0) {
						IBeacon mIBeacon = mIBeaconNearList.get(0);
						mIBeaconNearList.clear();
						if (mIBeacon.getProximityUuid().toUpperCase()
								.equals(detectedPushUUID)) {
							if (mIBeacon.getAccuracy() > VALID_PUSH_DISTANCE) {
								return;
							}
						} else if (mIBeacon.getProximityUuid().toUpperCase()
								.equals(detectedBroadcastUUID)) {
							if (mIBeacon.getAccuracy() > VALID_BROADCAST_DISTANCE) {
								return;
							}
						} else {
							return;
						}

						IBEACON_STATE = IN_IBEACON;
						
						String str = constructParams(newMajorAndMinor,
								oldMajorAndMinor, proximityUUID);

						mIBeaconRequest.setRequestParam(str);

						String result = mIBeaconRequest.sendPost();
						praseResult(result);

					} else {	IBEACON_STATE = NONE_OR_ENTER_IBEACON;}
				}

				if (mContext != null) {
					if (!MyApplication.isAppOnForeground(mContext)) {

						if (iBeaconManager.isBound(mShopplayIBeaconService)) {
							iBeaconManager.setBackgroundMode(
									ShopplayIBeaconService.getInstance(), true);
						}
					} else {
						if (iBeaconManager.isBound(mShopplayIBeaconService)) {
							iBeaconManager.setBackgroundMode(
									ShopplayIBeaconService.getInstance(), false);
						}
					}
				}
				
				
			}
		});

		try {
			iBeaconManager.startRangingBeaconsInRegion(new Region(
					"myRangingUniqueId", null, null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start iBeacon service
	 * @param context
	 */
	public static void startBackService(Context context) {
		ShopplayIBeaconService.mContext = context;
		if (!ShopplayIBeaconService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			Log.i(TAG, "ShopplayIBeaconService begin to start...");
			Intent intent = new Intent(context, ShopplayIBeaconService.class);
			context.startService(intent);
		} else {

		}
	}

	/**
	 * Stop iBeacon service
	 * @param context
	 */
	public static void stopBackService(Context context) {
		if (ShopplayIBeaconService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			Intent intent = new Intent(context, ShopplayIBeaconService.class);
			context.stopService(intent);
		}
	}

	public static boolean isServiceExisted(Context context, Class service) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;
			if (serviceName.getClassName().equals(service.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Sort iBeacons by distance 
	 * @param validDistance
	 * @return
	 */
	public static List<IBeacon> getSortIbeacon(Collection<IBeacon> iBeacons) {
		if (iBeacons.size() > 0) {
			copy.clear();

			Iterator<IBeacon> it = iBeacons.iterator();
			while (it.hasNext()) {
				IBeacon beacon = (IBeacon) it.next();
				if (beacon.getProximityUuid().toUpperCase()
						.equals(detectedPushUUID)
						|| beacon.getProximityUuid().toUpperCase()
								.equals(detectedBroadcastUUID)) {
					copy.add(beacon);
				}
			}
			Collections.sort(copy, comparator);
		}
		return copy;
	}

	private static final Comparator<IBeacon> comparator = new Comparator<IBeacon>() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(IBeacon arg0, IBeacon arg1) {
			return Double.compare(arg0.getAccuracy(), arg1.getAccuracy());
		}
	};

	public void push() {
		if (mContext != null) {
			
			if (!MyApplication.isAppOnForeground(mContext)) {

				Log.i("State", "prepare to show push notification...");
				IBeaconNotification mIBeaconNotification = new IBeaconNotification(
						mContext);
				mIBeaconNotification.setPushTitle("PushTitle");
				mIBeaconNotification.setPushContent(mShopBeaconPush
						.getPushTitle() == null ? "" : mShopBeaconPush
						.getPushTitle());
				mIBeaconNotification.setmShopBeaconPush(mShopBeaconPush);
				mIBeaconNotification.clearNotification();

				mIBeaconNotification.showNotification();

			} else {
				Log.i("State", "prepare to show push dialog...");
				showPushDialog();
			}
		}
	}

	/**
	 * 
	 * 
	 * @param mNewMajorAndMinor
	 * @param mOldMajorAndMinor
	 * @param mProximityUuid
	 * @return
	 */
	public String constructParams(String mNewMajorAndMinor,
			String mOldMajorAndMinor, String mProximityUuid) {
//		String userID = rms.readString("UserID", "");
//		String UserKey = rms.readString("UserKey", "");
//		String Password = rms.readString("Password", "");
//		String DevicesId = rms.readString("DevicesId", "");
//
//		String str = "userID=" + userID + "&userPassword=" + Password
//				+ "&Appkey=" + UserKey + "&proximityUUID=" + mProximityUuid
//				+ "&oldMajorAndMinor=" + mOldMajorAndMinor
//				+ "&newMajorAndMinor=" + mNewMajorAndMinor + "&deviceType=1"
//				+ "&deviceID=" + DevicesId;
		return "";
	}

	/**
	 * 
	 * @param strResult
	 */
	public void praseResult(String strResult) {
		if ((IBeaconRequest.ConnectTimeOutErrorCode).equals(strResult)) {

		} else {
			
		}
	}

	/**
	 * �˴������ڵ������������AR���治����PushDialog
	 */
	public void showPushDialog() {

		if (!IBeaconPushActivity.isShowing ) {

			int mActivitySize = MyApplication.activityList
					.size();
			Activity mCurrentActivity = MyApplication.activityList
					.get(mActivitySize - 1);

			bCanPush = false;

			Log.i(TAG, "intent to pushActivity");
			Intent in = new Intent(ShopplayIBeaconService.this,
					IBeaconPushActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle mBundle = new Bundle();

			mBundle.putParcelable("ShopBeaconPush", mShopBeaconPush);
			in.putExtras(mBundle);
			mCurrentActivity.startActivity(in);
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	public class ShopplayIBeaconServiceBinder extends Binder {
		public ShopplayIBeaconService getService() {
			return ShopplayIBeaconService.this;
		}
	}
}
