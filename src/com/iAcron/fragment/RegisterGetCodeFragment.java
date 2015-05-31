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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * 获取验证码S
 * 
 * @author jorry
 * 
 */
public class RegisterGetCodeFragment extends BaseFregment {

	EditText code_edit;
	Button register_code1_getcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.register_code, null);
		code_edit = (EditText) main.findViewById(R.id.code_edit);
		register_code1_getcode = (Button) main
				.findViewById(R.id.register_code1_getcode);
		register_code1_getcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(code_edit.getText().toString())) {
					return;
				}
				reqeustCode(code_edit.getText().toString());

			}
		});

		return main;
	}

	public void reqeustCode(String phone) {

		activity.startWaitDialog("提示", "网络连接", false);
		// 获取验证码
		String url = "http://112.74.95.154:8080/iAcron/reqCheckCode";
		RequestParams par = new RequestParams();
		par.put("mob_tel_no", code_edit.getText().toString());
		par.put("auth_usage", "1");

		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				activity.finishWaitDialog();

				BaseResponse reponse = new BaseResponse(activity);
				reponse.parse(data);
				if (reponse.result == 1) {
					LayoutBuilder lb = new LayoutBuilder(activity);
					Bundle b = new Bundle();
					b.putString("phone", code_edit.getText().toString());
					lb.replaceRegister(RegisterGetCodeFragment2.class, b);
				}else{
					MyToast.showToast(activity, reponse.result_msg);
				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				activity.finishWaitDialog();

			}

			@Override
			public void onFailure(int error, Throwable message) {
				activity.finishWaitDialog();

			}

		});
		client.post(url, par);
	}

}
