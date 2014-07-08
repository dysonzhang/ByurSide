package com.ysls.imhere.config;

public class Constants {
	/**
	 * 分钟
	 */
	public static int Content_ListCacheTime = 5;
	public static int Content_ContentCacheTime = 60 * 24 * 3;
	public static int ImageCacheTime = 60 * 24 * 15;
	public static int Content_DefaultCacheTime = 60 * 24 * 3;
	
	public static int DiscussCacheTime=60;
	
	/**
	 * ShareSdk AppKey
	 */
	public static String ShareSdk_Appkey = "21490f8034f8";
	/**
	 * Bluetooth check in mode
	 */
	public static int BT_GENREAL_CHECKIN_MODE = 0;
	public static int BT_IBEACON_CHECKIN_MODE = 1;
	public static int BT_NULL_CHECKIN_MODE = 2;
	
	/**
	 * MAC address filter "60:36:DD:3A:6D:CF"
	 */
	public static final String DEVICE_MAC = "60:36:DD:3A:6D:CF";
	public static final int BT_GENREAL_SCAN_TIME = 3000;
	/**
	 * UUID filter
	 */
	public static final String detectedPushUUID = "25275B5C-0210-4DD5-B290-552B0AFBD80C";
	/**
	 * UUID filter
	 */
	public static final String detectedBroadcastUUID = "25275B5C-0210-4DD5-B290-552B0AFBD80D";
	/**
	 * UUID filter
	 */
	public static final String proximityUUID = "";
	
	/**
	 * Easemod chat
	 */
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	
}
