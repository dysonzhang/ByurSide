package com.ysls.imhere;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.R;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.calendar.CalendarGridView;
import com.ysls.imhere.calendar.CalendarGridViewAdapter;
import com.ysls.imhere.calendar.NumberHelper;
import com.ysls.imhere.widget.TitleBarView;

import java.util.Calendar;
import java.util.Date;

public class CheckinHistoryActivity extends BaseActivity implements
		OnTouchListener {

	private TitleBarView mTitleBarView;

	/**
	 * 日历布局ID
	 */
	private static final int CAL_LAYOUT_ID = 55;
	// 判断手势用
	private static final int SWIPE_MIN_DISTANCE = 120;

	private static final int SWIPE_MAX_OFF_PATH = 250;

	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	// 动画
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;
	GestureDetector mGesture = null;

	/**
	 * 上一个月按钮
	 */
	private ImageView mPreMonthImg;

	/**
	 * 下一个月按钮
	 */
	private ImageView mNextMonthImg;

	/**
	 * 用于显示今天的日期
	 */
	private TextView mDayMessage;

	/**
	 * 用于装截日历的View
	 */
	private RelativeLayout mCalendarMainLayout;

	// 基本变量
	private Context mContext = this;
	/**
	 * 上一个月View
	 */
	private GridView firstGridView;

	/**
	 * 当前月View
	 */
	private GridView currentGridView;

	/**
	 * 下一个月View
	 */
	private GridView lastGridView;

	/**
	 * 当前显示的日历
	 */
	private Calendar calStartDate = Calendar.getInstance();

	/**
	 * 选择的日历
	 */
	private Calendar calSelected = Calendar.getInstance();

	/**
	 * 今日
	 */
	private Calendar calToday = Calendar.getInstance();

	/**
	 * 当前界面展示的数据源
	 */
	private CalendarGridViewAdapter currentGridAdapter;

	/**
	 * 预装载上一个月展示的数据源
	 */
	private CalendarGridViewAdapter firstGridAdapter;

	/**
	 * 预装截下一个月展示的数据源
	 */
	private CalendarGridViewAdapter lastGridAdapter;
	//
	/**
	 * 当前视图月
	 */
	private int mMonthViewCurrentMonth = 0;

	/**
	 * 当前视图年
	 */
	private int mMonthViewCurrentYear = 0;

	/**
	 * 起始周
	 */
	private int iFirstDayOfWeek = Calendar.MONDAY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_checkin_history);
		initView();
		initTitleView();
		updateStartDateForMonth();

		generateContetView(mCalendarMainLayout);
	}

	/**
	 * 用于初始化控件
	 */
	private void initView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);

		mDayMessage = (TextView) findViewById(R.id.day_message);
		mCalendarMainLayout = (RelativeLayout) findViewById(R.id.calendar_main);
		mPreMonthImg = (ImageView) findViewById(R.id.left_img);
		mNextMonthImg = (ImageView) findViewById(R.id.right_img);

		mPreMonthImg.setOnClickListener(onPreMonthClickListener);
		mNextMonthImg.setOnClickListener(onNextMonthClickListener);

		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils
				.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_out);

		slideLeftIn.setAnimationListener(animationListener);
		slideLeftOut.setAnimationListener(animationListener);
		slideRightIn.setAnimationListener(animationListener);
		slideRightOut.setAnimationListener(animationListener);

		mGesture = new GestureDetector(this, new GestureListener());
	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setBtnRight("今天");
		mTitleBarView.setTitleText(R.string.menu_checkin_history);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});

		/**
		 * 今天按钮
		 */
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calStartDate = Calendar.getInstance();
				updateStartDateForMonth();
				generateContetView(mCalendarMainLayout);
			}
		});
	}

	/**
	 * 用于加载上一个月日期的事件
	 */
	private View.OnClickListener onPreMonthClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			viewFlipper.setInAnimation(slideRightIn);
			viewFlipper.setOutAnimation(slideRightOut);
			viewFlipper.showPrevious();
			setPrevViewItem();
		}
	};

	/**
	 * 用于加载下一个月日期的事件
	 */
	private View.OnClickListener onNextMonthClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			viewFlipper.setInAnimation(slideLeftIn);
			viewFlipper.setOutAnimation(slideLeftOut);
			viewFlipper.showNext();
			setNextViewItem();
		}
	};

	/**
	 * 主要用于生成发前展示的日历View
	 * 
	 * @param layout
	 *            将要用于去加载的布局
	 */
	private void generateContetView(RelativeLayout layout) {
		// 创建一个垂直的线性布局（整体内容）
		viewFlipper = new ViewFlipper(this);
		viewFlipper.setId(CAL_LAYOUT_ID);
		calStartDate = getCalendarStartDate();
		CreateGirdView();
		RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(viewFlipper, params_cal);

		LinearLayout br = new LinearLayout(this);
		RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 1);
		params_br.addRule(RelativeLayout.BELOW, CAL_LAYOUT_ID);
		br.setBackgroundColor(getResources().getColor(
				R.color.calendar_background));
		layout.addView(br, params_br);
	}

	/**
	 * 用于创建当前将要用于展示的View
	 */
	private void CreateGirdView() {

		Calendar firstCalendar = Calendar.getInstance(); // 临时
		Calendar currentCalendar = Calendar.getInstance(); // 临时
		Calendar lastCalendar = Calendar.getInstance(); // 临时
		firstCalendar.setTime(calStartDate.getTime());
		currentCalendar.setTime(calStartDate.getTime());
		lastCalendar.setTime(calStartDate.getTime());

		firstGridView = new CalendarGridView(mContext);
		firstCalendar.add(Calendar.MONTH, -1);
		firstGridAdapter = new CalendarGridViewAdapter(this, firstCalendar);
		firstGridView.setAdapter(firstGridAdapter);// 设置菜单Adapter
		firstGridView.setId(CAL_LAYOUT_ID);

		currentGridView = new CalendarGridView(mContext);
		currentGridAdapter = new CalendarGridViewAdapter(this, currentCalendar);
		currentGridView.setAdapter(currentGridAdapter);// 设置菜单Adapter
		currentGridView.setId(CAL_LAYOUT_ID);

		lastGridView = new CalendarGridView(mContext);
		lastCalendar.add(Calendar.MONTH, 1);
		lastGridAdapter = new CalendarGridViewAdapter(this, lastCalendar);
		lastGridView.setAdapter(lastGridAdapter);// 设置菜单Adapter
		lastGridView.setId(CAL_LAYOUT_ID);

		currentGridView.setOnTouchListener(this);
		firstGridView.setOnTouchListener(this);
		lastGridView.setOnTouchListener(this);

		if (viewFlipper.getChildCount() != 0) {
			viewFlipper.removeAllViews();
		}

		viewFlipper.addView(currentGridView);
		viewFlipper.addView(lastGridView);
		viewFlipper.addView(firstGridView);

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);
		mDayMessage.setText(s);
	}

	/**
	 * 上一个月
	 */
	private void setPrevViewItem() {
		mMonthViewCurrentMonth--;// 当前选择月--
		// 如果当前月为负数的话显示上一年
		if (mMonthViewCurrentMonth == -1) {
			mMonthViewCurrentMonth = 11;
			mMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth); // 设置月
		calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear); // 设置年

	}

	/**
	 * 下一个月
	 */
	private void setNextViewItem() {
		mMonthViewCurrentMonth++;
		if (mMonthViewCurrentMonth == 12) {
			mMonthViewCurrentMonth = 0;
			mMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear);

	}

	/**
	 * 根据改变的日期更新日历 填充日历控件用
	 */
	private void updateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		mMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		mMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1) + "-"
				+ calStartDate.get(Calendar.DAY_OF_MONTH);
		mDayMessage.setText(s);
		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

	}

	/**
	 * 用于获取当前显示月份的时间
	 * 
	 * @return 当前显示月份的时间
	 */
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		return calStartDate;
	}

	AnimationListener animationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// 当动画完成后调用
			CreateGirdView();
		}
	};

	class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
					setNextViewItem();
					return true;

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					setPrevViewItem();
					return true;

				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// 得到当前选中的是第几个单元格
			int pos = currentGridView.pointToPosition((int) e.getX(),
					(int) e.getY());
			LinearLayout txtDay = (LinearLayout) currentGridView
					.findViewById(pos + 5000);
			if (txtDay != null) {
				if (txtDay.getTag() != null) {
					Date date = (Date) txtDay.getTag();
					calSelected.setTime(date);

					String state = " 正常";

					if (calSelected.get(Calendar.DAY_OF_MONTH) == 25) {
						state = " 迟到";
					}
					if (calSelected.get(Calendar.DAY_OF_MONTH) == 15) {
						state = " 早退";
					}

					String s = calSelected.get(Calendar.YEAR)
							+ "-"
							+ NumberHelper.LeftPad_Tow_Zero(calSelected
									.get(Calendar.MONTH) + 1)
							+ "-"
							+ NumberHelper.LeftPad_Tow_Zero(calSelected
									.get(Calendar.DAY_OF_MONTH));

					mDayMessage.setText(s + state);

					currentGridAdapter.setSelectedDate(calSelected);
					currentGridAdapter.notifyDataSetChanged();
					firstGridAdapter.setSelectedDate(calSelected);
					firstGridAdapter.notifyDataSetChanged();

					lastGridAdapter.setSelectedDate(calSelected);
					lastGridAdapter.notifyDataSetChanged();
				}
			}

			Log.i("TEST", "onSingleTapUp -  pos=" + pos);

			return false;
		}
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}
