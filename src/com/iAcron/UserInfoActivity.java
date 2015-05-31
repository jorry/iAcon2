package com.iAcron;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.iAcon.response.fetchWardTelBookResponse;
import com.iAcon.response.getUserInfoResponse;
import com.iAcron.data.AppData;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.view.MyToast;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 
 * @author jorry
 * 
 */
public class UserInfoActivity extends BaseActivity {

	EditText userName;
	EditText shenfen;
	EditText email;
	EditText phoneCode;
	EditText qq;
	RadioGroup user_groupsex;
	EditText brith;
	EditText qianming;

	int sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		
		findViewById(R.id.homeBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		findViewById(R.id.contact_add_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUserInfo();
			}
		});
		userName = (EditText) findViewById(R.id.user_name);
		shenfen = (EditText) findViewById(R.id.user_shenfen);
		email = (EditText) findViewById(R.id.user_email);
		phoneCode = (EditText) findViewById(R.id.user_phonecode);
		qq = (EditText) findViewById(R.id.user_qq);
		user_groupsex = (RadioGroup) findViewById(R.id.user_groupsex);
		brith = (EditText) findViewById(R.id.user_brith);
		brith.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					showDate();
					return true;
				}
				return false;
			}
		});

		qianming = (EditText) findViewById(R.id.user_qianming);

		user_groupsex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (R.id.nan == arg1) {
					sex = 1;
				} else if (R.id.nv == arg1) {
					sex = 2;
				}
			}
		});
		
		getUserinfo();
	}

	/**
	 * 	EditText userName;
	EditText shenfen;
	EditText email;
	EditText phoneCode;
	EditText qq;
	RadioGroup user_groupsex;
	EditText brith;
	EditText qianming;

	 */
	public void initEdit(getUserInfoResponse bean){
		userName.setText(bean.user.getUser_name());
		email.setText(bean.user.getEmail());
		phoneCode.setText(bean.user.getMob_tel_no());
		qq.setText(bean.user.getQq_no());
		brith.setText(bean.user.getBirthday());
		int sex = bean.user.getSex()==1?1:2;
		if(sex==1){
			user_groupsex.getChildAt(0).setClickable(true);
		}else{
			user_groupsex.getChildAt(1).setClickable(true);
		}
		qianming.setText(bean.user.getUser_desc());
		
	}
	public void getUserinfo() {
		Common rp = new Common();
		rp.setKV("guard_id", AppData.user_id.get());
		RequstClient client = new RequstClient(this,
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						getUserInfoResponse base = new getUserInfoResponse(
							UserInfoActivity.this);
						base.parse(data);
						if (base.result == 1) {
							initEdit(base);
						} else {
							MyToast.showToast(UserInfoActivity.this, base.result_msg);
						}
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
		client.post("http://112.74.95.154:8080/iAcron/getUserInfo", rp.getParams());
	}
	
	/**
	 * user_ident		String	用户身份证号
sex		Int	性别
birthday		Date	生日，格式yyyy-MM-dd
qq_no		String	QQ号
user_desc		string	用户说明
nick_name		string	姓名
addr		string	地址
home_tel		string	家庭电话
office_tel		string	办公室电话

	 */
	
	public void updateUserInfo() {
		startWaitDialog("请求",null, false);
		Common rp = new Common();
		rp.setKV("sex", ""+sex);
		rp.setKV("birthday", brith.getText().toString());
		rp.setKV("qq_no", brith.getText().toString());
		rp.setKV("user_desc", brith.getText().toString());
		rp.setKV("nick_name", brith.getText().toString());
		
		RequstClient client = new RequstClient(this,
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						finishWaitDialog();
						getUserInfoResponse base = new getUserInfoResponse(
							UserInfoActivity.this);
						base.parse(data);
						if (base.result == 1) {
							initEdit(base);
						} else {
							MyToast.showToast(UserInfoActivity.this, base.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						finishWaitDialog();
					}

					@Override
					public void onFailure(int error, Throwable message) {
						finishWaitDialog();
					}

				});
		client.post("http://112.74.95.154:8080/iAcron/updateUserInfo", rp.getParams());
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void showDate(){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		int y= 2000;
		int m = 1;
		int d = 1;
		try {
			date = df.parse(brith.getText().toString());
			y  = date.getYear() ;
			m = date.getMonth()+1;
			d = date.getDay();
		} catch (ParseException e) {
		}
		
		DatePickerDialog dateDialog = new DatePickerDialog(UserInfoActivity.this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0,
							int arg1, int arg2, int arg3) {
						arg2 += 1;
						brith.setText(arg1 + "-" + arg2
								+ "-" + arg3);
					}
				}, y,m+1,d);
		dateDialog.show();
	}
}
