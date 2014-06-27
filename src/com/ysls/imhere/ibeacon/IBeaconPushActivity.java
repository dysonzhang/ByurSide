package com.ysls.imhere.ibeacon;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.R;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Global;
import com.ysls.imhere.utils.AsyncImageLoader;
import com.ysls.imhere.utils.ImageCallback;
import com.ysls.imhere.utils.ToastUtil;
import com.ysls.ysls.sound.SoundManager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
	private ImageView iv_ibeacon_push_dialog_img;
	private ShopBeaconPush mShopBeaconPush;
	private AsyncImageLoader mAsyncImageLoader;

	public static boolean isShowing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ibeacon_push_content);

		mContext = this;
		isShowing = true;

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
		Global.isDetectingBeacon = true;
	}

	@Override
	public void onDestroy() {
		isShowing = false;
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
		iv_ibeacon_push_dialog_img = (ImageView) findViewById(R.id.rl_ibeacon_push_img);

		mAsyncImageLoader = new AsyncImageLoader();
		tv_ibeacon_push_dialog_subtitle.setText(mShopBeaconPush
				.getPushContent());
		
		mAsyncImageLoader.loadDrawable(mShopBeaconPush.getProPic(),
				iv_ibeacon_push_dialog_img, new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						if (imageDrawable != null) {
							iv_ibeacon_push_dialog_img
									.setBackgroundDrawable(imageDrawable);
						}
					}
				});

		iv_ibeacon_push_dialog_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showMsg(mContext, "You clicked checkin view");
				isShowing = false;
				defaultFinish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
