package com.ysls.imhere.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import com.ysls.imhere.fragment.HomeFragment;
import com.ysls.imhere.fragment.HttpErrorFragment;
import com.ysls.imhere.fragment.CommunicateFragment;
import com.ysls.imhere.fragment.TodoFragment;

/**
 * 三大导航栏viewpage适配器
 * 
 * @author dyson
 * 
 */
public class BasePageViewAdapter extends FragmentStatePagerAdapter {

	private Activity mActivity;

	public ViewPager mViewPager;
	
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public List<String> tabs = new ArrayList<String>();

	public BasePageViewAdapter(FragmentActivity fragmentActivity, ViewPager mViewPager) {
		super(fragmentActivity.getSupportFragmentManager());
		mActivity = fragmentActivity;
		this.mViewPager = mViewPager;
	}

	public void Clear() {
		mFragments.clear();
		tabs.clear();
	}

	public void addFragment(List<String> paramList) {
		tabs.addAll(paramList);
		for (int i = 0; i < tabs.size(); i++) {
			String str = (String) this.tabs.get(i);
			if (str.equals("主页"))
				addTab(new HomeFragment(mViewPager));
			if (str.equals("任务"))
				addTab(new TodoFragment());
			if (str.equals("沟通"))
				addTab(new CommunicateFragment());
		}
	}

	public void addNullFragment() {
		tabs.add("连接错误");
		addTab(new HttpErrorFragment());
	}

	public void addTab(Fragment paramFragment) {
		mFragments.add(paramFragment);
		notifyDataSetChanged();
	}

	public void destroyItem(ViewGroup paramViewGroup, int paramInt,
			Object paramObject) {
		super.destroyItem(paramViewGroup, paramInt, paramObject);
	}

	public int getCount() {
		return mFragments.size();
	}

	public Fragment getItem(int paramInt) {
		return ((Fragment) mFragments.get(paramInt));
	}

	public int getItemPosition(Object paramObject) {
		return -2;
	}

	public CharSequence getPageTitle(int paramInt) {
		return ((CharSequence) tabs.get(paramInt));
	}
}