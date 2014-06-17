package com.ysls.imhere;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.eoe.app.entity.NavigationModel;

import com.ysls.imhere.adapter.BasePageViewAdapter;
import com.ysls.imhere.base.BaseSlidingFragmentActivity;
import com.ysls.imhere.db.DBHelper;
import com.ysls.imhere.indicator.PageIndicator;
import com.ysls.imhere.slidingmenu.SlidingMenu;
import com.ysls.imhere.utils.IntentUtil;
import com.ysls.imhere.utils.LogUtil;
import com.ysls.imhere.utils.PopupWindowUtil;
import com.ysls.imhere.utils.ToastUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseSlidingFragmentActivity implements
		View.OnClickListener, Animation.AnimationListener {
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
		initSlidingMenu();
		setContentView(R.layout.above_slidingmenu);

		initClass();
		initControl();

		initViewPager();

		initListView();
		initgoHome();
		initNav();
	}

	private List<Map<String, Object>> getData() {
		ArrayList localArrayList = new ArrayList();
		HashMap localHashMap1 = new HashMap();
		localHashMap1.put("text", getResources().getString(2131427340));
		localHashMap1.put("img", Integer.valueOf(2130837563));
		localArrayList.add(localHashMap1);
		HashMap localHashMap2 = new HashMap();
		localHashMap2.put("text", getResources().getString(2131427341));
		localHashMap2.put("img", Integer.valueOf(2130837562));
		localArrayList.add(localHashMap2);
		HashMap localHashMap3 = new HashMap();
		localHashMap3.put("text", getResources().getString(2131427342));
		localHashMap3.put("img", Integer.valueOf(2130837563));
		localArrayList.add(localHashMap3);
		HashMap localHashMap4 = new HashMap();
		localHashMap4.put("text", getResources().getString(2131427343));
		localHashMap4.put("img", Integer.valueOf(2130837560));
		localArrayList.add(localHashMap4);
		HashMap localHashMap5 = new HashMap();
		localHashMap5.put("text", getResources().getString(2131427344));
		localHashMap5.put("img", Integer.valueOf(2130837562));
		localArrayList.add(localHashMap5);
		HashMap localHashMap6 = new HashMap();
		localHashMap6.put("text", getResources().getString(2131427345));
		localHashMap6.put("img", Integer.valueOf(2130837563));
		localArrayList.add(localHashMap6);
		HashMap localHashMap7 = new HashMap();
		localHashMap7.put("text", getResources().getString(2131427346));
		localHashMap7.put("img", Integer.valueOf(2130837560));
		localArrayList.add(localHashMap7);
		return localArrayList;
	}

	private void initClass() {
	}

	private void initControl() {
		this.imm = ((InputMethodManager) getApplicationContext()
				.getSystemService("input_method"));
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);

		mAboveTitle = (TextView) findViewById(R.id.tv_above_title);
		this.mAboveTitle.setText("我在");

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

		this.bn_refresh = ((Button) findViewById(R.id.bn_refresh));
		this.bn_refresh.setOnClickListener(this);
	}

	private void initListView() {
		lvAdapter = new SimpleAdapter(this, getData(),
				R.layout.behind_list_show, new String[] { LIST_TEXT,
						LIST_IMAGEVIEW },
				new int[] { R.id.textview_behind_title,
						R.id.imageview_behind_icon }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub.
				View view = super.getView(position, convertView, parent);
				if (position == mTag) {
					view.setBackgroundResource(R.drawable.back_behind_list);
					lvTitle.setTag(view);
				} else {
					view.setBackgroundColor(Color.TRANSPARENT);
				}
				return view;
			}
		};
		this.lvTitle.setAdapter(this.lvAdapter);
		this.lvTitle
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> paramAdapterView,
							View paramView, int paramInt, long paramLong) {
						NavigationModel localNavigationModel = (NavigationModel) HomeActivity.this.navs
								.get(paramInt);
						HomeActivity.this.current_page = localNavigationModel
								.getTags();
						if (HomeActivity.this.lvTitle.getTag() != null) {
							if (HomeActivity.this.lvTitle.getTag() == paramView) {
								HomeActivity.this.showContent();
								return;
							}
							((View) HomeActivity.this.lvTitle.getTag())
									.setBackgroundColor(Color.TRANSPARENT);
						}
						HomeActivity.this.lvTitle.setTag(paramView);
						paramView.setBackgroundResource(R.drawable.back_behind_list);
						switch (paramInt) {

						case 0:
							HomeActivity.this.initViewPager();
							break;
						case 1:
							ToastUtil.showMsg(HomeActivity.this, "您点击了通知公告");
							IntentUtil.start_activity(HomeActivity.this,
									AboutActivity.class);
							break;
						case 2:
							ToastUtil.showMsg(HomeActivity.this, "您点击了帐号设置");
							IntentUtil.start_activity(HomeActivity.this,
									AboutActivity.class);
							break;
						case 3:
							ToastUtil.showMsg(HomeActivity.this, "您点击了密码修改");
							IntentUtil.start_activity(HomeActivity.this,
									AboutActivity.class);
							break;
						case 4:
							ToastUtil.showMsg(HomeActivity.this, "您点击了反馈");
							IntentUtil.start_activity(HomeActivity.this,
									AboutActivity.class);
							break;
						case 5:
							ToastUtil.showMsg(HomeActivity.this, "您点击了关于");
							IntentUtil.start_activity(HomeActivity.this,
									AboutActivity.class);
							break;
						case 6:
							ToastUtil.showMsg(HomeActivity.this, "您点击了退出登录");
							break;
						}
					}
				});
	}

	private void initNav() {
		navs = new ArrayList();
		NavigationModel localNavigationModel1 = new NavigationModel(
				getResources().getString(2131427340), "");
		NavigationModel localNavigationModel2 = new NavigationModel(
				getResources().getString(2131427341), "notice");
		NavigationModel localNavigationModel3 = new NavigationModel(
				getResources().getString(2131427342), "setting");
		NavigationModel localNavigationModel4 = new NavigationModel(
				getResources().getString(2131427343), "passwordmodify");
		NavigationModel localNavigationModel5 = new NavigationModel(
				getResources().getString(2131427344), "feedback");
		NavigationModel localNavigationModel6 = new NavigationModel(
				getResources().getString(2131427345), "about");
		NavigationModel localNavigationModel7 = new NavigationModel(
				getResources().getString(2131427346), "exit");
		Collections.addAll(this.navs, new NavigationModel[] {
				localNavigationModel1, localNavigationModel2,
				localNavigationModel3, localNavigationModel4,
				localNavigationModel5, localNavigationModel6,
				localNavigationModel7 });
	}

	// [start]初始化函数
	private void initSlidingMenu() {
		setBehindContentView(R.layout.behind_slidingmenu);
		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// sm.setShadowWidth(20);
		sm.setBehindScrollScale(0);
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
		showContent();
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
                showMenu();
                break;
         
            case R.id.imageview_above_more:
                if (isShowPopupWindows) {
                    new PopupWindowUtil(mViewPager).showActionWindow(paramView, this,
                    		mBasePageViewAdapter.tabs);
                }
                break;
           
            case R.id.bn_refresh:
                switch (mTag) {}
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
		// TODO Auto-generated method stub

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

			if (sm.isMenuShowing()) {
				toggle();
			} else {
				showMenu();
			}
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
				HomeActivity.this.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_FULLSCREEN);
				HomeActivity.this.imgLeft.setVisibility(8);
				LogUtil.i(HomeActivity.TAG, "主页");

			} else if (paramInt == HomeActivity.this.mBasePageViewAdapter.mFragments
					.size() - 1) {
				HomeActivity.this.imgRight.setVisibility(8);
				HomeActivity.this.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
				LogUtil.i(HomeActivity.TAG, "我的考勤");

			} else {
				HomeActivity.this.imgRight.setVisibility(0);
				HomeActivity.this.imgLeft.setVisibility(0);
				HomeActivity.this.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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