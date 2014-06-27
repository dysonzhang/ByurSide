package com.ysls.imhere.ibeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.ysls.imhere.MyApplication;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.utils.LogUtil;

public class ShopplayIBeaconService extends Service implements IBeaconConsumer {

	public static final String TAG = "ShopplayIBeaconService";

	private IBeaconManager iBeaconManager = IBeaconManager
			.getInstanceForApplication(this);

	private static ShopplayIBeaconService mShopplayIBeaconService;

	public static Context mContext;

	public static List<IBeacon> copy = new ArrayList<IBeacon>();

	public static final int NO_ENTER_IBEACON = 0;
	public static final int IN_IBEACON = 1;
	public static int IBEACON_STATE = -1;

	private IBeaconRequest mIBeaconRequest;

	public ShopBeaconPush mShopBeaconPush;
	/**
	 * VALID_PUSH_DISTANCE
	 */
	private static double VALID_PUSH_DISTANCE = 50;
	/**
	 * VALID_BROADCAST_DISTANCE
	 */
	private static double VALID_BROADCAST_DISTANCE = 50;

	private IBeaconUserInfo mIBeaconUserInfo = new IBeaconUserInfo();

	public static ShopplayIBeaconService getInstance() {
		if (mShopplayIBeaconService == null)
			mShopplayIBeaconService = new ShopplayIBeaconService();
		return mShopplayIBeaconService;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;

		mIBeaconRequest = new IBeaconRequest();
		mIBeaconRequest.setConnectTimeout(10000);
		mIBeaconRequest.setReadTimeout(10000);
		mIBeaconRequest.setRequestUrl(IBeaconRequest.iBeaconPushUrl);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		iBeaconManager.bind(this);
		iBeaconManager.setBackgroundScanPeriod(1100);
		iBeaconManager.setBackgroundBetweenScanPeriod(0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
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

				LogUtil.i(TAG,
						"mIBeaconNearList is ---->" + mIBeaconNearList.size());

				if (Global.isDetectingBeacon) {

					if (mIBeaconNearList.size() > 0) {

						IBEACON_STATE = IN_IBEACON;
						LogUtil.i(TAG, "IN_IBEACON");
						IBeacon mIBeacon = mIBeaconNearList.get(0);
						mIBeaconNearList.clear();
						if (mIBeacon.getProximityUuid().toUpperCase()
								.equals(Constants.detectedPushUUID)) {
							if (mIBeacon.getAccuracy() > VALID_PUSH_DISTANCE) {
								return;
							}
						} else if (mIBeacon.getProximityUuid().toUpperCase()
								.equals(Constants.detectedBroadcastUUID)) {
							if (mIBeacon.getAccuracy() > VALID_BROADCAST_DISTANCE) {
								return;
							}
						} else {
							return;
						}
						/*
						 * String str = constructParams(" ", " ",
						 * proximityUUID); mIBeaconRequest.setRequestParam(str);
						 * String result = mIBeaconRequest.sendPost();
						 * praseResult(result);
						 */

						BTCheckInUtil.sendCheckInNotice(mContext);

					} else {
						IBEACON_STATE = NO_ENTER_IBEACON;
						LogUtil.i(TAG, "NO_ENTER_IBEACON");
					}

					mIBeaconNearList.clear();
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
					"ImhereRangingUniqueId", null, null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start iBeacon service
	 * 
	 * @param context
	 */
	public static void startBackService(Context context) {
		ShopplayIBeaconService.mContext = context;
		if (!ShopplayIBeaconService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			LogUtil.i(TAG, "ShopplayIBeaconService begin to start...");
			Intent intent = new Intent(context, ShopplayIBeaconService.class);
			context.startService(intent);
		} else {

		}
	}

	/**
	 * Stop iBeacon service
	 * 
	 * @param context
	 */
	public static void stopBackService(Context context) {
		if (ShopplayIBeaconService.isServiceExisted(context,
				ShopplayIBeaconService.class)) {
			LogUtil.i(TAG, "ShopplayIBeaconService begin to stop...");
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
	 * 
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
						.equals(Constants.detectedPushUUID)
						|| beacon.getProximityUuid().toUpperCase()
								.equals(Constants.detectedBroadcastUUID)) {
					copy.add(beacon);
				}
			}
			Collections.sort(copy, comparator);
		}
		return copy;
	}

	private static final Comparator<IBeacon> comparator = new Comparator<IBeacon>() {
		@Override
		public int compare(IBeacon arg0, IBeacon arg1) {
			return Double.compare(arg0.getAccuracy(), arg1.getAccuracy());
		}
	};

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
		// String userID = rms.readString("UserID", "");
		// String UserKey = rms.readString("UserKey", "");
		// String Password = rms.readString("Password", "");
		// String DevicesId = rms.readString("DevicesId", "");
		//
		// String str = "userID=" + userID + "&userPassword=" + Password
		// + "&Appkey=" + UserKey + "&proximityUUID=" + mProximityUuid
		// + "&oldMajorAndMinor=" + mOldMajorAndMinor
		// + "&newMajorAndMinor=" + mNewMajorAndMinor + "&deviceType=1"
		// + "&deviceID=" + DevicesId;
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
