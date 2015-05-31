package com.iAcron;

import com.iAcron.data.Constants;
import com.iAcron.data.DataBase;
import com.iAcron.fragment.ContactListFragment;
import com.iAcron.fragment.JiaTingList;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.JiaTingDetailView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 
 * 家庭管理
 * 
 * @author jorry_liu
 * 
 */
public class JiaTingMangerActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiatingmanager);
		LayoutBuilder lb = new LayoutBuilder(this);
		lb.replaceRegister(JiaTingList.class, null);
		// getSupportFragmentManager().beginTransaction().add(R.id.contactfragment,
		// new JiaTingList(),
		// JiaTingList.class.getSimpleName()).addToBackStack(JiaTingList.class.getSimpleName()).commitAllowingStateLoss();
	}

	public void startWaitDialog(String title, String message, boolean bCancel) {
		// TODO Auto-generated method stub
		super.startWaitDialog(title, message, bCancel);
	}

	/*
	 * 8
	 * 
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) {
	 * System.out.println("qingqingNEWs .java   dis"); if (event.getKeyCode() ==
	 * KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
	 * long secondTime = System.currentTimeMillis(); if (secondTime - firstTime
	 * > 2000) {// 如果两次按键时间间隔大于2秒，则不退出 firstTime = secondTime;// 更新firstTime
	 * Toast.makeText(QinQingNews.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	 * } else { finish(); } return true; } return super.dispatchKeyEvent(event);
	 * }
	 */
	
	@Override 
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			int fc = getSupportFragmentManager().getBackStackEntryCount();
			if (fc >1) {
				onStart();
				getSupportFragmentManager().popBackStackImmediate();
				return true;
			} else {
				return false;
			}

		}
		return super.dispatchKeyEvent(event);
	}


	
	
	long firstTime;

	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("onKeyDown"+keyCode);
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (getSupportFragmentManager().getBackStackEntryCount() <=1) {
				long secondTime = System.currentTimeMillis();
				if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
					firstTime = secondTime;// 更新firstTime
					Toast.makeText(JiaTingMangerActivity.this, "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
				} else {
					finish();
				}
			}else{
				onStart();
				if (getSupportFragmentManager() != null) {
					getSupportFragmentManager().popBackStackImmediate();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
