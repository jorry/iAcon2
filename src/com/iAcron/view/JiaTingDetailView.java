package com.iAcron.view;

import com.iAcron.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

public class JiaTingDetailView extends LinearLayout {

	public JiaTingDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		onFinishInflate();
	}

	EditText name;
	EditText sex;
	EditText brith;
	EditText tel;
	EditText phone;
	EditText qq;
	EditText email;
	EditText address;
	
	protected void onFinishInflate() {
		address = (EditText)findViewById(R.id.jiating_address);
		email = (EditText)findViewById(R.id.jiating_email);
		qq = (EditText)findViewById(R.id.jiating_qq);
		phone = (EditText)findViewById(R.id.jiating_phone);
		tel = (EditText)findViewById(R.id.jiating_tel);
		brith = (EditText)findViewById(R.id.jiating_brith);
		sex = (EditText)findViewById(R.id.jiating_sex);
		name = (EditText)findViewById(R.id.jiating_name);
		
		super.onFinishInflate();
	}
	public void setEditText(Object o){
		address.setText("");
		email.setText("");
		qq.setText("");
		phone.setText("");
		tel.setText("");
		sex.setText("");
		name.setText("");
			
	}
	
	public void onDestory(){
		address = null;
		email = null;
		qq = null;
		phone = null;
		tel = null;
		brith = null;
		sex = null;
		name = null;
	}

}
