package com.ysls.imhere.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showMsg(Context paramContext, String paramString) {
		new Toast(paramContext);
		Toast localToast = Toast.makeText(paramContext, paramString, 1);
		localToast.setGravity(1, 0, 0);
		localToast.show();
	}
}