package com.iAcron;

import com.iAcon.response.BaseResponse;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 密码重置
 * 
 * @author jorry
 * 
 */
public class ResetPassword extends BaseActivity {

	EditText password;
	EditText configPassword;
	Button okBt;
	String phone, checkCode;
	int show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.resetpasword);

		phone = getIntent().getExtras().getString("phone");
		checkCode = getIntent().getExtras().getString("checkCode");
		password = (EditText) findViewById(R.id.passwordet);
		configPassword = (EditText) findViewById(R.id.configpasword);
		okBt = (Button) findViewById(R.id.config);
		okBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isNull(password) || isNull(configPassword)) {
					return;
				}
				if(!password.getText().toString().equals(configPassword.getText().toString())){
					MyToast.showToast(ResetPassword.this,"两次密码不同");
					return;
				}
				reqeust();
			}
		});
		TextView title = (TextView) findViewById(R.id.topbar_title);
		title.setText("重置密码");
		findViewById(R.id.menuBtn).setVisibility(View.GONE);
		Button homeBtn = (Button) findViewById(R.id.homeBtn);
		homeBtn.setBackgroundResource(R.drawable.u8);
		homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	public boolean isNull(EditText str) {
		return TextUtils.isEmpty(str.getText().toString());
	}

	public void reqeust() {
		RequestParams par = new RequestParams();
		String url = "http://112.74.95.154:8080/iAcron/resetPassword";
		startWaitDialog("网络连接", "努力加载中.....", true);
		par.put("apply_type", "1");
		par.put("new_pwd", password.getText().toString());
		par.put("check_code", checkCode);
		par.put("login_name", phone);

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
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
		client.post(url, par);
	}

	protected void onHttpFailure(int error) {

	}

	protected void onHttpOnSuccess(String data) {
		BaseResponse b = new BaseResponse(this);
		b.parse(data);
		if (b.result == 1) {
			finish();
		} else {
			MyToast.showToast(this, b.result_msg);
		}
	}
}
