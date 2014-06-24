package com.ysls.imhere.ibeacon;

import com.ysls.imhere.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class IBeaconNotification {
	private NotificationManager mNM;
	private final String NOTIFICATION_SERVICE = "notification";
	private Context mContext;
	private String pushContent;
	private String pushTitle;
	private ShopBeaconPush mShopBeaconPush;

	public ShopBeaconPush getmShopBeaconPush() {
		return mShopBeaconPush;
	}

	public void setmShopBeaconPush(ShopBeaconPush mShopBeaconPush) {
		this.mShopBeaconPush = mShopBeaconPush;
	}

	public String getPushContent() {
		return pushContent;
	}

	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}

	public String getPushTitle() {
		return pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	public IBeaconNotification(Context mContext) {
		mNM = (NotificationManager) mContext
				.getSystemService(NOTIFICATION_SERVICE);
		this.mContext = mContext;
	}

	public void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		// CharSequence text = getText(R.string.remote_service_started);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_launcher,
				pushContent, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		Intent actClick = new Intent(
				IBeaconPushReceiver.IBEACON_PUSH_RECEVIER_ACTION);
		Bundle mBundle = new Bundle();
		mBundle.putParcelable("mShopBeaconPush", mShopBeaconPush);
		actClick.putExtras(mBundle);
		actClick.setAction(IBeaconPushReceiver.IBEACON_PUSH_RECEVIER_ACTION);
		PendingIntent contentIntent = PendingIntent.getBroadcast(mContext, 0,
				actClick, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(mContext, pushTitle, pushContent,
				contentIntent);

		notification.sound = Uri.parse("android.resource://"
				+ mContext.getPackageName() + "/" + R.raw.notification);

		// FLAG_AUTO_CANCEL
		// FLAG_NO_CLEAR
		// FLAG_ONGOING_EVENT
		// FLAG_INSISTENT

		notification.flags = Notification.FLAG_AUTO_CANCEL;
		// Send the notification.
		// We use a string id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(0, notification);
	}

	//
	public void clearNotification() {

		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
}
