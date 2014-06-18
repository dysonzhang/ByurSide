package com.ysls.imhere.view;

import com.ysls.imhere.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HttpErrorFragment extends Fragment {
	public View onCreateView(LayoutInflater layoutInflater,
			ViewGroup viewGroup, Bundle bundle) {
		return layoutInflater.inflate(R.layout.http_error_fragment, null);
	}
}