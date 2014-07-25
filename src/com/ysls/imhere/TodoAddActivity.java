package com.ysls.imhere;

import java.util.Calendar;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.ysls.imhere.base.BaseFragmentActivity;
import com.ysls.imhere.widget.TitleBarView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TodoAddActivity extends BaseFragmentActivity implements
		OnClickListener, OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	private Context mContext;

	private LinearLayout addFrom;
	private View mPopViewPriority;
	private View mPopViewState;

	private PopupWindow mPopupWindowPriority;
	private PopupWindow mPopupWindowState;

	private TextView todo_very_emergency;
	private TextView todo_emergency;
	private TextView todo_general;
	private TextView todo_priority_cancle;

	private TextView todo_doing;
	private TextView todo_finish;
	private TextView todo_state_cancle;

	private RelativeLayout rl_todo_priority;
	private RelativeLayout rl_todo_state;

	private TitleBarView mTitleBarView;
	private RelativeLayout rl_todo_start_time;
	private RelativeLayout rl_todo_end_time;
	private TextView et_todo_start_time;
	private TextView et_todo_end_time;

	private TextView tv_todo_priority;
	private TextView tv_todo_state;

	private String timeStr = "";

	public static final String DATEPICKER_TAG = "datepicker";
	public static final String TIMEPICKER_TAG = "timepicker";

	final Calendar calendar = Calendar.getInstance();

	final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
			this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH), false);

	final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
			this, calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), false, false);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_add);
		mContext = this;

		findView();
		initTitleView();
		setListener(savedInstanceState);
	}

	private void findView() {
		addFrom = (LinearLayout) findViewById(R.id.ll_add_from);
		mPopViewPriority = LayoutInflater.from(mContext).inflate(
				R.layout.todo_priority, null);
		mPopViewState = LayoutInflater.from(mContext).inflate(
				R.layout.todo_state, null);

		todo_very_emergency = (TextView) mPopViewPriority
				.findViewById(R.id.todo_very_emergency);
		todo_emergency = (TextView) mPopViewPriority
				.findViewById(R.id.todo_emergency);
		todo_general = (TextView) mPopViewPriority
				.findViewById(R.id.todo_general);
		todo_priority_cancle = (TextView) mPopViewPriority
				.findViewById(R.id.todo_priority_cancle);

		todo_doing = (TextView) mPopViewState.findViewById(R.id.todo_doing);
		todo_finish = (TextView) mPopViewState.findViewById(R.id.todo_finish);
		todo_state_cancle = (TextView) mPopViewState
				.findViewById(R.id.todo_state_cancle);

		todo_very_emergency.setOnClickListener(this);
		todo_emergency.setOnClickListener(this);
		todo_general.setOnClickListener(this);
		todo_priority_cancle.setOnClickListener(this);
		todo_doing.setOnClickListener(this);
		todo_finish.setOnClickListener(this);
		todo_state_cancle.setOnClickListener(this);
		
		rl_todo_priority = (RelativeLayout) findViewById(R.id.rl_todo_priority);
		rl_todo_priority.setOnClickListener(this);
		rl_todo_state = (RelativeLayout) findViewById(R.id.rl_todo_state);
		rl_todo_state.setOnClickListener(this);

		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		rl_todo_start_time = (RelativeLayout) findViewById(R.id.rl_todo_start_time);
		rl_todo_end_time = (RelativeLayout) findViewById(R.id.rl_todo_end_time);
		et_todo_start_time = (TextView) findViewById(R.id.et_todo_start_time);
		et_todo_end_time = (TextView) findViewById(R.id.et_todo_end_time);

		tv_todo_priority = (TextView) findViewById(R.id.tv_todo_priority);
		tv_todo_state = (TextView) findViewById(R.id.tv_todo_state);

		mPopupWindowPriority = new PopupWindow(mPopViewPriority,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindowState = new PopupWindow(mPopViewState,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back,
				R.string.back);
		mTitleBarView.setBtnRight("发布");
		mTitleBarView.setTitleText(R.string.todo_add);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultFinish();
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLongToast("任务发布成功!");
				defaultFinish();
			}
		});
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {

		int mon = month + 1;
		timeStr = year + "-" + mon + "-" + day;
		timePickerDialog.setVibrate(false);
		timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);

		TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager()
				.findFragmentByTag(TIMEPICKER_TAG);
		if (tpd != null) {
			tpd.setOnTimeSetListener(this);
		}

	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		timeStr += " " + hourOfDay + ":" + minute;

		if (et_todo_start_time.getText().toString().equals("")) {
			et_todo_start_time.setText(timeStr);
		}
		if (et_todo_end_time.getText().toString().equals("")) {
			et_todo_end_time.setText(timeStr);
		}
	}

	private void setListener(Bundle savedInstanceState) {

		rl_todo_start_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_todo_start_time.setText("");
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(getSupportFragmentManager(),
						DATEPICKER_TAG);
			}
		});

		rl_todo_end_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_todo_end_time.setText("");
				datePickerDialog.setVibrate(false);
				datePickerDialog.setYearRange(1985, 2028);
				datePickerDialog.show(getSupportFragmentManager(),
						DATEPICKER_TAG);
			}
		});

		if (savedInstanceState != null) {
			DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager()
					.findFragmentByTag(DATEPICKER_TAG);
			if (dpd != null) {
				dpd.setOnDateSetListener(this);
			}
		}
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_todo_priority:
			mPopupWindowPriority.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#b0000000")));
			mPopupWindowPriority.showAtLocation(addFrom, Gravity.BOTTOM, 0, 0);
			mPopupWindowPriority.setAnimationStyle(R.style.app_pop);
			mPopupWindowPriority.setOutsideTouchable(true);
			mPopupWindowPriority.setFocusable(true);
			mPopupWindowPriority.update();
			break;
		case R.id.rl_todo_state:
			mPopupWindowState.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#b0000000")));
			mPopupWindowState.showAtLocation(addFrom, Gravity.BOTTOM, 0, 0);
			mPopupWindowState.setAnimationStyle(R.style.app_pop);
			mPopupWindowState.setOutsideTouchable(true);
			mPopupWindowState.setFocusable(true);
			mPopupWindowState.update();
			break;
		case R.id.todo_very_emergency:
			tv_todo_priority.setText(todo_very_emergency.getText());
			mPopupWindowPriority.dismiss();
			break;
		case R.id.todo_emergency:
			tv_todo_priority.setText(todo_emergency.getText());
			mPopupWindowPriority.dismiss();
			break;
		case R.id.todo_general:
			tv_todo_priority.setText(todo_general.getText());
			mPopupWindowPriority.dismiss();
			break;
		case R.id.todo_priority_cancle:
			mPopupWindowPriority.dismiss();
			break;
		case R.id.todo_doing:
			tv_todo_state.setText(todo_doing.getText());
			mPopupWindowState.dismiss();
			break;
		case R.id.todo_finish:
			tv_todo_state.setText(todo_finish.getText());
			mPopupWindowState.dismiss();
			break;
		case R.id.todo_state_cancle:
			mPopupWindowState.dismiss();
			break;
		}

	}
}
