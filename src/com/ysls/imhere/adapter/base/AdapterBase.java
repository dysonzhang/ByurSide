package com.ysls.imhere.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.LinkedList;
import java.util.List;

public abstract class AdapterBase<T> extends BaseAdapter {
	private final List<T> mList = new LinkedList();

	public void appendToList(List<T> paramList) {
		if (paramList == null)
			return;
		this.mList.addAll(paramList);
		notifyDataSetChanged();
	}

	public void appendToTopList(List<T> paramList) {
		if (paramList == null)
			return;
		this.mList.addAll(0, paramList);
		notifyDataSetChanged();
	}

	public void clear() {
		this.mList.clear();
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.mList.size();
	}

	protected abstract View getExView(int paramInt, View paramView,
			ViewGroup paramViewGroup);

	public Object getItem(int paramInt) {
		if (paramInt > -1 + this.mList.size())
			return null;
		return this.mList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public List<T> getList() {
		return this.mList;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (paramInt == -1 + getCount())
			onReachBottom();
		return getExView(paramInt, paramView, paramViewGroup);
	}

	protected abstract void onReachBottom();
}