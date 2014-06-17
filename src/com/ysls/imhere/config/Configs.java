package com.ysls.imhere.config;

public class Configs {
	public static int Content_ContentCacheTime;
	public static int Content_DefaultCacheTime;
	public static int Content_ListCacheTime = 5;
	public static int DiscussCacheTime;
	public static int ImageCacheTime;

	static {
		Content_ContentCacheTime = 4320;
		ImageCacheTime = 21600;
		Content_DefaultCacheTime = 4320;
		DiscussCacheTime = 60;
	}
}