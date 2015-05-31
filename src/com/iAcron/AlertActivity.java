package com.iAcron;

import com.iAcron.data.Constants;
import com.iAcron.fragment.AlertDetails;
import com.iAcron.fragment.AlertListActicty;
import com.iAcron.fragment.ContactListFragment;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.ThreadUtil;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

/**
 * 某个联系人的电话薄
 * 
 * @author jorry_liu
 * 
 */
public class AlertActivity extends BaseFragmentActivity {

	public iAconApplication app;
	
	int showType;
	
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		setContentView(R.layout.contact);
		app = (iAconApplication) getApplication();
		showType = getIntent().getIntExtra("showType",0);
		if(showType==0){
			LayoutBuilder lb = new LayoutBuilder(this);
			lb.replaceRegister(AlertListActicty.class, null);
		}else{
			LayoutBuilder lb = new LayoutBuilder(this);
			lb.replaceRegister(AlertDetails.class, getIntent().getExtras() );
		}
		

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			int fc = getSupportFragmentManager().getBackStackEntryCount();
			if (fc >1) {
				onStart();
				getSupportFragmentManager().popBackStackImmediate();
				return true;
			} else {
				finish();
				return false;
			}

		}
		return super.dispatchKeyEvent(event);
	}

}
