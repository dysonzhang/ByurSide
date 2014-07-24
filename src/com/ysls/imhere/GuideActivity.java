package com.ysls.imhere;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.litesuits.http.request.param.HttpMethod;
import com.litesuits.http.request.param.HttpParam;
import com.ysls.imhere.adapter.ViewPagerAdapter;
import com.ysls.imhere.base.BaseActivity;
import com.ysls.imhere.config.Global;

import java.util.ArrayList;

/**
 * 使用介绍页
 * 
 * @author dyson
 * 
 */
public class GuideActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {
	
	private static int viewlenght = 4;
	private int currentIndex;
	private ImageView[] points;
	private Button startBt;
	
	private View view1;
	private View view2;
	private View view3;
	private View view4;
	private ViewPager viewPager;
	private ArrayList<View> views;
	
	private ViewPagerAdapter vpAdapter;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_guide);

		initView();
		initData();
	}

	private void initView() {
		LayoutInflater localLayoutInflater = LayoutInflater.from(this);

		view1 = localLayoutInflater.inflate(R.layout.guide_view01, null);
		view2 = localLayoutInflater.inflate(R.layout.guide_view02, null);
		view3 = localLayoutInflater.inflate(R.layout.guide_view03, null);
		view4 = localLayoutInflater.inflate(R.layout.guide_view04, null);

		viewPager = ((ViewPager) findViewById(R.id.viewpager));

		views = new ArrayList<View>();
		vpAdapter = new ViewPagerAdapter(views);
		startBt = ((Button) view4.findViewById(R.id.startBtn));
	}

	private void initData() {

		views.add(this.view1);
		views.add(this.view2);
		views.add(this.view3);
		views.add(this.view4);

		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(vpAdapter);

		startBt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				startbutton();
			}
		});

		initPoint();
	}

	private void initPoint() {
		LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.ll);
		points = new ImageView[viewlenght];
		for (int i = 0;; ++i) {
			if (i >= viewlenght) {
				currentIndex = 0;
				points[this.currentIndex].setEnabled(false);
				return;
			}
			points[i] = ((ImageView) localLinearLayout.getChildAt(i));
			points[i].setEnabled(true);
			points[i].setOnClickListener(this);
			points[i].setTag(Integer.valueOf(i));
		}
	}

	private void setCurDot(int paramInt) {
		if ((paramInt < 0) || (paramInt > -1 + viewlenght)
				|| (this.currentIndex == paramInt))
			return;
		points[paramInt].setEnabled(false);
		points[currentIndex].setEnabled(true);
		currentIndex = paramInt;
	}

	private void setCurView(int paramInt) {
		if ((paramInt < 0) || (paramInt >= viewlenght))
			return;
		viewPager.setCurrentItem(paramInt);
	}

	private void startbutton() {
		
		if(Global.isLogin){
			openActivity(MainActivity.class);
		}else{
			openActivity(LoginActivity.class);
		}
		
		defaultFinish();
	}

	public void onClick(View paramView) {
		int i = ((Integer) paramView.getTag()).intValue();
		setCurView(i);
		setCurDot(i);
	}

	public void onPageScrollStateChanged(int paramInt) {
	}

	public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
	}

	public void onPageSelected(int paramInt) {
		setCurDot(paramInt);
	}

	@Override
	public void refreshUI(String taskApiURL, HttpParam httpParam,
			HttpMethod httpMethod) {
		// TODO Auto-generated method stub
	}
}