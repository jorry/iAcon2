package com.iAcron;

import java.util.ArrayList;
import java.util.List;

import com.iAcron.data.Constants;
import com.iAcron.fragment.ContactListFragment;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.ThreadUtil;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 某个联系人的电话薄
 * 
 * @author jorry_liu
 * 
 */
public class ContactActivity extends BaseFragmentActivity {

	public iAconApplication app;
	private List<Pair<Integer, Handler.Callback>> callbacks = new ArrayList<Pair<Integer, Callback>>();
	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.contactBack:
				int fc = getSupportFragmentManager().getBackStackEntryCount();
				if (fc > 1) {
					onStart();
					getSupportFragmentManager().popBackStackImmediate();
				}
			case Constants.openMenu:
				break;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.contact);
		app = (iAconApplication) getApplication();
		regMsg(Constants.contactBack, msgCallback);
		LayoutBuilder lb = new LayoutBuilder(this);
		lb.replaceRegister(ContactListFragment.class, null);

	}

	protected void regMsg(int msg, Handler.Callback callback) {
		ThreadUtil.addMessageListener(msg, callback);
		callbacks.add(new Pair<Integer, Handler.Callback>(msg, callback));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(callbacks!=null){
			callbacks.clear();
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
			if (fc > 1) {
				onStart();
				getSupportFragmentManager().popBackStackImmediate();
				return true;
			} else {
				return false;
			}

		}
		return super.dispatchKeyEvent(event);
	}

}
