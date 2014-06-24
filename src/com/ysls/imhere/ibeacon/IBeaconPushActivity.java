package com.ysls.imhere.ibeacon;


import com.ysls.imhere.R;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Golbal;
import com.ysls.imhere.utils.AsyncImageLoader;
import com.ysls.imhere.utils.ImageCallback;
import com.ysls.imhere.widget.CustomProgressDialog;
import com.ysls.imhere.widget.ImgLoadView;
import com.ysls.ysls.sound.SoundManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class IBeaconPushActivity extends BaseActivity {
	private static String TAG = "IBeaconPushActivity";
	private Context mContext;
	private TextView tv_ibeacon_push_dialog_subtitle;
	private ImgLoadView iv_ibeacon_push_dialog_img;
	private ImageView iv_ibeacon_push_dialog_logo;
	private ShopBeaconPush mShopBeaconPush;
	private AsyncImageLoader mAsyncImageLoader;

	// private ShopBeaconReward mShopBeaconReward;
	private CustomProgressDialog mProgressDialog;

	public static boolean isShowing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ibeacon_push_content);
		mContext = this;
		isShowing = true;
		Log.i("IBeaconPushActivity", "isShowing --->" + isShowing);
		if (this.getIntent() != null) {
			mShopBeaconPush = getIntent().getParcelableExtra("ShopBeaconPush");
		}

		initView();

		SoundManager sm = new SoundManager();
		sm.play(IBeaconPushActivity.this, R.raw.modern_alert);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Golbal.isDetectingBeacon = false;
	}

	@Override
	public void onDestroy() {
		isShowing = false;
		Log.i("IBeaconPushActivity", "isShowing --->" + isShowing);
		super.onDestroy();
	}

	/**
	 * 
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		View view = getWindow().getDecorView();
		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view
				.getLayoutParams();
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		lp.x = 1;
		lp.y = 1;
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = (int) (getScrrenSize().getHeight()); // * 0.55
		getWindowManager().updateViewLayout(view, lp);
	}

	public Display getScrrenSize() {
		return ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
	}

	public void initView() {
		tv_ibeacon_push_dialog_subtitle = (TextView) findViewById(R.id.tv_ibeacon_push_content_info);
		iv_ibeacon_push_dialog_img = (ImgLoadView) findViewById(R.id.rl_ibeacon_push_img);

		iv_ibeacon_push_dialog_logo = (ImageView) findViewById(R.id.iv_ibeacon_push_logo);

		mAsyncImageLoader = new AsyncImageLoader();
		tv_ibeacon_push_dialog_subtitle.setText(mShopBeaconPush
				.getPushContent());
		mAsyncImageLoader.loadDrawable(mShopBeaconPush.getMerLogo(),
				iv_ibeacon_push_dialog_logo, new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						// TODO Auto-generated method stub
						if (imageDrawable != null) {
							iv_ibeacon_push_dialog_logo
									.setBackgroundDrawable(imageDrawable);
						}
					}
				});

		Drawable d = mAsyncImageLoader.loadDrawable(
				mShopBeaconPush.getProPic(), iv_ibeacon_push_dialog_logo,
				new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						// TODO Auto-generated method stub
						if (imageDrawable != null) {
							iv_ibeacon_push_dialog_img
									.setDrawable(imageDrawable);
						}
					}
				});

		
		iv_ibeacon_push_dialog_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mShopBeaconPush.getIsHavePushReward() == 1) {

					mProgressDialog = new CustomProgressDialog(mContext,
							R.style.custom_progress_dlg);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.show();

					sendRequest();

				} else {
					// TranlateToProductCardActivity();
				}
			}
		});

	}

	// @Override
	// public void refresh(Object... param) {
	// if (param[0].equals(TaskType.REF_TASK_SHOP_BEACON_REWARD_SERVICE)) {
	// if (param[1] instanceof JsonShopBeaconRewardList) {
	// JsonShopBeaconRewardList list1 = (JsonShopBeaconRewardList) param[1];
	// mProgressDialog.dismiss();
	// if (list1.getFeedback().getState() == 1) { // ok
	// mShopBeaconReward = list1.getResult().get(0);
	// switch (mShopBeaconReward.getRewardState()) {
	// case 1:
	// // No reward
	// TranlateToProductCardActivity();
	// break;
	// case 2:
	// Global.isDetectingBeacon = false;
	//
	// // get reward point of reward, and show the green ball
	// UserDAO dao = new UserDAO(this);
	// TuUserMdl user = dao.findByAll();
	//
	// user.setPickbeanNumNow(mShopBeaconReward
	// .getPickbeanNumNow());
	// user.setUserEmp(mShopBeaconReward.getUserEmp());
	// user.setBeanNumIn(mShopBeaconReward.getBeanNumIn());
	// user.setUserLevel(mShopBeaconReward.getUserLevel());
	//
	// dao.updateUser(user);
	//
	// sendRefreshDataBroadcast();
	// isShowing = false;
	// TranlateToPushAnimActivity();
	// break;
	// case 3:
	// // during time limited
	// TranlateToProductCardActivity();
	// break;
	// case 4:
	// // have get the reward before
	// TranlateToProductCardActivity();
	// break;
	// }
	//
	// } else if (list1.getFeedback().getState() == 0) {
	//
	// } else if (list1.getFeedback().getState() == 2) {
	// Log.i(TAG, "�û���ϢuserID��userPassword��AppkeyΪnull���");
	// } else if (list1.getFeedback().getState() == 3) {
	// Log.i(TAG, "pushIDΪnull���");
	// } else if (list1.getFeedback().getState() == 4) {
	// Log.i(TAG, "��γ��Ϊnull���");
	// } else if (list1.getFeedback().getState() == 5) {
	// Log.i(TAG, "���ʻ��ѱ�����ʹ��");
	// } else if (list1.getFeedback().getState() == 6) {
	// Log.i(TAG, "û�и��û���Ϣ");
	// } else if (list1.getFeedback().getState() == 7) {
	// Log.i(TAG, "û�и����ͼ�¼��Ϣ");
	// } else if (list1.getFeedback().getState() == 8) {
	// Log.i(TAG, "���û������ŵ����1000��");
	// TranlateToProductCardActivity();
	// } else if (list1.getFeedback().getState() == 9) {
	// Log.i(TAG, "������û�н���");
	// TranlateToProductCardActivity();
	// } else if (list1.getFeedback().getState() == 10) {
	// Log.i(TAG, "���豸�Ѿ���ȡ�����ͽ���");
	// TranlateToProductCardActivity();
	// } else if (list1.getFeedback().getState() == 11) {
	// Log.i(TAG, "�û���ȡ���ͽ���ʱ��δ��");
	// TranlateToProductCardActivity();
	// }
	// }
	// }
	// }

	/**
	 * sendRequest
	 */
	public void sendRequest() {

		// WeakHashMap<String, String> hm = new WeakHashMap<String, String>();
		// hm.put("userID", Global.userID);
		// hm.put("userPassword", Global.password);
		// hm.put("Appkey", Global.userKey);
		//
		// hm.put("pushID", mShopBeaconPush.getPushID() + "");
		// hm.put("nowlng", Global.lon);
		// hm.put("nowlat", Global.lat);
		// hm.put("deviceID", Global.deviceID);
		//
		// Task ShopBeaconRewardServiceTask = new Task(
		// TaskType.SHOP_BEACON_REWARD_SERVICE, hm);
		// StartShopPlayService.addNewTask(ShopBeaconRewardServiceTask);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	public void TranlateToPushAnimActivity() {

		// Intent it = new Intent(IBeaconPushActivity.this,
		// ShopplayPushAnimActivity.class);
		//
		// it.putExtra("braId", mShopBeaconReward.getBraId().toString());
		// it.putExtra("braname", mShopBeaconReward.getBraName());
		// it.putExtra("bralogo", mShopBeaconReward.getBraLogo());
		//
		// it.putExtra("CheckInWandou", mShopBeaconReward.getRewardPoint()
		// .toString());
		//
		// it.putExtra("merId", mShopBeaconReward.getMerId().toString());
		// it.putExtra("mername", mShopBeaconReward.getMerName());
		// it.putExtra("merlogo", mShopBeaconReward.getMerLogo());
		//
		// it.putExtra("proId", mShopBeaconReward.getProId().toString());
		//
		// // �ж�SPNearby
		// it.putExtra("ishaveCheckin",
		// mShopBeaconReward.getIsHaveCheckin() == 0 ? false : true);
		// it.putExtra("ishaveScan",
		// mShopBeaconReward.getIsHaveScan() == 0 ? false : true);
		// it.putExtra("ishaveSuprise",
		// mShopBeaconReward.getIsHaveSuprise() == 0 ? false : true);
		// it.putExtra("ishavePushReward",
		// mShopBeaconReward.getBraIsHavePushReward() == 0 ? false : true);
		//
		// startActivity(it);
		//
		this.finish();
	}

	/**
	 *
	 */
	public void sendRefreshDataBroadcast() {

		Intent intent = new Intent();
		intent.setAction("com.justel.service");
		intent.putExtra("refresh", true);
		sendBroadcast(intent);
	}
}
