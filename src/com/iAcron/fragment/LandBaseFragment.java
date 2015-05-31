package com.iAcron.fragment;

import com.iAcron.fragment.IBtnCallListener.IBtnCallListener;
import com.iAcron.util.LogUtil;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class LandBaseFragment extends BaseFregment implements IBtnCallListener{

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void email() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void phone() {
		// TODO Auto-generated method stub
		
	}
	IBtnCallListener  mbtnListener;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		try {		
			mbtnListener=(IBtnCallListener) activity;
			LogUtil.i("UI", "onaTTACH");
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString() + "must implement mbtnListener");
		}
		
		super.onAttach(activity);
	}

	@Override
	public void land(int selectEvent) {
		// TODO Auto-generated method stub
		
	}
}
