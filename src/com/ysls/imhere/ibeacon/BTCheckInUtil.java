package com.ysls.imhere.ibeacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ysls.imhere.MyApplication;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.utils.LogUtil;

public class BTCheckInUtil {
	
	public static ShopBeaconPush mShopBeaconPush;
	
	public static final String TAG = "BTCheckInUtil";

	public static void sentCheckInNotice(Context mContext) {
		if (mContext != null) {

			mShopBeaconPush = new ShopBeaconPush();

			mShopBeaconPush.setPushID(1L);
			mShopBeaconPush.setPushContent("push notification content");
			mShopBeaconPush.setPushTitle("push title");
			mShopBeaconPush
					.setBraLogo("http://www.shopplay.cn/Shop_Play_WEB//ImageResource/merlogo/holashopplaycn.jpg");
			mShopBeaconPush
					.setProPic("http://www.shopplay.cn//Shop_Play_WEB/ImageResource/product/1369886895209.jpg");

			if (!MyApplication.isAppOnForeground(mContext)
					&& !Global.isHavePushed) {

				LogUtil.i(TAG, "prepare to show push notification...");
				IBeaconNotification mIBeaconNotification = new IBeaconNotification(
						mContext);
				mIBeaconNotification.setPushTitle("PushTitle");
				mIBeaconNotification.setPushContent(mShopBeaconPush
						.getPushTitle() == null ? "PushContent"
						: mShopBeaconPush.getPushTitle());
				mIBeaconNotification.setmShopBeaconPush(mShopBeaconPush);

				mIBeaconNotification.clearNotification();
				mIBeaconNotification.showNotification();

				Global.isHavePushed = true;

			} else {
				LogUtil.i(TAG, "prepare to show push dialog...");
				showCheckOverlay(mContext);
			}
		}
	}

	/**
	 * PushDialog
	 */
	public static void showCheckOverlay(Context mContext) {
		if (!IBeaconPushActivity.isShowing) {

			int mActivitySize = MyApplication.activityList.size();

			if (mActivitySize > 0) {
				Activity mCurrentActivity = MyApplication.activityList
						.get(mActivitySize - 1);

				LogUtil.i(TAG, "intent to pushActivity");

				Intent in = new Intent(mContext, IBeaconPushActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle mBundle = new Bundle();
				mBundle.putParcelable("ShopBeaconPush", mShopBeaconPush);
				in.putExtras(mBundle);
				mCurrentActivity.startActivity(in);
			}
		}
	}
}
