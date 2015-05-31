package com.iAcron;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.model.BindWardBean;
import com.iAcron.data.DataBase;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.HttpResonseHandler;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.ThreadUtil;
import com.loopj.android.http.RequestParams;

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
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 测试
 * 
 * @author jorry_liu
 * 
 */
public abstract class BaseActivity extends BaseFragmentActivity {

	private Context con;
	iAconApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.con = this;
		app = (iAconApplication) getApplication();
	}

	

	// Activity基类
	private List<Pair<Integer, Handler.Callback>> callbacks = new ArrayList<Pair<Integer, Callback>>();

	/**
	 * 注册广播
	 * 
	 * @param msg
	 *            唯一标识
	 * @param callback
	 *            回调方法
	 */
	protected void regMsg(int msg, Handler.Callback callback) {
		ThreadUtil.addMessageListener(msg, callback);
		callbacks.add(new Pair<Integer, Handler.Callback>(msg, callback));
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

	public void showBaseToast(String message) {
		Toast.makeText(con, message, Toast.LENGTH_LONG).show();
		;
	}

	public void startBaseActivity(Class<?> c) {
		Intent in = new Intent(this, c);
		startActivity(in);
	}

}
