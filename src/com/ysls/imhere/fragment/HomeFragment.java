package com.ysls.imhere.fragment;

import java.util.Calendar;

import com.ysls.imhere.ChatActivity;
import com.ysls.imhere.R;
import com.ysls.imhere.TodoDetailActivity;
import com.ysls.imhere.UserInfoActivity;
import com.ysls.imhere.calendar.NumberHelper;
import com.ysls.imhere.widget.ProgressWebView;
import com.ysls.imhere.widget.RoundImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements OnClickListener {

	private Context mContext;

	private ImageButton ib_user_state;
	private ImageButton ib_reward;
	private TextView tv_data_city;
	private RoundImageView iv_user_logo;

	private RelativeLayout rl_todo_no_read;
	private RelativeLayout rl_msg_no_read;

	private RelativeLayout rl_todo_one;
	private RelativeLayout rl_todo_two;
	private RelativeLayout rl_todo_three;

	private RelativeLayout rl_msg_one;
	private RelativeLayout rl_msg_two;

	protected View view;
	private ProgressWebView webview;
	private ViewPager mViewPager;

	private Calendar cal = Calendar.getInstance();

	public HomeFragment() {
	}

	public HomeFragment(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle) {
		view = layoutInflater.inflate(R.layout.fragment_home, null);
		mContext = getActivity();

		findView(view);

		initView();
		// // 绑定控件
		// webview = (ProgressWebView) view.findViewById(R.id.webview);
		// webview.loadUrl("http://www.protoshop.io");

		return view;
	}

	private void findView(View view) {
		ib_user_state = (ImageButton) view.findViewById(R.id.ib_user_state);
		ib_reward = (ImageButton) view.findViewById(R.id.ib_reward);
		iv_user_logo = (RoundImageView) view.findViewById(R.id.iv_user_logo);
		tv_data_city = (TextView) view.findViewById(R.id.tv_data_city);

		rl_todo_no_read = (RelativeLayout) view
				.findViewById(R.id.rl_todo_no_read);
		rl_msg_no_read = (RelativeLayout) view
				.findViewById(R.id.rl_msg_no_read);

		rl_todo_one = (RelativeLayout) view.findViewById(R.id.rl_todo_one);
		rl_todo_two = (RelativeLayout) view.findViewById(R.id.rl_todo_two);
		rl_todo_three = (RelativeLayout) view.findViewById(R.id.rl_todo_three);

		rl_msg_one = (RelativeLayout) view.findViewById(R.id.rl_msg_one);
		rl_msg_two = (RelativeLayout) view.findViewById(R.id.rl_msg_two);

		ib_user_state.setOnClickListener(this);
		ib_reward.setOnClickListener(this);
		iv_user_logo.setOnClickListener(this);

		rl_todo_no_read.setOnClickListener(this);
		rl_msg_no_read.setOnClickListener(this);

		rl_todo_one.setOnClickListener(this);
		rl_todo_two.setOnClickListener(this);
		rl_todo_three.setOnClickListener(this);

		rl_msg_one.setOnClickListener(this);
		rl_msg_two.setOnClickListener(this);
	}

	private void initView() {

		String s = cal.get(Calendar.YEAR) + "-"
				+ NumberHelper.LeftPad_Tow_Zero(cal.get(Calendar.MONTH) + 1)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(cal.get(Calendar.DAY_OF_MONTH));

		tv_data_city.setText(s + " 上海");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_user_state:
			Toast.makeText(mContext, "下班打卡成功！", Toast.LENGTH_LONG).show();
			break;
		case R.id.ib_reward:
			Toast.makeText(mContext, "我获得的荣誉奖励！", Toast.LENGTH_LONG).show();
			break;
		case R.id.iv_user_logo:
			mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
			break;
		case R.id.rl_todo_no_read:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rl_msg_no_read:
			mViewPager.setCurrentItem(2);
			break;
		case R.id.rl_todo_one:
			mContext.startActivity(new Intent(mContext,
					TodoDetailActivity.class));
			break;
		case R.id.rl_todo_two:
			mContext.startActivity(new Intent(mContext,
					TodoDetailActivity.class));
			break;
		case R.id.rl_todo_three:
			mContext.startActivity(new Intent(mContext,
					TodoDetailActivity.class));
			break;
		case R.id.rl_msg_one:
			mContext.startActivity(new Intent(mContext,
					TodoDetailActivity.class));
			break;
		case R.id.rl_msg_two:
			mContext.startActivity(new Intent(getActivity(), ChatActivity.class)
					.putExtra("userId", "bill"));
			break;
		}
	}
}