package com.ysls.imhere.adapter;

import java.util.List;

import com.ysls.imhere.R;
import com.ysls.imhere.bean.RecentChat;
import com.ysls.imhere.widget.CustomListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	protected static final String TAG = "NewsAdapter";
	private Context mContext;
	private List<RecentChat> lists;
	private CustomListView mCustomListView;

	public NewsAdapter(Context context, List<RecentChat> lists,
			CustomListView customListView) {
		this.mContext = context;
		this.lists = lists;
		this.mCustomListView = customListView;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		RecentChat chat = lists.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_todolist_item,
					null);
			holder = new Holder();
			holder.iv = (ImageView) convertView.findViewById(R.id.user_picture);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.tv_feel = (TextView) convertView
					.findViewById(R.id.user_feel);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.user_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.iv.setBackgroundResource(R.drawable.point_select);

		holder.tv_name.setText(chat.getUserName());
		holder.tv_feel.setText(chat.getUserFeel());
		holder.tv_time.setText(chat.getUserTime());

		return convertView;
	}

	class Holder {
		ImageView iv;
		TextView tv_name;
		TextView tv_feel;
		TextView tv_time;
	}

}
