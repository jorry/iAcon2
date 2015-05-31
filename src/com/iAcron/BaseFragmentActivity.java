package com.iAcron;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.model.BindWardBean;
import com.iAcron.util.ThreadUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class BaseFragmentActivity extends FragmentActivity {

	// Activity基类
	private List<Pair<Integer, Handler.Callback>> callbacks = new ArrayList<Pair<Integer, Callback>>();
	Context con;
	iAconApplication app;
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.con = this;
		app = (iAconApplication) getApplication();
	}

	protected void regMsg(int msg, Handler.Callback callback) {
		ThreadUtil.addMessageListener(msg, callback);
		callbacks.add(new Pair<Integer, Handler.Callback>(msg, callback));
	}

	@Override
	protected void onResume() {
		RelativeLayout tell = (RelativeLayout) ((Activity) con)
				.findViewById(R.id.tellayout);
		if (tell != null) {
			tell.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (app.bindWardLists == null
							|| app.bindWardLists.size() == 0) {
						return;
					}
					BindWardBean bean = app.bindWardLists
							.get(app.bindWardSelect);
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + bean.getWatch_tel_no()));
					startActivity(intent);
				}
			});
		}

		RelativeLayout chatlayout = (RelativeLayout) ((Activity) con)
				.findViewById(R.id.chatlayout);
		if (chatlayout != null) {
			chatlayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (app.bindWardLists == null
							|| app.bindWardLists.size() == 0) {
						return;
					}
					Intent in = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("obj",app.bindWardLists.get(app.bindWardSelect));
					in.putExtras(b);
					
					
					in.setClass(BaseFragmentActivity.this, ChatMessage.class);
					startActivity(in);
				}
			});
		}

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (Pair<Integer, Callback> p : callbacks) {
			ThreadUtil.removeMessageListener(p.first, p.second);
		}
		System.out.println("删除   广播");
		callbacks.clear();
	}

	ProgressDialog myDialog;

	/**
	 * 圆形Dialog
	 * 
	 * @param title
	 *            提示
	 * @param message
	 *            标题
	 * @param bCancel
	 *            返回键是否取消dialog
	 */
	public void startWaitDialog(String title, String message, boolean bCancel) {
		finishWaitDialog();
		myDialog = new ProgressDialog(this);
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (title == null) {
			myDialog.setTitle("网络连接");
		} else {
			myDialog.setTitle(title);
		}

		if (message == null) {
			myDialog.setTitle("努力加载中.....");
		} else {
			myDialog.setTitle(message);
		}
		myDialog.setCancelable(bCancel);
		myDialog.show();
	}

	public void finishWaitDialog() {
		if (myDialog != null) {
			myDialog.dismiss();
			myDialog = null;
		}
	}
}
