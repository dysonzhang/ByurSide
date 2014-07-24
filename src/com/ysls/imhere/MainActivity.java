package com.ysls.imhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.adapter.BasePageViewAdapter;
import com.ysls.imhere.base.BaseFragmentActivity;
import com.ysls.imhere.ibeacon.BluetoothController;
import com.ysls.imhere.indicator.PageIndicator;
import com.ysls.imhere.slidingdrawer.SemiClosedSlidingDrawer;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.utils.PopupWindowUtil;
import com.ysls.imhere.widget.ImageViewWithText;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主类Activity加载所有布局以及FragmentActivity
 * 
 * @author dyson
 * 
 */
public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {

	private static String TAG = "MainActivity";

	private Context mContext;

	private SemiClosedSlidingDrawer slidingDrawer;
	private boolean isHaveInit = false;
	private LinearLayout openLayout, closeLayout;

	private ImageViewWithText custom_checkin_history;
	private ImageViewWithText custom_publication;
	private ImageViewWithText custom_account_setting;
	private ImageViewWithText custom_about;
	private ImageViewWithText custom_feedback;
	private ImageViewWithText custom_exit;
	private ImageViewWithText custom_phonebook_backup;
	private ImageViewWithText custom_door_control;

	private Button bn_refresh;

	private ImageView imgLeft;
	private ImageView imgMore;
	private ImageView imgRight;

	private InputMethodManager imm;
	private boolean isShowPopupWindows = false;
	private int keyBackClickCount = 0;

	private LinearLayout llGoHome;
	private LinearLayout loadFaillayout;
	private LinearLayout loadLayout;

	private TextView mAboveTitle;
	private BasePageViewAdapter mBasePageViewAdapter;

	private PageIndicator mIndicator;

	public ViewPager mViewPager;
	private LinearLayout mlinear_listview;

	private View title;

	// 当前fragment的index
	private int currentTabIndex = 1;

	public static int HOMEFRAGMENT = 0;
	public static int TODOFRAGMENT = 1;
	public static int CHATFRAGMENT = 2;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_main);
		mContext = this;

		initClass();
		initControl();
		initViewPager();
		initBelowSlidingMenu();
		initBtContorller();

	}

	/**
	 * 初始化相关类
	 */
	private void initClass() {
		imm = ((InputMethodManager) getApplicationContext().getSystemService(
				"input_method"));
	}

	/**
	 * 初始化蓝牙接口
	 */
	private void initBtContorller() {
		BluetoothController.openBTAdapter();
		BluetoothController.getInstance(mContext).startBTCheckInService();
	}

	/**
	 * 加载界面所有布局控件
	 */
	private void initControl() {
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);

		mAboveTitle = (TextView) findViewById(R.id.tv_above_title);
		mAboveTitle.setText("imhere");
		mAboveTitle.setTextSize(22);

		imgMore = (ImageView) findViewById(R.id.imageview_above_more);
		imgMore.setOnClickListener(this);
		imgLeft = (ImageView) findViewById(R.id.imageview_above_left);
		imgRight = (ImageView) findViewById(R.id.imageview_above_right);

		mViewPager = (ViewPager) findViewById(R.id.above_pager);
		mIndicator = (PageIndicator) findViewById(R.id.above_indicator);
		llGoHome = (LinearLayout) findViewById(R.id.Linear_above_toHome);
		llGoHome.setOnClickListener(this);

		title = findViewById(R.id.main_title);
		mlinear_listview = (LinearLayout) findViewById(R.id.main_linear_listview);

		bn_refresh = ((Button) findViewById(R.id.bn_refresh));
		bn_refresh.setOnClickListener(this);

		slidingDrawer = (SemiClosedSlidingDrawer) findViewById(R.id.sd);
		openLayout = (LinearLayout) findViewById(R.id.open_header_layout);
		closeLayout = (LinearLayout) findViewById(R.id.close_header_layout);
	}

	/**
	 * 初始化底部界面抽屉Menu
	 */
	private void initBelowSlidingMenu() {
		slidingDrawer
				.setOnDrawerCloseListener(new SemiClosedSlidingDrawer.OnDrawerCloseListener() {
					public void onDrawerClosed() {
						openLayout.setVisibility(View.INVISIBLE);
						closeLayout.setVisibility(View.VISIBLE);
					}
				});

		slidingDrawer
				.setOnDrawerOpenListener(new SemiClosedSlidingDrawer.OnDrawerOpenListener() {
					public void onDrawerOpened() {
						openLayout.setVisibility(View.VISIBLE);
						closeLayout.setVisibility(View.INVISIBLE);

						if (!isHaveInit) {
							custom_checkin_history = (ImageViewWithText) findViewById(R.id.custom_checkin_history);
							custom_checkin_history
									.setOnClickListener(MainActivity.this);
							custom_publication = (ImageViewWithText) findViewById(R.id.custom_publication);
							custom_publication
									.setOnClickListener(MainActivity.this);
							custom_account_setting = (ImageViewWithText) findViewById(R.id.custom_account_setting);
							custom_account_setting
									.setOnClickListener(MainActivity.this);
							custom_about = (ImageViewWithText) findViewById(R.id.custom_about);
							custom_about.setOnClickListener(MainActivity.this);
							custom_feedback = (ImageViewWithText) findViewById(R.id.custom_feedback);
							custom_feedback
									.setOnClickListener(MainActivity.this);
							custom_exit = (ImageViewWithText) findViewById(R.id.custom_exit);
							custom_exit.setOnClickListener(MainActivity.this);
							custom_phonebook_backup = (ImageViewWithText) findViewById(R.id.custom_phonebook_backup);
							custom_phonebook_backup
									.setOnClickListener(MainActivity.this);
							custom_door_control = (ImageViewWithText) findViewById(R.id.custom_door_control);
							custom_door_control
									.setOnClickListener(MainActivity.this);

							isHaveInit = true;
						}
					}
				});

	}

	/**
	 * 初始化 主页，任务，沟通 滑动Menu菜单
	 */
	private void initViewPager() {

		mBasePageViewAdapter = new BasePageViewAdapter(this, mViewPager);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mBasePageViewAdapter);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(new MyPageChangeListener());

		imgLeft.setVisibility(View.GONE);
		imgRight.setVisibility(View.GONE);
		loadLayout.setVisibility(View.VISIBLE);

		mViewPager.setVisibility(View.GONE);
		mViewPager.removeAllViews();
		mBasePageViewAdapter.Clear();

		isShowPopupWindows = false;

		ArrayList<String> menuList = new ArrayList<String>();
		menuList.add("任务");
		menuList.add("主页");
		menuList.add("沟通");

		isShowPopupWindows = true;
		mBasePageViewAdapter.Clear();
		mViewPager.removeAllViews();

		if (!menuList.isEmpty()) {
			mBasePageViewAdapter.addFragment(menuList);
			// imgRight.setVisibility(View.VISIBLE);
			loadLayout.setVisibility(View.GONE);
			loadFaillayout.setVisibility(View.GONE);
		} else {
			mBasePageViewAdapter.addNullFragment();
			loadLayout.setVisibility(View.GONE);
			loadFaillayout.setVisibility(View.VISIBLE);
		}

		mViewPager.setVisibility(View.VISIBLE);
		mBasePageViewAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(1);
		mIndicator.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.Linear_above_toHome:
			// showLongToast("你点击了LOGO");
			break;
		case R.id.imageview_above_more:
			if (isShowPopupWindows) {
				new PopupWindowUtil(mViewPager).showActionWindow(paramView,
						this, mBasePageViewAdapter.tabs);
			}
			break;
		case R.id.custom_checkin_history:
			startActivity(new Intent(mContext, CheckinHistoryActivity.class));
			MainActivity.this.overridePendingTransition(R.anim.activity_up,
					R.anim.fade_out);
			break;
		case R.id.custom_publication:
			startActivity(new Intent(mContext, PublicationActivity.class));
			MainActivity.this.overridePendingTransition(R.anim.activity_up,
					R.anim.fade_out);
			break;
		case R.id.custom_account_setting:
			startActivity(new Intent(mContext, SettingsActivity.class));
			MainActivity.this.overridePendingTransition(R.anim.activity_up,
					R.anim.fade_out);
			break;
		case R.id.custom_feedback:
			startActivity(new Intent(mContext, FeedbackActivity.class));
			MainActivity.this.overridePendingTransition(R.anim.activity_up,
					R.anim.fade_out);
			break;
		case R.id.custom_about:
			startActivity(new Intent(mContext, AboutActivity.class));
			MainActivity.this.overridePendingTransition(R.anim.activity_up,
					R.anim.fade_out);
			break;

		case R.id.custom_exit:
			showLongToast("服务尚未开通，敬请期待！");
			break;
		case R.id.custom_phonebook_backup:
			showLongToast("服务尚未开通，敬请期待！");
			break;
		case R.id.custom_door_control:
			showLongToast("服务尚未开通，敬请期待！");
			break;
		case R.id.bn_refresh:
			showLongToast("You clicked my refresh button!");
			break;
		}
	}

	protected void onDestroy() {
		super.onDestroy();

		try {
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				MyApplication.exitApp();
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (slidingDrawer.isOpened()) {
				slidingDrawer.toggle();
			} else {
				slidingDrawer.showContextMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
		this.keyBackClickCount = 0;
	}

	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
	}

	class MyPageChangeListener implements ViewPager.OnPageChangeListener {
		public void onPageScrollStateChanged(int paramInt) {
		}

		public void onPageScrolled(int paramInt1, float paramFloat,
				int paramInt2) {
		}

		public void onPageSelected(int selectId) {
			if (selectId == 0) {
				imgLeft.setVisibility(View.GONE);
				LogUtil.i(MainActivity.TAG, "任务");
				currentTabIndex = TODOFRAGMENT;

			} else if (selectId == mBasePageViewAdapter.mFragments.size() - 1) {
				imgRight.setVisibility(View.GONE);
				LogUtil.i(MainActivity.TAG, "主页");
				currentTabIndex = HOMEFRAGMENT;
			} else {
				// imgRight.setVisibility(View.VISIBLE);
				// imgLeft.setVisibility(View.VISIBLE);
				LogUtil.i(MainActivity.TAG, "沟通");
				currentTabIndex = CHATFRAGMENT;
			}
		}
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
	}
}