package com.iAcron;

import com.iAcron.data.DataBase;
import com.iAcron.fragment.ResetByPhoneFragment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.LayoutBuilder;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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
public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getpassword_manager);
		findViewById(R.id.back).setOnClickListener(this);
		LayoutBuilder lb  = new LayoutBuilder(this);
		lb.replaceRegister(ResetByPhoneFragment.class, null);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
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
