package com.ysls.imhere.fragment;

import com.ysls.imhere.AddContactActivity;
import com.ysls.imhere.R;
import com.ysls.imhere.TodoAddActivity;
import com.ysls.imhere.widget.TitleBarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TodoFragment extends Fragment {
	private static final String TAG = "TodoFragment";
	private Context mContext;
	private View mBaseView;

	private TitleBarView mTitleBarView;
	private View mTitleBarLineView;
	private RelativeLayout mCanversLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_todo, null);

		findView();
		init();

		return mBaseView;
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mTitleBarLineView = (View) mBaseView.findViewById(R.id.devide_line);
		mCanversLayout = (RelativeLayout) mBaseView
				.findViewById(R.id.rl_todo_canvers);

	}

	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);
		mTitleBarLineView.setVisibility(View.GONE);
		mTitleBarView.setBtnRight(R.drawable.btn_add_friend);
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, TodoAddActivity.class));
			}
		});

		mTitleBarView.setTitleLeft(R.string.todo_doing);
		mTitleBarView.setTitleRight(R.string.todo_end);

		mTitleBarView.getTitleLeft().setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (mTitleBarView.getTitleLeft().isEnabled()) {
					mTitleBarView.getTitleLeft().setEnabled(false);
					mTitleBarView.getTitleRight().setEnabled(true);
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					TodoListFragment newsFragment = new TodoListFragment();
					ft.replace(R.id.child_todo_fragment, newsFragment,
							TodoFragment.TAG);
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
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					TodoListFragment newsFragment = new TodoListFragment();
					ft.replace(R.id.child_todo_fragment, newsFragment,
							TodoFragment.TAG);
					ft.commit();
				}

			}
		});

		mTitleBarView.getTitleLeft().performClick();
	}
}
