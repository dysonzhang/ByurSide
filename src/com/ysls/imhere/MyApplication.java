package com.ysls.imhere;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import com.ysls.imhere.baidu.BaiduLocation;

public class MyApplication extends Application {
	
	public static Context ctx;

	public void onCreate() {
		super.onCreate();
		
		ctx = getApplicationContext();
		BaiduLocation.getBaiduLocationInstance(ctx).sendLocationReq();
		
		JPushInterface.setDebugMode(true);
		JPushInterface.init(ctx);
	}
}