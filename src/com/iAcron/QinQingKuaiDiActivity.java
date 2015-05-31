package com.iAcron;

import java.util.zip.Inflater;

import com.iAcron.R;
import com.iAcron.data.Constants;
import com.iAcron.data.DataBase;
import com.iAcron.fragment.KuaiDiVedioFragment;
import com.iAcron.fragment.QinQingKuaiDiListView;
import com.iAcron.fragment.RegisterGetCodeFragment;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.ThreadUtil;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("NewApi")
public class QinQingKuaiDiActivity extends BaseFragmentActivity {

	ListView flipper;

	LinearLayout qinqinglayout1;
	LayoutInflater inflater;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qinqing_kuaidi);
		LayoutBuilder lb = new LayoutBuilder(this);
		lb.replaceRegister(QinQingKuaiDiListView.class, null);
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
				ThreadUtil.sendMessage(Constants.openMenu);
				finish();
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
