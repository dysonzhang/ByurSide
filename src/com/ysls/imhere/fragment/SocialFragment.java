package com.ysls.imhere.fragment;

import com.ysls.imhere.R;
import com.ysls.imhere.widget.ProgressWebView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("ValidFragment")
public class SocialFragment extends Fragment {
	private Activity mActivity;

	protected View view;
	private ProgressWebView webview;

	public SocialFragment() {
	}

	public SocialFragment(Activity paramActivity) {
		this.mActivity = paramActivity;
	}

	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle) {
		view = layoutInflater.inflate(R.layout.common_web, null);

		// 绑定控件
		webview = (ProgressWebView) view.findViewById(R.id.webview);


	    webview.loadUrl("http://a.m.taobao.com/i19226299687.htm?sid=7fabde721d3199f9&pds=fromtop%23h%23shop");
	
		return view;
	}
}