package com.iAcron.fragment;

import com.iAcon.response.BaseResponse;
import com.iAcron.R;
import com.iAcron.RegisterActivity;
import com.iAcron.RegisterPhoneCode;
import com.iAcron.ResetPassword;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ResetGetCodeFragment2 extends BaseFregment {

	EditText password_getcode_nickname;
	Button register_code_2_daojishi;
	EditText register_code_2_code;
	Button register_code_2_next;

	EditText password_getcode_phone;
	
	MyCount myTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.reset_password_getcode, null);
		password_getcode_nickname = (EditText) main
				.findViewById(R.id.password_getcode_nickname);
		password_getcode_phone = (EditText)main.findViewById(R.id.password_getcode_phone);
		register_code_2_daojishi = (Button) main
				.findViewById(R.id.register_code_2_daojishi);
		
		register_code_2_daojishi.setOnClickListener(listener2);
		register_code_2_code = (EditText) main
				.findViewById(R.id.register_code_2_code);
		register_code_2_next = (Button) main
				.findViewById(R.id.register_code_2_next);
		register_code_2_next.setOnClickListener(listener2);

		password_getcode_phone.setText(getArguments().getString("phone"));
		password_getcode_nickname.setText(getArguments().getString("nickname"));
		myTimer = new MyCount(60 * 1000, 1000);
		myTimer.start();
		return main;
	}

	private OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.register_code_2_daojishi:
				reqeustCode(password_getcode_phone.getText().toString());
				break;
			case R.id.register_code_2_next:
				if(TextUtils.isEmpty(register_code_2_code.getText().toString())){
					MyToast.showToast(activity, "请输入验证码");
					return;
				}
				String url = "http://112.74.95.154:8080/iAcron/validateCheckCode";
				RequestParams par = new RequestParams();
				par.put("mob_tel_no", password_getcode_phone.getText()
						.toString());
				par.put("check_code", register_code_2_code.getText().toString());

				reqeust2(url, par);

				break;
			default:
				break;
			}
		}
	};

	RequstClient client;

	public void reqeust2(String url, RequestParams rp) {
		client = new RequstClient(activity, new HttpCallBack() {
			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				BaseResponse reponse = new BaseResponse(activity);
				reponse.parse(data);
				if (reponse.result == 1) {
					Intent in = new Intent();
					Bundle bun = new Bundle();
					bun.putString("checkCode", register_code_2_code.getText().toString());
					bun.putString("phone", password_getcode_phone.getText().toString());
					
					in.putExtras(bun);
					in.setClass(activity, ResetPassword.class);
					startActivity(in);
					activity.finish();
				}
			}

			@Override
			public void onAppError(int error, Throwable message) {

			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
			}

		});
		client.post(url, rp);
	}

	public void reqeustCode(String phone) {
		// 获取验证码
		String url = "http://112.74.95.154:8080/iAcron/reqCheckCode";
		RequestParams par = new RequestParams();
		par.put("mob_tel_no", password_getcode_phone.getText().toString());
		par.put("auth_usage", "1");
		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				BaseResponse reponse = new BaseResponse(activity);
				reponse.parse(data);
				MyToast.showToast(activity, reponse.result_msg);
				myTimer.start();
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
			}

		});
		client.post(url, par);

		register_code_2_daojishi.setEnabled(false); // 点击获取验证码
		myTimer.start();

	}

	/**
	 * 定时
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			register_code_2_daojishi.setText("重新获取验证码");
			register_code_2_daojishi.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			register_code_2_daojishi.setText("重新获取验证码(" + millisUntilFinished
					/ 1000 + ")");
		}

	}
}
