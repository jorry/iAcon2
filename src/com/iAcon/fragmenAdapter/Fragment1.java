package com.iAcon.fragmenAdapter;

import com.iAcron.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1 extends Fragment {

	public static Fragment1 instantiation(int position) {
		Fragment1 fragment = new Fragment1();
		Bundle args = new Bundle();
		args.putInt("position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int position = getArguments().getInt("position");
		System.out.println("position = "+position);
//		return inflater.inflate(R.layout.fragment_test, container, false);
		return null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}


	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}
}
