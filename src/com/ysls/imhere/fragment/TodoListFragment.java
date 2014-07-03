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
import com.ysls.imhere.adapter.NewsAdapter;
import com.ysls.imhere.bean.RecentChat;
import com.ysls.imhere.http.AsyncTaskBase;
import com.ysls.imhere.test.TestData;
import com.ysls.imhere.widget.CustomListView;
import com.ysls.imhere.widget.CustomListView.OnRefreshListener;
import com.ysls.imhere.widget.LoadingView;

public class TodoListFragment extends Fragment {
	
	private static final String TAG = "TodoListFragment";
	
	private Context mContext;
	private View mBaseView;
	private CustomListView mCustomListView;
	private LoadingView mLoadingView;
 
	private NewsAdapter adapter;
	private LinkedList<RecentChat> chats = new LinkedList<RecentChat>();

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
		adapter = new NewsAdapter(mContext, chats, mCustomListView);
		mCustomListView.setAdapter(adapter);
		mCustomListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new AsyncRefresh().execute(0);
			}
		});
		mCustomListView.setCanLoadMore(false);
		new NewsAsyncTask(mLoadingView).execute(0);
	}

	private class NewsAsyncTask extends AsyncTaskBase {
		List<RecentChat> recentChats = new ArrayList<RecentChat>();

		public NewsAsyncTask(LoadingView loadingView) {
			super(loadingView);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			int result = -1;
			recentChats = TestData.getRecentChats();
			if (recentChats.size() > 0) {
				result = 1;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			chats.addAll(recentChats);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	private class AsyncRefresh extends
			AsyncTask<Integer, Integer, List<RecentChat>> {
		private List<RecentChat> recentchats = new ArrayList<RecentChat>();

		@Override
		protected List<RecentChat> doInBackground(Integer... params) {
			recentchats = TestData.getRecentChats();
			return recentchats;
		}

		@Override
		protected void onPostExecute(List<RecentChat> result) {
			super.onPostExecute(result);
			if (result != null) {
				for (RecentChat rc : recentchats) {
					chats.addFirst(rc);
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
