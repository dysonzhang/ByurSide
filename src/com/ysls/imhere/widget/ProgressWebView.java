package com.ysls.imhere.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * 
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	
	private WebSettings webSettings;
	private ProgressBar progressbar;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		webSettings = this.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				3, 0, 0));
		addView(progressbar);
		
		this.setDownloadListener(new MyDownloadListener());
		this.setWebViewClient(new MyWebViewClient());
		
		this.setWebChromeClient(new WebChromeClient());
	}
	/*
	 * @see android.webkit.WebView#loadUrl(java.lang.String)
	 * 重写loadUrl,使地址栏及时更新地址
	 */
	@Override
	public void loadUrl(String url) {
		// TODO Auto-generated method stub
		super.loadUrl(url);
	}

	/**
	 * @describe 清除缓存
	 * @parameter
	 * @return void
	 */
	public void ClearAllCache() {
		this.clearHistory();
		this.clearCache(true);
		this.destroy();
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			result.confirm();
			return super.onJsAlert(view, url, message, result);
		}
		
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
	class MyWebViewClient extends WebViewClient {

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		}
	}

	class MyDownloadListener implements DownloadListener {

		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
		}

	}
}