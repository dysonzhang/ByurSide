package com.ysls.imhere;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.Request;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.litesuits.http.response.Response;
import com.litesuits.http.response.handler.HttpModelHandler;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.task.param.Man;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.widget.TextURLView;
import com.ysls.imhere.widget.TitleBarView;

public class RegisterPhoneActivity extends BaseActivity {

	protected String TAG = "RegisterPhoneActivity";

	private Context mContext;
	private TitleBarView mTitleBarView;
	private TextURLView mTextViewURL;
	private Button next;
	private Button sendPhoneVerifyCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		mContext = this;

		findView();
		initTitleView();
		initTvUrl();
		init();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTextViewURL = (TextURLView) findViewById(R.id.tv_url);
		next = (Button) findViewById(R.id.btn_next);
		sendPhoneVerifyCode = (Button) findViewById(R.id.btn_send_phoneVerifyCode);
	}

	private void init() {
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Man man = new Man("dyson.zhang@shopplay.cn",
						"9774fcb5b47274e44a41ba252ff613");
				refreshUI(
						"http://115.29.244.59/Shop_Play_API/v1/api/user/login",
						man, HttpMethod.Post);
				openActivity(RegisterInfoActivity.class);
			}
		});
		sendPhoneVerifyCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLongToast("你点击了 发送验证码");
			}
		});
		mTextViewURL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global.WebView_URL = "http://a.m.taobao.com/i17166586737.htm?sid=7fabde721d3199f9&show_id=1&pds=fromtop%23h%23shop";
				openActivity(WebViewActivity.class);
				RegisterPhoneActivity.this.overridePendingTransition(
						R.anim.activity_up, R.anim.fade_out);
			}
		});
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setTitleText(R.string.title_phoneNumber);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.tv_xieyi_url);
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {

		Request req = new Request(taskApiURL, httpParam).setMethod(httpMethod);

		asyncExcutor.execute(client, req, new HttpModelHandler<String>() {
			@Override
			protected void onSuccess(String data, Response res) {
				showLongToast("LiteHttp请求成功");
				LogUtil.i(TAG, "User String: " + data);
			}

			@Override
			protected void onFailure(HttpException e, Response res) {
				showLongToast("e: " + e);
			}

		});
		/*
		 * asyncExcutor.execute(client, req, new
		 * HttpModelHandler<ArrayList<User>>() {
		 * 
		 * @Override protected void onSuccess(ArrayList<User> data, Response
		 * response) {
		 * 
		 * ToastUtil.showMsg(mContext, "成功"); // User user2 =
		 * response.getObject(User.class);
		 * ToastUtil.showMsg(mContext,"User List: " + data); }
		 * 
		 * @Override public void onFailure(HttpException e, Response response) {
		 * 
		 * new HttpExceptionHandler() {
		 * 
		 * @Override protected void onClientException( HttpClientException e,
		 * ClientException type) { ToastUtil.showMsg(mContext,
		 * "开发者可更新界面提示用户，原因：客户端有异常"); }
		 * 
		 * @Override protected void onNetException(HttpNetException e,
		 * NetException type) { if (type == NetException.NetworkError) {
		 * ToastUtil.showMsg(mContext, "开发者可更新界面提示用户，原因：无可用网络"); } else if (type
		 * == NetException.UnReachable) { ToastUtil.showMsg(mContext,
		 * "开发者可更新界面提示用户，原因：服务器不可访问(或网络不稳定)"); } else if (type ==
		 * NetException.NetworkDisabled) { ToastUtil.showMsg(mContext,
		 * "原因：该网络类型已被开发者设置禁用"); } }
		 * 
		 * @Override protected void onServerException( HttpServerException e,
		 * ServerException type, HttpStatus status) {
		 * ToastUtil.showMsg(mContext, "开发者可更新界面提示用户，原因：服务暂时不可用"); }
		 * 
		 * }.handleException(e);
		 * 
		 * LogUtil.i(TAG, response.getString()); } });
		 */
	}
}
