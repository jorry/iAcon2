package com.iAcron;

import com.iAcon.response.BaseResponse;
import com.iAcron.fragment.RegisterGetCodeFragment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LayoutBuilder;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

/**
 * 验证码
 * 
 * @author jorry
 * 
 */
public class RegisterPhoneCode extends BaseFragmentActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_manager);
		findViewById(R.id.menuBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onStart();
				if(getSupportFragmentManager().getBackStackEntryCount()<=1){
					finish();
				}
			}
		});
		LayoutBuilder lb = new LayoutBuilder(this);
		lb.replaceRegister(RegisterGetCodeFragment.class,null);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(getSupportFragmentManager().getBackStackEntryCount()<=1){
				finish();
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
