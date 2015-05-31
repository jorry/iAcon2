package com.iAcron;

import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.manager.JianhuManager;
import com.iAcon.database.manager.BindWardBeanManager;
import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.wardinfo;
import com.iAcon.response.JianHuListResponse;
import com.iAcon.response.queryWardGuardianListResponse;
import com.iAcon.response.bean.queryWardGuardianList;
import com.iAcron.fragment.JianHuOnconfigFragmetn;
import com.iAcron.fragment.JiangHuJuJueFragment;
import com.iAcron.fragment.JiangHuTongyiFragment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.widget.Toast;

public class JianHuActivity extends BaseFragmentActivity {

	FragmentTabHost tabHost;
	private String msg;
	int ward_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.jianhu);
		tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		addTabView();
	}

	public void addTabView() {

		tabHost.setup(this, getSupportFragmentManager(), R.id.finish);

		// 0
		tabHost.addTab(tabHost.newTabSpec("finish").setIndicator("未处理"),
				JianHuOnconfigFragmetn.class, null);
		// 1
		tabHost.addTab(tabHost.newTabSpec("tongyi").setIndicator("已同意"),
				JiangHuTongyiFragment.class, null);
		// 2
		tabHost.addTab(tabHost.newTabSpec("jujue").setIndicator("已拒绝"),
				JiangHuJuJueFragment.class, null);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	

}