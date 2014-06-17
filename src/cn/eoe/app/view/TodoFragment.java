package cn.eoe.app.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TodoFragment extends Fragment {
	private Activity mActivity;

	public TodoFragment() {
	}

	public TodoFragment(Activity paramActivity) {
		this.mActivity = paramActivity;
	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		return paramLayoutInflater.inflate(2130903058, null);
	}
}