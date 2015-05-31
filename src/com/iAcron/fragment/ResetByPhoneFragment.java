package com.iAcron.fragment;

import com.iAcon.response.BaseResponse;
import com.iAcron.BaseActivity;
import com.iAcron.R;
import com.iAcron.data.DataBase;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ViewFlipper;

/**
 * 邮箱和手机号找回密码
 * 
 * @author jorry_liu
 * 
 */
public class ResetByPhoneFragment extends BaseFregment implements
		OnClickListener {
	// 第一个页面

	EditText landphone_nickname;
	EditText landphone_phone;

	Button land_phone_getcode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.land_phone_main, null);
		// 手机号
		landphone_nickname = (EditText) view
				.findViewById(R.id.landphone_nickname);
		landphone_phone = (EditText) view.findViewById(R.id.landphone_phone);
		view.findViewById(R.id.land_phone_getcode).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!TextUtils.isEmpty(landphone_nickname.getText().toString())&&!TextUtils.isEmpty(landphone_phone.getText().toString())){
					reqeust();
				}
			}
		});
		return view;
	}


	int show;

	public void reqeust() {
		
		((BaseActivity) activity).startWaitDialog("网络连接", "努力加载中.....", true);
		
		RequestParams par = new RequestParams();
		String url = "http://112.74.95.154:8080/iAcron/reqCheckCode";
		
		par.put("mob_tel_no", landphone_phone.getText().toString());

		par.put("auth_usage", "2");

		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				((BaseActivity) activity).finishWaitDialog();
				onHttpOnSuccess(data);
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				((BaseActivity) activity).finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				((BaseActivity) activity).finishWaitDialog();
				onHttpFailure(error);
			}

		});
		client.post(url, par);
	}

	public void onHttpOnSuccess(String data) {
		BaseResponse br = new BaseResponse(activity);
		br.parse(data);
		if(br.result==1){
			Bundle b = new Bundle();
			b.putString("nickname", landphone_nickname.getText().toString());
			b.putString("phone", landphone_phone.getText().toString());
			LayoutBuilder lb  = new LayoutBuilder(activity);
			lb.replaceRegister(ResetGetCodeFragment2.class, b);
		}else{
			MyToast.showToast(activity, br.result_msg);
		}
	}

	public void onHttpFailure(int error) {

	}

	@Override
	public void onClick(View arg0) {
		
	}
}
