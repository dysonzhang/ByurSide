package com.ysls.imhere.adapter;

import java.util.List;

import com.ysls.imhere.AddContactActivity;
import com.ysls.imhere.R;
import com.ysls.imhere.TodoDetailActivity;
import com.ysls.imhere.bean.TodoModel; 
import com.ysls.imhere.widget.CustomListView; 
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoListAdapter extends BaseAdapter  {
	protected static final String TAG = "TodoListAdapter";
	private Context mContext;
	private List<TodoModel> lists;
	private CustomListView mCustomListView;

	public TodoListAdapter(Context context, List<TodoModel> lists,
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
		TodoModel todo = lists.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_todolist_item,
					null);
			holder = new Holder();
			holder.iv = (ImageView) convertView.findViewById(R.id.user_picture);
			holder.tv_todo_title = (TextView) convertView
					.findViewById(R.id.todo_title);
			holder.tv_todo_content = (TextView) convertView
					.findViewById(R.id.todo_content);
			holder.tv_todo_time = (TextView) convertView
					.findViewById(R.id.todo_time);
			holder.tv_todo_out_time = (TextView) convertView
					.findViewById(R.id.todo_out_time);
			convertView.setTag(holder);
			
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.tv_todo_title.setText(todo.getTodoTitle());
		holder.tv_todo_content.setText(todo.getTodoContent());
		holder.tv_todo_time.setText(todo.getTodoTime());
		holder.tv_todo_out_time.setText(todo.getTodoOutTime());
		
		if(todo.getTodoTitle().equals("非常紧急任务")){
			holder.iv.setBackgroundResource(R.drawable.todo_one);
			holder.tv_todo_out_time.setTextColor(mContext.getResources().getColor(R.color.yellow));
		}else if(todo.getTodoTitle().equals("紧急任务")){
			holder.iv.setBackgroundResource(R.drawable.todo_two);
		}else {
			holder.iv.setBackgroundResource(R.drawable.todo_three);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext, TodoDetailActivity.class));
			}
		});
		
		
		return convertView;
	}

	class Holder {
		ImageView iv;
		TextView tv_todo_title;
		TextView tv_todo_content;
		TextView tv_todo_time;
		TextView tv_todo_out_time;
	}
 
}
