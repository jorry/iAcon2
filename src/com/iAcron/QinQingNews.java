package com.iAcron;

import java.util.ArrayList;

import com.iAcon.database.manager.BindWardBeanManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcron.fragment.QinQingFragmentList;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 亲情列表
 * 
 * @author jorry_liu
 * 
 */
public class QinQingNews extends BaseFragmentActivity implements
		OnClickListener {

	LinearLayout tablayout;
	public ArrayList<BindWardBean> lists = new ArrayList<BindWardBean>();
	private int tabSelect;

	iAconApplication app;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.qinqing);
		app = (iAconApplication) getApplication();
		tablayout = (LinearLayout) findViewById(R.id.tablayout);

		findViewById(R.id.add_contact).setOnClickListener(this);

		queryBindWardList();
	}

	public void queryBindWardList() {
		
		BindWardBeanManager manager  =  new BindWardBeanManager(QinQingNews.this);
		
		
		lists = (ArrayList<BindWardBean>) manager.getList();
		if(lists.size()>=1){
			addTab();
		}
//		startWaitDialog(null, null, false);
//		String url = "http://112.74.95.154:8080/iAcron/queryBindWardList";
//		Common rp = new Common();
//		RequstClient client = new RequstClient(this, new HttpCallBack() {
//
//			@Override
//			public void onSuccess(String data) {
//				// TODO Auto-generated method stub
//				com.iAcon.response.queryBindWardList q = new com.iAcon.response.queryBindWardList(
//						QinQingNews.this);
//				q.parse(data);
//				if (q.result == 1) {
//					lists = app.bindWardLists = q.lists;
//					if (q.lists.size() >= 1) { // 如果有绑定过被被监护人
//						addTab();
//					} else {
//
//					}
//				}
//				finishWaitDialog();
//			}
//
//			@Override
//			public void onAppError(int error, Throwable message) {
//				// TODO Auto-generated method stub
//				finishWaitDialog();
//			}
//
//			@Override
//			public void onFailure(int error, Throwable message) {
//				// TODO Auto-generated method stub
//				finishWaitDialog();
//			}
//
//		});
//		client.post(url, rp.getParams());
	}


	public void addTab() {
		if (lists != null && lists.size() >= 1) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.titles, getFragment(lists.get(0)))
					.commitAllowingStateLoss();
		}
		if (tablayout != null) {
			tablayout.removeAllViews();
		}
		for (int i = 0; i < lists.size(); i++) {
			addTab(i, lists.get(i));
		}

		select(tabSelect);
	}

	public void select(int index) {
		for (int i = 0; i < tablayout.getChildCount(); i++) {
			if (index == i) {
				TextView tv = (TextView) tablayout.getChildAt(i).findViewById(
						R.id.left_tab);
				tv.setTextColor(0xff000000);
				tablayout.getChildAt(i).setBackgroundResource(R.drawable.u16);
				tabSelect = i;
			} else {
				TextView tv = (TextView) tablayout.getChildAt(i).findViewById(
						R.id.left_tab);
				tv.setTextColor(0xffffffff);
				tablayout.getChildAt(i).setBackgroundResource(R.drawable.u20);
			}
		}
	}

	public void addTab(int i, final BindWardBean bean) {

		LayoutInflater flater = getLayoutInflater();
		final View view = flater.inflate(R.layout.qinqing_left, null);
		LinearLayout qinqing_left_layout = (LinearLayout) view
				.findViewById(R.id.qinqing_left_layout);
		TextView tv = (TextView) view.findViewById(R.id.left_tab);
		tv.setText(bean.getWard_name());
		qinqing_left_layout.setTag(i);
		qinqing_left_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(v + "   " + v.getTag());
				select((Integer) v.getTag());
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.titles, getFragment(bean))
						.commitAllowingStateLoss();
			}
		});
		tablayout.addView(view);
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}


	public Fragment getFragment(BindWardBean bean) {
		return QinQingFragmentList.newInstance(bean, null);
	}

	@Override
	public void onClick(View arg0) {
		Intent in = new Intent();
		in.setClass(QinQingNews.this, QinQingAddActivity.class);
		startActivityForResult(in, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 101 && arg1 == 101) {
			queryBindWardList();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
}
