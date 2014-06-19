package com.ysls.imhere.fragment;

import com.ysls.imhere.R;
import com.ysls.imhere.widget.TitleBarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class NewsFatherFragment extends Fragment {

	private static final String TAG = "NewsFatherFragment";
	private Context mContext;

	private View mBaseView;
	private TitleBarView mTitleBarView;
	private RelativeLayout mCanversLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();

		mBaseView = inflater.inflate(R.layout.fragment_news_father, null);

		findView();
		init();

		return mBaseView;
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mCanversLayout = (RelativeLayout) mBaseView
				.findViewById(R.id.rl_canvers);
	}

	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);

		mTitleBarView.setTitleLeft(R.string.cnews);
		mTitleBarView.setTitleRight(R.string.contacter);

		mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleLeft().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(false);
					mTitleBarView.getTitleRight().setEnabled(true);

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					NewsFragment newsFragment = new NewsFragment();
					ft.replace(R.id.child_fragment, newsFragment,
							NewsFatherFragment.TAG);
					// ft.addToBackStack(TAG);
					ft.commit();
				}
			}
		});

		mTitleBarView.getTitleRight().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleRight().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(true);
					mTitleBarView.getTitleRight().setEnabled(false);
					
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					PhoneConstactFragment phoneConstactFragment=new PhoneConstactFragment();
					ft.replace(R.id.child_fragment, phoneConstactFragment,NewsFatherFragment.TAG);
					ft.setCustomAnimations(R.anim.activity_up, R.anim.activity_down);
					ft.commit();
				}
			}
		});
		mTitleBarView.getTitleLeft().performClick();
	}
}
