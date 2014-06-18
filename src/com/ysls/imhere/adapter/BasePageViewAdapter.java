package com.ysls.imhere.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import cn.eoe.app.view.CheckFragment;
import cn.eoe.app.view.HomeFragment;
import cn.eoe.app.view.HttpErrorFragment;
import cn.eoe.app.view.TodoFragment;
import java.util.ArrayList;
import java.util.List;

public class BasePageViewAdapter extends FragmentStatePagerAdapter {
	private Activity mActivity;
	public ArrayList<Fragment> mFragments = new ArrayList();
	public List<String> tabs = new ArrayList();

	public BasePageViewAdapter(FragmentActivity paramFragmentActivity) {
		super(paramFragmentActivity.getSupportFragmentManager());
		this.mActivity = paramFragmentActivity;
	}

	public void Clear() {
		this.mFragments.clear();
		this.tabs.clear();
	}

	public void addFragment(List<String> paramList) {
		tabs.addAll(paramList);
		for (int i = 0; i < tabs.size(); i++) {
			String str = (String) this.tabs.get(i);
			if (str.equals("任务"))
				addTab(new HomeFragment(this.mActivity));
			if (str.equals("主页"))
				addTab(new TodoFragment(this.mActivity));
			if (str.equals("通讯录"))
				addTab(new CheckFragment(this.mActivity));
		}
	}

	public void addNullFragment() {
		new String();
		this.tabs.add("连接错误");
		addTab(new HttpErrorFragment());
	}

	public void addTab(Fragment paramFragment) {
		this.mFragments.add(paramFragment);
		notifyDataSetChanged();
	}

	public void destroyItem(ViewGroup paramViewGroup, int paramInt,
			Object paramObject) {
		super.destroyItem(paramViewGroup, paramInt, paramObject);
	}

	public int getCount() {
		return this.mFragments.size();
	}

	public Fragment getItem(int paramInt) {
		return ((Fragment) this.mFragments.get(paramInt));
	}

	public int getItemPosition(Object paramObject) {
		return -2;
	}

	public CharSequence getPageTitle(int paramInt) {
		return ((CharSequence) this.tabs.get(paramInt));
	}
}