package com.ysls.imhere;

import android.content.Context;
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

import com.ysls.imhere.adapter.BasePageViewAdapter;
import com.ysls.imhere.base.BaseFragmentActivity;
import com.ysls.imhere.db.DBHelper;
import com.ysls.imhere.indicator.PageIndicator;
import com.ysls.imhere.slidingdrawer.SemiClosedSlidingDrawer;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.utils.PopupWindowUtil;
import com.ysls.imhere.utils.ToastUtil;
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
public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	private static String TAG = "HomeActivity";

	private Context mContext;

	private SemiClosedSlidingDrawer slidingDrawer;
	private LinearLayout openLayout, closeLayout;
	private ImageViewWithText sdgl;
	private ImageViewWithText ysbh;
	private ImageViewWithText wdrj;
	private ImageViewWithText rjyx;
	private ImageViewWithText sygj;
	private ImageViewWithText sjfd;
	private ImageViewWithText txbf;
	private ImageViewWithText qqwp;

	private Button bn_refresh;

	private ImageView imgLeft;
	private ImageView imgMore;
	private ImageView imgRight;

	private InputMethodManager imm;
	private boolean isShowPopupWindows = false;
	private int keyBackClickCount = 0;
	private float lastX = 0.0F;
	private float lastY = 0.0F;

	private LinearLayout llGoHome;
	private LinearLayout loadFaillayout;
	private LinearLayout loadLayout;

	private TextView mAboveTitle;
	private BasePageViewAdapter mBasePageViewAdapter;

	private PageIndicator mIndicator;
	private boolean mIsAnim = false;
	private boolean mIsTitleHide = false;
	private int mTag = 0;
	private ViewPager mViewPager;
	private LinearLayout mlinear_listview;

	private View title;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.above_slidingmenu);
		mContext = this;

		initClass();
		initControl();
		initBelowSlidingMenu();
		initViewPager();

	}

	/**
	 * 初始化相关类
	 */
	private void initClass() {
	}

	/**
	 * 加载界面所有布局控件
	 */
	private void initControl() {
		imm = ((InputMethodManager) getApplicationContext().getSystemService(
				"input_method"));
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);

		mAboveTitle = (TextView) findViewById(R.id.tv_above_title);
		mAboveTitle.setText("IMHere");
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
					}
				});

		sdgl = (ImageViewWithText) findViewById(R.id.custom_sdgl);
		sdgl.setOnClickListener(this);
		ysbh = (ImageViewWithText) findViewById(R.id.custom_ysbh);
		ysbh.setOnClickListener(this);
		wdrj = (ImageViewWithText) findViewById(R.id.custom_wdrj);
		wdrj.setOnClickListener(this);
		rjyx = (ImageViewWithText) findViewById(R.id.custom_rjyx);
		rjyx.setOnClickListener(this);
		sygj = (ImageViewWithText) findViewById(R.id.custom_sygj);
		sygj.setOnClickListener(this);
		sjfd = (ImageViewWithText) findViewById(R.id.custom_sjfd);
		sjfd.setOnClickListener(this);
		txbf = (ImageViewWithText) findViewById(R.id.custom_txbf);
		txbf.setOnClickListener(this);
		qqwp = (ImageViewWithText) findViewById(R.id.custom_qqwp);
		qqwp.setOnClickListener(this);
	}

	/**
	 * 初始化 主页，任务，通讯录 滑动Menu菜单
	 */
	private void initViewPager() {

		this.mBasePageViewAdapter = new BasePageViewAdapter(this);
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
		menuList.add("通讯录");

		this.isShowPopupWindows = true;
		this.mBasePageViewAdapter.Clear();
		this.mViewPager.removeAllViews();

		if (!menuList.isEmpty()) {
			mBasePageViewAdapter.addFragment(menuList);
			imgRight.setVisibility(View.VISIBLE);
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

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.Linear_above_toHome:
			ToastUtil.showMsg(mContext, "You clicked my logo!");
			break;
		case R.id.imageview_above_more:
			if (isShowPopupWindows) {
				new PopupWindowUtil(mViewPager).showActionWindow(paramView,
						this, mBasePageViewAdapter.tabs);
			}
			break;
		case R.id.bn_refresh:
			ToastUtil.showMsg(mContext, "You clicked my refresh button!");
			break;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			DBHelper.getInstance(this).closeDb();
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
				defaultFinish();
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
				LogUtil.i(HomeActivity.TAG, "任务");

			} else if (selectId == mBasePageViewAdapter.mFragments.size() - 1) {
				imgRight.setVisibility(View.GONE);
				LogUtil.i(HomeActivity.TAG, "主页");

			} else {
				imgRight.setVisibility(View.VISIBLE);
				imgLeft.setVisibility(View.VISIBLE);
				LogUtil.i(HomeActivity.TAG, "通讯录");
			}
		}
	}
}