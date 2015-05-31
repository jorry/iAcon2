package com.iAcron;

import com.iAcron.data.DataBase;

import android.os.Bundle;

/**
 * 
 * @author jorry
 *
 */
public class ShiShiActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);
	}

	public void onHttpOnSuccess(String data) {
		// TODO Auto-generated method stub
		
	}
	public void onHttpFailure(int error) {
		// TODO Auto-generated method stub
		
	}
}
