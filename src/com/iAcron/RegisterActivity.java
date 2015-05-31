package com.iAcron;

import com.iAcon.response.BaseResponse;
import com.iAcron.data.AppData;
import com.iAcron.data.DataBase;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LogUtil;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends BaseActivity {

	private EditText nickname;
	private EditText password;
	private EditText configpass;
	private EditText name;
	private EditText age;
	private EditText address;
	private EditText tel;
	private EditText phone;
	private EditText qq;
	private EditText brith;
	private EditText email;

	private RadioGroup radioGroup1;
	private RadioButton nan, nv;
	private String check_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.land_register);
		check_code = getIntent().getExtras().getString("code");

		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		nan = (RadioButton) findViewById(R.id.radio_nan);
		nv = (RadioButton) findViewById(R.id.radio_nv);

		findViewById(R.id.menuBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		nickname = (EditText) findViewById(R.id.register_nickname);
		password = (EditText) findViewById(R.id.register_password);
		email = (EditText) findViewById(R.id.register_email);
		configpass = (EditText) findViewById(R.id.register_configpass);
		name = (EditText) findViewById(R.id.register_name);
		brith = (EditText) findViewById(R.id.register_brith);
		brith.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					DatePickerDialog date = new DatePickerDialog(
							RegisterActivity.this, new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker arg0,
										int arg1, int arg2, int arg3) {
									// TODO Auto-generated method stub
									arg2 += 1;
									brith.setText(arg1 + "-" + arg2 + "-"
											+ arg3);
								}
							}, 2000, 1, 1);
					date.show();
					return true;
				}
				return false;
			}
		});

		age = (EditText) findViewById(R.id.register_age);
		address = (EditText) findViewById(R.id.register_address);

		phone = (EditText) findViewById(R.id.register_phone);
		phone.setText(getIntent().getExtras().getString("phone"));
		tel = (EditText) findViewById(R.id.register_tel);
		qq = (EditText) findViewById(R.id.register_qq);

	}

	/**
	 * 
	 * user_name 必须 String 用户名 mob_tel_no 必须 String 用户手机号 email String 用户电子邮件
	 * pwd 必须 String 密码 check_code 必须 String
	 * 为防止恶意注册，需要在注册是提供验证码（在页面注册时通过该方式，通过手机注册时，此处即收到的短信验证码。手机验证码需要加强。）
	 * 
	 * user_ident String 用户身份证号 sex Int 性别 birthday Date 生日，格式yyyy-MM-dd qq_no
	 * String QQ号 user_desc string 用户说明 nick_name string 姓名 addr string 地址
	 * home_tel string 家庭电话 office_tel string 办公室电话
	 * 
	 * @param view
	 */
	public void registerBt(View view) {

		if (isNull(nickname)) {
			MyToast.showToast(this, "填写用户名");
			return;
		}
		if (isNull(password)) {
			MyToast.showToast(this, "填写密码");

			return;
		}
		if (isNull(configpass)) {
			MyToast.showToast(this, "填写确认密码");

			return;
		}
		if (isNull(name)) {
			MyToast.showToast(this, "填写姓名");

			return;
		}
		
		
		if (isNull(phone)) {
			MyToast.showToast(this, "填写手机号");

			return;
		}
		if (isNull(email)) {
			MyToast.showToast(this, "填写邮箱");

			return;
		}
		
		if (isNull(brith)) {
			MyToast.showToast(this, "出生日期");
			return;
		}


		
		System.out.println(password.getText().toString() + "  密码 "
				+ configpass.getText().toString());
		if (!password.getText().toString()
				.equals(configpass.getText().toString())) {
			MyToast.showToast(this, "密码不一致，请检查");
			return;

		}
		LogUtil.i("user", "nan-Checked" + nan.isChecked());
		RequestParams pa = new RequestParams();
		pa.put("user_name", nickname.getText().toString());
		pa.put("mob_tel_no", phone.getText().toString());
		pa.put("email", email.getText().toString());
		pa.put("pwd", password.getText().toString());
		pa.put("check_code", check_code);
		int sex = nan.isChecked() ? 1 : 2;

		pa.put("sex", "" + sex);
		pa.put("birthday", brith.getText().toString());
		pa.put("home_tel", tel.getText().toString());
		pa.put("addr", address.getText().toString());
		pa.put("qq_no", qq.getText().toString());
		pa.put("nick_name", name.getText().toString());
		pa.put("addr", address.getText().toString());
		pa.put("home_tel", tel.getText().toString());
		reqeust("http://112.74.95.154:8080/iAcron/register", pa);
	}

	public boolean isNull(EditText str) {
		return TextUtils.isEmpty(str.getText().toString());
	}

	RequstClient client;

	public void reqeust(String url, RequestParams rp) {
		startWaitDialog(null, null, false);
		client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				finishWaitDialog();
				onHttpOnSuccess(data);
			}

			@Override
			public void onAppError(int error, Throwable message) {
				finishWaitDialog();
				
			}

			@Override
			public void onFailure(int error, Throwable message) {
				finishWaitDialog();
				
				onHttpFailure(error);
			}

		});
		client.post(url, rp);
	}

	public void onHttpOnSuccess(String data) {
		BaseResponse reponse = new BaseResponse(RegisterActivity.this);
		reponse.parse(data);
		if (reponse.result == 1) {
			finish();
		}else{
			MyToast.showToast(RegisterActivity.this, reponse.result_msg);
		}
	}

	public void onHttpFailure(int error) {
		// TODO Auto-generated method stub

	}

}
