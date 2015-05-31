package com.iAcron.fragment;

import com.iAcon.response.BaseResponse;
import com.iAcron.R;
import com.iAcron.RegisterActivity;
import com.iAcron.RegisterPhoneCode;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterGetCodeFragment2 extends BaseFregment {

	EditText register_code_2_phone;
	Button register_code_2_daojishi;
	EditText register_code_2_code;
	Button register_code_2_next;

	MyCount myTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.register_code_2, null);
		register_code_2_phone = (EditText) main
				.findViewById(R.id.register_code_2_phone);
		register_code_2_daojishi = (Button) main
				.findViewById(R.id.register_code_2_daojishi);
		register_code_2_daojishi.setOnClickListener(listener2);
		register_code_2_code = (EditText) main
				.findViewById(R.id.register_code_2_code);
		register_code_2_next = (Button) main
				.findViewById(R.id.register_code_2_next);
		register_code_2_next.setOnClickListener(listener2);

		register_code_2_phone.setText(getArguments().getString("phone"));
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
				reqeustCode(register_code_2_phone.getText().toString());
				break;
			case R.id.register_code_2_next:

				String url = "http://112.74.95.154:8080/iAcron/validateCheckCode";
				RequestParams par = new RequestParams();
				par.put("mob_tel_no", register_code_2_phone.getText()
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
					bun.putString("code", register_code_2_code.getText().toString());
					bun.putString("phone", register_code_2_phone.getText().toString());
					
					in.putExtras(bun);
					in.setClass(activity, RegisterActivity.class);
					startActivity(in);
					activity.finish();
				}else{
					MyToast.showToast(activity, reponse.result_msg);
				}
			}

			@Override
			public void onAppError(int error, Throwable message) {

			}

			@Override
			public void onFailure(int error, Throwable message) {
			}

		});
		client.post(url, rp);
	}

	public void reqeustCode(String phone) {
		// 获取验证码
		String url = "http://112.74.95.154:8080/iAcron/reqCheckCode";
		RequestParams par = new RequestParams();
		par.put("mob_tel_no", register_code_2_phone.getText().toString());
		par.put("auth_usage", "1");
		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				BaseResponse reponse = new BaseResponse(activity);
				reponse.parse(data);
				MyToast.showToast(activity, reponse.result_msg);
				myTimer.start();
//				if (reponse.result == 1) {
//					LayoutBuilder lb = new LayoutBuilder(activity);
//					lb.replaceRegister(RegisterGetCodeFragment2.class, null);
					
//				}
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
