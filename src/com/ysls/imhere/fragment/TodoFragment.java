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
import android.widget.Toast;

public class TodoFragment extends Fragment {
	private static final String TAG = "TodoFragment";
	private Context mContext;
	private View mBaseView;
 
	private TitleBarView mTitleBarView; 
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
		mCanversLayout = (RelativeLayout) mBaseView
				.findViewById(R.id.rl_todo_canvers);
		
	}

	private void init() {
		mTitleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);
		mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "setBtnRightOnclickListener", Toast.LENGTH_LONG).show();
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

					Toast.makeText(mContext, "进行中", Toast.LENGTH_LONG).show();
					
					FragmentTransaction ft = getFragmentManager().beginTransaction();
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
					
					Toast.makeText(mContext, "已结束", Toast.LENGTH_LONG).show();
					
					FragmentTransaction ft = getFragmentManager().beginTransaction();
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
