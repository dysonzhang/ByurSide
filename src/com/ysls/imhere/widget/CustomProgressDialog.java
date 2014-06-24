package com.ysls.imhere.widget;


import com.ysls.imhere.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomProgressDialog extends ProgressDialog {
	 TextView dlgMessage = null;
	 String strMessage = null;
	public CustomProgressDialog(Context context) {
		super(context);
	}
	public CustomProgressDialog(Context context,String strMessage) {
		super(context);
		this.strMessage = strMessage;
	}
	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_progress_dlg);
		dlgMessage = (TextView) findViewById(R.id.custom_progress_dlg_message);
		if(strMessage != null){
			dlgMessage.setVisibility(View.VISIBLE);
			dlgMessage.setText(strMessage);
		}
	}
	
	public  void setMessage(int resID){
		if(dlgMessage != null){
			dlgMessage.setVisibility(View.VISIBLE);
			dlgMessage.setText(resID);
		}
	}
	
	public  void setMessage(String strMsg){
		if(dlgMessage != null){
			dlgMessage.setVisibility(View.VISIBLE);
			dlgMessage.setText(strMsg);
		}
	}

	public static CustomProgressDialog show(Context context) {
		CustomProgressDialog dialog = new CustomProgressDialog(context,R.style.custom_progress_dlg);
		dialog.show();
		return dialog;
	}
}
