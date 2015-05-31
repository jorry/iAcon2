package com.iAcron.fragment;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.model.BindWardBean;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.ChatMessage;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.util.ThreadUtil;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseFregment extends Fragment {

	BaseFragmentActivity activity;

	private List<Pair<Integer, Handler.Callback>> callbacks = new ArrayList<Pair<Integer, Callback>>();

	protected void regMsg(int msg, Handler.Callback callback) {
		ThreadUtil.addMessageListener(msg, callback);
		callbacks.add(new Pair<Integer, Handler.Callback>(msg, callback));
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (BaseFragmentActivity) activity;
		super.onAttach(activity);
	}

	iAconApplication app;

	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		app = (iAconApplication) activity.getApplication();
		RelativeLayout chat = (RelativeLayout) view
				.findViewById(R.id.chatlayout);
		RelativeLayout tall = (RelativeLayout) view
				.findViewById(R.id.tellayout);
		if (chat != null) {
			chat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (app.bindWardLists == null
							|| app.bindWardLists.size() == 0) {
						return;
					}
					
					TextView  alert  = (TextView) view.findViewById(R.id.info_alert);
					if(alert!=null){
						alert.setText("");
					}
					Intent in = new Intent();
					
					Bundle b = new Bundle();
					b.putSerializable("obj",app.bindWardLists.get(app.bindWardSelect));
					in.putExtras(b);
					
					
					in.setClass(activity, ChatMessage.class);
					startActivity(in);
				}
			});
		}
		if (tall != null) {
			tall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (app.bindWardLists == null
							|| app.bindWardLists.size() == 0) {
						return;
					}
					BindWardBean bean = app.bindWardLists
							.get(app.bindWardSelect);
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + bean.getWatch_tel_no()));
					activity.startActivity(intent);
				}
			});
		}
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 返回到第一个首页
	 */
	public void backTop() {
		if (getFragmentManager() != null) {
			getFragmentManager().popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	/**
	 * 返回到指定页面
	 * 
	 * @param backFragment
	 *            想要返回 到的页面
	 */
	public void backStackFragment(Class<?> cls) {
		if (getFragmentManager() != null) {
			getFragmentManager().popBackStack(cls.getName(), 0);
		}
	}

	/**
	 * 返回上一个页面
	 * 
	 * 上一个页面必须执行了addBackStack()添加到栈里面
	 * 
	 */
	public void backStackFragment() {
		try {
			if (getFragmentManager() != null) {
				getFragmentManager().popBackStackImmediate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		super.onDestroy();
		for (Pair<Integer, Callback> p : callbacks) {
			ThreadUtil.removeMessageListener(p.first, p.second);
		}
		System.out.println("删除   广播");
		callbacks.clear();
	}
}
