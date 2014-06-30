package com.ysls.imhere.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;

import com.easemob.util.HanziToPinyin;

import com.ysls.imhere.GroupsActivity;
import com.ysls.imhere.HomeActivity;
import com.ysls.imhere.MyApplication;
import com.ysls.imhere.R;
import com.ysls.imhere.config.Constants;
import com.ysls.imhere.db.InviteMessgeDao;
import com.ysls.imhere.db.UserDao;
import com.ysls.imhere.domain.InviteMessage;
import com.ysls.imhere.domain.InviteMessage.InviteMesageStatus;
import com.ysls.imhere.domain.User;
import com.ysls.imhere.utils.CommonUtil;
import com.ysls.imhere.widget.TitleBarView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsFatherFragment extends Fragment {

	private static final String TAG = "NewsFatherFragment";
	private Context mContext;

	private View mBaseView;
	private TitleBarView mTitleBarView;

	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;

	private RelativeLayout mCanversLayout;

	// 当前fragment的index
	private int currentTabIndex = 0;

	private NewMessageBroadcastReceiver msgReceiver;

	private ContactlistFragment contactListFragment;
	private ChatHistoryFragment chatHistoryFragment;

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;
	// 账号在别处登录
	private boolean isConflict = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();

		mBaseView = inflater.inflate(R.layout.fragment_news_father, null);

		initClass();
		findView();
		init();
		regChatReceiver();

		return mBaseView;
	}

	private void initClass() {
		inviteMessgeDao = new InviteMessgeDao(mContext);
		userDao = new UserDao(mContext);

		chatHistoryFragment = new ChatHistoryFragment();
		contactListFragment = new ContactlistFragment();
	}

	private void regChatReceiver() {
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		mContext.registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		mContext.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mCanversLayout = (RelativeLayout) mBaseView
				.findViewById(R.id.rl_canvers);
		unreadLabel = (TextView) mBaseView.findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) mBaseView.findViewById(R.id.unread_address_number);
	}

	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);

		mTitleBarView.setTitleLeft(R.string.cnews);
		mTitleBarView.setTitleRight(R.string.contacter);

		mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleLeft().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(false);
					mTitleBarView.getTitleRight().setEnabled(true);

					currentTabIndex = 0;

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();

					ft.replace(R.id.child_fragment, chatHistoryFragment,
							NewsFatherFragment.TAG);
					// ft.addToBackStack(TAG);
					ft.commit();
				}
			}
		});

		mTitleBarView.getTitleRight().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleRight().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(true);
					mTitleBarView.getTitleRight().setEnabled(false);

					currentTabIndex = 0;

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();

					ft.replace(R.id.child_fragment, contactListFragment,
							NewsFatherFragment.TAG);
					ft.setCustomAnimations(R.anim.activity_up,
							R.anim.activity_down);
					ft.commit();
				}
			}
		});
		mTitleBarView.getTitleLeft().performClick();
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			mContext.unregisterReceiver(msgReceiver);
			mContext.unregisterReceiver(ackMessageReceiver);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新新的朋友消息数
	 */
	public void updateUnreadAddressLable() {

		int count = getUnreadAddressCountTotal();
		if (count > 0) {
			unreadAddressLable.setText(String.valueOf(count));
			unreadAddressLable.setVisibility(View.VISIBLE);
		} else {
			unreadAddressLable.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * 获取未读新的朋友消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (MyApplication.getInstance().getContactList()
				.get(Constants.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = MyApplication.getInstance()
					.getContactList().get(Constants.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/***
	 * 联系人变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = MyApplication.getInstance()
					.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);
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
							.get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 删除联系人
			Map<String, User> localUsers = MyApplication.getInstance()
					.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			// 刷新ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();
			updateUnreadLabel();

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现

		}

	}

	protected void notifyNewIviteMessage(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = MyApplication.getInstance().getContactList()
				.get(Constants.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
		// 提示有新消息
		EMNotifier.getInstance(mContext.getApplicationContext())
				.notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		if (currentTabIndex == 1)
			contactListFragment.refresh();
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆dialog
				showConflictDialog();
			} else {
				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
				chatHistoryFragment.errorText.setText("连接不到聊天服务器");
			}
		}

		@Override
		public void onReConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(mContext.getApplicationContext())
					.notifyOnNewMsg();

			// runOnUiThread(new Runnable() {
			// public void run() {
			// updateUnreadLabel();
			// // 刷新ui
			// if (currentTabIndex == 0)
			// chatHistoryFragment.refresh();
			// // if
			// //
			// (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName()))
			// // {
			// // GroupsActivity.instance.onResume();
			// // }
			// }
			// });

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			// runOnUiThread(new Runnable() {
			// public void run() {
			try {
				updateUnreadLabel();
				if (currentTabIndex == 0)
					chatHistoryFragment.refresh();
				if (CommonUtil.getTopActivity(mContext).equals(
						GroupsActivity.class.getName())) {
					GroupsActivity.instance.onResume();
				}
			} catch (Exception e) {
				Log.e("###", "refresh exception " + e.getMessage());
			}

			// }
			// });
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			// runOnUiThread(new Runnable() {
			// public void run() {
			updateUnreadLabel();
			if (currentTabIndex == 0)
				chatHistoryFragment.refresh();
			if

			(CommonUtil.getTopActivity(mContext).equals(
					GroupsActivity.class.getName())) {
				GroupsActivity.instance.onResume();
			}
			// }
			// });

		}

	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// if (!isConflict) {
	// updateUnreadLabel();
	// updateUnreadAddressLable();
	// }
	//
	// }

	private android.app.AlertDialog.Builder conflictBuilder;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {

		MyApplication.getInstance().logout();

//		if (!HomeActivity.class.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							mContext);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								// finish();
								// startActivity(new Intent(MainActivity.this,
								// LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###",
						"---------color conflictBuilder error" + e.getMessage());
			}

//		}

	}
}
