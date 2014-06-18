package com.ysls.imhere;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.eoe.app.entity.NavigationModel;

import com.ysls.imhere.adapter.BasePageViewAdapter;
import com.ysls.imhere.base.BaseFragmentActivity;
import com.ysls.imhere.db.DBHelper;
import com.ysls.imhere.indicator.PageIndicator;
import com.ysls.imhere.slidingmenu.SlidingMenu;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.utils.PopupWindowUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener, AnimationListener {

	private static String TAG = "HomeActivity";

	private final String LIST_IMAGEVIEW = "img";
	private final String LIST_TEXT = "text";

	private Button bn_refresh;
	private String current_page;

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
	private SimpleAdapter lvAdapter;

	private ListView lvTitle;
	private TextView mAboveTitle;
	private BasePageViewAdapter mBasePageViewAdapter;
	private FrameLayout mFrameTv;
	private ImageView mImgTv;
	private PageIndicator mIndicator;
	private boolean mIsAnim = false;
	private boolean mIsTitleHide = false;
	private int mTag = 0;
	private ViewPager mViewPager;
	private LinearLayout mlinear_listview;
	private List<NavigationModel> navs;
	private SlidingMenu sm;
	private View title;
	private TextView tv_username;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		setContentView(R.layout.above_slidingmenu);

		initClass();
		initControl();

		initViewPager();

		initgoHome();
	}

	private void initClass() {
	}

	private void initControl() {
		imm = ((InputMethodManager) getApplicationContext().getSystemService(
				"input_method"));
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);

		mAboveTitle = (TextView) findViewById(R.id.tv_above_title);
		mAboveTitle.setText("我在");

		imgMore = (ImageView) findViewById(R.id.imageview_above_more);
		imgMore.setOnClickListener(this);
		imgLeft = (ImageView) findViewById(R.id.imageview_above_left);
		imgRight = (ImageView) findViewById(R.id.imageview_above_right);
		mViewPager = (ViewPager) findViewById(R.id.above_pager);
		mIndicator = (PageIndicator) findViewById(R.id.above_indicator);
		lvTitle = (ListView) findViewById(R.id.behind_list_show);
		llGoHome = (LinearLayout) findViewById(R.id.Linear_above_toHome);

		title = findViewById(R.id.main_title);
		mlinear_listview = (LinearLayout) findViewById(R.id.main_linear_listview);
		mFrameTv = (FrameLayout) findViewById(R.id.fl_off);
		mImgTv = (ImageView) findViewById(R.id.iv_off);

		bn_refresh = ((Button) findViewById(R.id.bn_refresh));
		bn_refresh.setOnClickListener(this);
	}

	private void initViewPager() {

		this.mBasePageViewAdapter = new BasePageViewAdapter(this);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mBasePageViewAdapter);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(new MyPageChangeListener());

		// TODO Auto-generated method stub
		imgLeft.setVisibility(View.GONE);
		imgRight.setVisibility(View.GONE);
		loadLayout.setVisibility(View.VISIBLE);
		mViewPager.setVisibility(View.GONE);
		mViewPager.removeAllViews();
		mBasePageViewAdapter.Clear();

		isShowPopupWindows = false;

		ArrayList localArrayList = new ArrayList();
		localArrayList.add("主页");
		localArrayList.add("任务中心");
		localArrayList.add("我的考勤");

		this.isShowPopupWindows = true;
		this.mBasePageViewAdapter.Clear();
		this.mViewPager.removeAllViews();

		if (!localArrayList.isEmpty()) {
			this.mBasePageViewAdapter.addFragment(localArrayList);
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
		mViewPager.setCurrentItem(0);
		mIndicator.notifyDataSetChanged();

	}

	private void initgoHome() {
		this.llGoHome.setOnClickListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(event);
		if (mIsAnim || mViewPager.getChildCount() <= 1) {
			return false;
		}
		final int action = event.getAction();

		float x = event.getX();
		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			lastX = x;
			return false;
		case MotionEvent.ACTION_MOVE:
			float dY = Math.abs(y - lastY);
			float dX = Math.abs(x - lastX);
			boolean down = y > lastY ? true : false;
			lastY = y;
			lastX = x;
			if (dX < 8 && dY > 8 && !mIsTitleHide && !down) {
				Animation anim = AnimationUtils.loadAnimation(
						HomeActivity.this, R.anim.push_top_in);
				// anim.setFillAfter(true);
				anim.setAnimationListener(HomeActivity.this);
				title.startAnimation(anim);
			} else if (dX < 8 && dY > 8 && mIsTitleHide && down) {
				Animation anim = AnimationUtils.loadAnimation(
						HomeActivity.this, R.anim.push_top_out);
				// anim.setFillAfter(true);
				anim.setAnimationListener(HomeActivity.this);
				title.startAnimation(anim);
			} else {
				return false;
			}
			mIsTitleHide = !mIsTitleHide;
			mIsAnim = true;
			break;
		default:
			return false;
		}
		return false;
	}

	public void onAnimationEnd(Animation paramAnimation) {
		if (this.mIsTitleHide)
			this.title.setVisibility(8);
		this.mIsAnim = false;
	}

	public void onAnimationRepeat(Animation paramAnimation) {
	}

	public void onAnimationStart(Animation paramAnimation) {
		this.title.setVisibility(0);
		if (this.mIsTitleHide) {
			FrameLayout.LayoutParams localLayoutParams3 = (FrameLayout.LayoutParams) this.mlinear_listview
					.getLayoutParams();
			localLayoutParams3.setMargins(0, 0, 0, 0);
			this.mlinear_listview.setLayoutParams(localLayoutParams3);
			return;
		}
		FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams) this.title
				.getLayoutParams();
		localLayoutParams1.setMargins(0, 0, 0, 0);
		this.title.setLayoutParams(localLayoutParams1);
		FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams) this.mlinear_listview
				.getLayoutParams();
		localLayoutParams2.setMargins(0,
				getResources().getDimensionPixelSize(2131230725), 0, 0);
		this.mlinear_listview.setLayoutParams(localLayoutParams2);
	}

	public void onClick(View paramView) {
		// TODO Auto-generated method stub
		switch (paramView.getId()) {
		case R.id.Linear_above_toHome:
			// showMenu();
			break;

		case R.id.imageview_above_more:
			if (isShowPopupWindows) {
				new PopupWindowUtil(mViewPager).showActionWindow(paramView,
						this, mBasePageViewAdapter.tabs);
			}
			break;

		case R.id.bn_refresh:
			switch (mTag) {
			}
			break;
		}

	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			DBHelper.getInstance(this).closeDb();
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
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
				mFrameTv.setVisibility(View.VISIBLE);
				mImgTv.setVisibility(View.VISIBLE);
				Animation anim = AnimationUtils.loadAnimation(
						HomeActivity.this, R.anim.tv_off);
				anim.setAnimationListener(new tvOffAnimListener());
				mImgTv.startAnimation(anim);
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

			// if (sm.isMenuShowing()) {
			// toggle();
			// } else {
			// showMenu();
			// }
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
		this.keyBackClickCount = 0;
	}

	public void onSaveInstanceState(Bundle paramBundle) {
		super.onSaveInstanceState(paramBundle);
	}

	class MyPageChangeListener implements ViewPager.OnPageChangeListener {
		public void onPageScrollStateChanged(int paramInt) {
		}

		public void onPageScrolled(int paramInt1, float paramFloat,
				int paramInt2) {
		}

		public void onPageSelected(int paramInt) {
			if (paramInt == 0) {
				imgLeft.setVisibility(View.GONE);
				LogUtil.i(HomeActivity.TAG, "主页");

			} else if (paramInt == HomeActivity.this.mBasePageViewAdapter.mFragments
					.size() - 1) {
				imgRight.setVisibility(View.GONE);
				LogUtil.i(HomeActivity.TAG, "我的考勤");

			} else {
				imgRight.setVisibility(View.VISIBLE);
				imgLeft.setVisibility(View.VISIBLE);
				LogUtil.i(HomeActivity.TAG, "任务中心");
			}
		}
	}

	class tvOffAnimListener implements Animation.AnimationListener {
		public void onAnimationEnd(Animation paramAnimation) {
			HomeActivity.this.defaultFinish();
		}

		public void onAnimationRepeat(Animation paramAnimation) {
		}

		public void onAnimationStart(Animation paramAnimation) {
		}
	}
}