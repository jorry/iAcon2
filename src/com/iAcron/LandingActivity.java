package com.iAcron;

import cn.jpush.android.api.InstrumentedActivity;

import com.baidu.platform.comapi.map.u;
import com.iAcon.response.ResonseOfLogin;
import com.iAcron.data.AppData;
import com.iAcron.data.DataBase;
import com.iAcron.fragment.DFragment;
import com.iAcron.fragment.IBtnCallListener.IBtnCallListener;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LogUtil;
import com.iAcron.util.PreferenceUtil;
import com.iAcron.view.MMAlert;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.preference.EditTextPreference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登陆模块 ！{@link myangelventure@gmail.com}
 * 
 * @author jorry
 * 
 */

public class LandingActivity extends InstrumentedActivity implements Callback {

	private EditText passwordEt;
	private EditText userNameEt;

	PreferenceUtil pu;

	Handler uiHandler = new Handler(Looper.getMainLooper(), this);

	private Context context;

	private boolean isZhidong;
	private boolean isJizhu;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.context = this;
		setContentView(R.layout.land_layout);

		AppData.init(this);
		passwordEt = (EditText) findViewById(R.id.landpassword_commit);
		userNameEt = (EditText) findViewById(R.id.landusername_commit);
		CheckBox dengluCheck = (CheckBox) findViewById(R.id.landdenglu_checked); // 自动登录
		CheckBox jizhuCheck = (CheckBox) findViewById(R.id.landjizhu_checked); // 记住密码
		dengluCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() { // 自动登录

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						isZhidong = arg1;
					}
				});
		jizhuCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() { // 记住密码

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						isJizhu = arg1;
					}
				});

		jizhuCheck.setChecked(AppData.jzmima.get());
		dengluCheck.setChecked(AppData.zidonglogin.get());

		userNameEt.setText(AppData.userName.get());
		// 如果是记住密码
		if (AppData.jzmima.get()) {
			passwordEt.setText(AppData.passWord.get());

		}

		// 如果是自动登录并且用户名不为空
		if (AppData.zidonglogin.get()
				&& !TextUtils.isEmpty(AppData.userName.get())) { // 自动登录
			RequestParams pa = new RequestParams();
			pa.put("login_name", AppData.userName.get());
			pa.put("pwd", AppData.passWord.get());
			reqeust("http://112.74.95.154:8080/iAcron/login", pa);
		}

	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void landBt(View view) {
		if (isEmpty(userNameEt)) {
			return;
		}
		if (isEmpty(passwordEt)) {
			return;
		}

		RequestParams pa = new RequestParams();
		pa.put("login_name", userNameEt.getText().toString());
		pa.put("pwd", passwordEt.getText().toString());
		reqeust("http://112.74.95.154:8080/iAcron/login", pa);

	}

	public boolean isEmpty(TextView tv) {
		return TextUtils.isEmpty(tv.getText().toString());
	}

	Dialog dialog;

	/**
	 * 忘记密码
	 * 
	 * @param view
	 */
	public void fogertBt(View view) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.alert_dialog_menu_layout, null);
		layout.findViewById(R.id.alert_cancel).setOnClickListener(
				myAlertListener);

		layout.findViewById(R.id.alert_email).setOnClickListener(
				myAlertListener);
		layout.findViewById(R.id.alert_phone).setOnClickListener(
				myAlertListener);

		dialog = MMAlert.showAlert(context, layout);
	}

	private OnClickListener myAlertListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			dialog.cancel();
			
			if (v.getId() == R.id.alert_cancel) {
			} else if (v.getId() == R.id.alert_email) {
				Intent in = new Intent();
				in.putExtra("show", 1);
				in.setClass(LandingActivity.this, FindPasswordActivity.class);
				startActivity(in);
			} else if (v.getId() == R.id.alert_phone) {
				Intent in = new Intent();
				in.putExtra("show", 2);
				in.setClass(LandingActivity.this, FindPasswordActivity.class);
				startActivity(in);
			}
			
		}
	};

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void registerBt(View view) {
		Intent in = new Intent();
		in.setClass(LandingActivity.this, RegisterPhoneCode.class);
		startActivity(in);
	}

	RequstClient client;

	public void reqeust(String url, RequestParams rp) {
		startWaitDialog("网络连接", "努力加载中.....", true);
		client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				
				finishWaitDialog();
				onHttpOnSuccess(data);
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
				onHttpFailure(error);
			}

		});
		client.post(url, rp);
	}

	public void onHttpOnSuccess(String data) {
		// TODO Auto-generated method stub
		ResonseOfLogin l = new ResonseOfLogin(LandingActivity.this);
		l.parse(data);
		if (l.result == 1) {
			AppData.userName.set(userNameEt.getText().toString());
			if (isJizhu) {
				AppData.passWord.set(passwordEt.getText().toString());
			}
			AppData.jzmima.set(isJizhu);
			AppData.zidonglogin.set(isZhidong);
			Intent in = new Intent(LandingActivity.this, MainActivity.class);
			startActivity(in);
			finish();
		} else {
			showBaseToast(l.result_msg);
		}
		LogUtil.i("net", l.result + " 登录返回 " + l.result_msg);
	}
	
	public void showBaseToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		;
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
		if(title==null){
			myDialog.setTitle("网络连接");
		}else{
			myDialog.setTitle(title);
		}
		
		if(message==null){
			myDialog.setTitle("努力加载中.....");
		}else{
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

	public void onHttpFailure(int error) {
		// TODO Auto-generated method stub
		LogUtil.i("net", "网络失败" + error);
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}