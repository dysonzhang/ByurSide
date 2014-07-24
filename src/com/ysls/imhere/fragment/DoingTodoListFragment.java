package com.ysls.imhere.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ysls.imhere.R;
import com.ysls.imhere.adapter.DoingTodoListAdapter;
import com.ysls.imhere.bean.TodoModel;
import com.ysls.imhere.http.AsyncTaskBase;
import com.ysls.imhere.test.TestData;
import com.ysls.imhere.widget.CustomListView;
import com.ysls.imhere.widget.CustomListView.OnRefreshListener;
import com.ysls.imhere.widget.LoadingView;

public class DoingTodoListFragment extends Fragment  {
	
	private static final String TAG = "DoingTodoListFragment";
	
	private Context mContext;
	private View mBaseView;
	private CustomListView mCustomListView;
	private LoadingView mLoadingView;
 
	private DoingTodoListAdapter adapter;
	private LinkedList<TodoModel> todolist = new LinkedList<TodoModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_todolist, null);
		 
		findView();
		init();
		
		return mBaseView;
	}

	private void findView() {
		mCustomListView = (CustomListView) mBaseView.findViewById(R.id.lv_news);
		mLoadingView = (LoadingView) mBaseView.findViewById(R.id.loading);
	}

	private void init() {
		adapter = new DoingTodoListAdapter(mContext, todolist, mCustomListView);
		mCustomListView.setAdapter(adapter);
		mCustomListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new AsyncRefresh().execute(0);
			}
		});
		mCustomListView.setCanLoadMore(false);
		mCustomListView.setCanRefresh(false);
		new NewsAsyncTask(mLoadingView).execute(0);
	}

	private class NewsAsyncTask extends AsyncTaskBase {
		List<TodoModel> recentTodoLsit = new ArrayList<TodoModel>();

		public NewsAsyncTask(LoadingView loadingView) {
			super(loadingView);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			recentTodoLsit = TestData.getRecentChats();
			if (recentTodoLsit.size() > 0) {
				result = 1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			todolist.addAll(recentTodoLsit);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private class AsyncRefresh extends
			AsyncTask<Integer, Integer, List<TodoModel>> {
		private List<TodoModel> recentTodos = new ArrayList<TodoModel>();

		@Override
		protected List<TodoModel> doInBackground(Integer... params) {
			recentTodos = TestData.getRecentChats();
			return recentTodos;
		}

		@Override
		protected void onPostExecute(List<TodoModel> result) {
			super.onPostExecute(result);
			if (result != null) {
				for (TodoModel td : recentTodos) {
					todolist.addFirst(td);
				}
				adapter.notifyDataSetChanged();
				mCustomListView.onRefreshComplete();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}
