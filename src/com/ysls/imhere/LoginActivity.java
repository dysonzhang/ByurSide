package com.ysls.imhere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.HanziToPinyin;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.db.UserDao;
import com.ysls.imhere.domain.User;
import com.ysls.imhere.utils.ShareSDKUtil;
import com.ysls.imhere.widget.TextURLView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Login Activity
 * 
 * @author dyson
 * 
 */
public class LoginActivity extends BaseActivity {

	private Context mContext;
	private RelativeLayout rl_user;

	private EditText account;
	private EditText pwd;

	private Button mLogin;
	private Button register;

	private TextURLView mTextViewURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;

		// 如果用户名密码都有，直接进入主页面
		if (MyApplication.getInstance().getUserName() != null
				&& MyApplication.getInstance().getPassword() != null) {
			openActivity(HomeActivity.class);
			defaultFinish();
		}

		findView();
		initTvUrl();
		initAnim();

	}

	/**
	 * Find View
	 */
	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);

		account = (EditText) findViewById(R.id.account);
		pwd = (EditText) findViewById(R.id.password);

		// 测试帐户
		account.setText("imhere");
		pwd.setText("123456");

		mLogin = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);

		mTextViewURL = (TextURLView) findViewById(R.id.tv_forget_password);

		// 如果用户名改变，清空密码
		account.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pwd.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * Login view animation
	 */
	private void initAnim() {
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
	}

	/**
	 * Forget Password
	 */
	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
		mTextViewURL.setOnClickListener(fogetOnClickListener);
	}

	private OnClickListener loginOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			login();
		}
	};

	private OnClickListener registerOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			openActivity(RegisterPhoneActivity.class);
		}
	};

	private OnClickListener fogetOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showLongToast("分享接口测试，你可以点击分享");
			// 测试
			ShareSDKUtil.showShare(mContext);
			
			openActivity(FindPasswordActivity.class);
		}
	};

	/**
	 * Login
	 * 
	 */
	private void login() {

		final String username = account.getText().toString();
		final String password = pwd.getText().toString();

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage("正在登录...");
			pd.show();
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(username, password,
					new EMCallBack() {

						@Override
						public void onSuccess() {
							// 登陆成功，保存用户名密码
							MyApplication.getInstance().setUserName(username);
							MyApplication.getInstance().setPassword(password);

							runOnUiThread(new Runnable() {
								public void run() {
									pd.setMessage("正在获取好友和群聊列表...");
								}
							});
							try {
								// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
								List<String> usernames = EMChatManager
										.getInstance().getContactUserNames();
								Map<String, User> userlist = new HashMap<String, User>();
								for (String username : usernames) {
									User user = new User();
									user.setUsername(username);
									setUserHearder(username, user);
									userlist.put(username, user);
								}
								// 添加user"新的朋友"
								User newFriends = new User();
								newFriends.setUsername(Constants.NEW_FRIENDS_USERNAME);
								newFriends.setNick("新的朋友");
								newFriends.setHeader("");
								userlist.put(Constants.NEW_FRIENDS_USERNAME,
										newFriends);
								// 添加"群聊"
								User groupUser = new User();
								groupUser.setUsername(Constants.GROUP_USERNAME);
								groupUser.setNick("群聊");
								groupUser.setHeader("");
								userlist.put(Constants.GROUP_USERNAME,
										groupUser);

								// 存入内存
								MyApplication.getInstance().setContactList(
										userlist);

								// 存入db
								UserDao dao = new UserDao(LoginActivity.this);
								List<User> users = new ArrayList<User>(userlist
										.values());
								dao.saveContactList(users);

								// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
								EMGroupManager.getInstance()
										.getGroupsFromServer();
							} catch (Exception e) {
							}

							if (!LoginActivity.this.isFinishing())
								pd.dismiss();
							// 进入主页面
							openActivity(HomeActivity.class);
							defaultFinish();
						}

						@Override
						public void onProgress(int progress, String status) {
						}

						@Override
						public void onError(int code, final String message) {
							runOnUiThread(new Runnable() {
								public void run() {
									pd.dismiss();
									showLongToast("登录失败: "
											+ message);
								}
							});
						}
					});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().getUserName() != null) {
			account.setText(MyApplication.getInstance().getUserName());
		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constants.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
