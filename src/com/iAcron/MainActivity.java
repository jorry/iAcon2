package com.iAcron;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.iAcron.data.AppData;
import com.iAcron.data.Constants;
import com.iAcron.util.AlisaHandler;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MenuHorizontalScrollView;
import com.iAcron.view.MyToast;
import com.iAcron.view.SizeCallBackForMenu;
import com.iAcron.view.SlidingLinearLayout;
import com.umeng.analytics.MobclickAgent;
//import com.umeng.analytics.MobclickAgent;

public class MainActivity extends ActivityGroup {

	int currentMenuIndex = -1;
	boolean isFirstRun = true;
	int bgSelectColor;
	int bgUnSelectColor;
	iAconApplication app;
	private LinearLayout.LayoutParams containerChildLayoutParams;

	LayoutInflater inflater;
	public String TAG = this.getClass().getSimpleName();

	private MenuHorizontalScrollView scrollView;
	private LinearLayout menuList;
	private SlidingLinearLayout acbuwaList;
	private List<String> listItems;
	private LinearLayout controllView;
	private Button menuBtn;
	Button home;
	private View[] children;
	private TextView titleView; // topbar_title
	private List<View> menuViews = new ArrayList<View>();

	private String titleArray[] = { "亲情列表", "监护申请", "家庭管理", "电话薄", "安全配置", "充值" };

	private List<Pair<Integer, Handler.Callback>> callbacks = new ArrayList<Pair<Integer, Callback>>();
	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.openMenu:
				clickIndex(-1);
				break;
			}
			return false;
		}
	};

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	}

	LocalActivityManager m_ActivityManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		m_ActivityManager = getLocalActivityManager();
		// MobclickAgent.onResume(this);
		System.out.println("MSG" + getIntent().getStringExtra("msg"));
		
		
		inflater = LayoutInflater.from(this);
		ThreadUtil.init(3);

		setContentView(R.layout.menu_scroll_view);
		app = (iAconApplication) getApplication();

		regMsg(Constants.openMenu, msgCallback);

		this.scrollView = (MenuHorizontalScrollView) findViewById(R.id.mScrollView);
		this.menuList = (LinearLayout) findViewById(R.id.menuView);

		this.controllView = (LinearLayout) inflater.inflate(
				R.layout.acbuwa_page, null);
		home = (Button) this.controllView.findViewById(R.id.homeBtn);
		home.setVisibility(View.VISIBLE);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.setAction(Constants.mapReceiver);
				sendBroadcast(in);
				clickIndex(-1);
			}
		});

		titleView = (TextView) this.controllView
				.findViewById(R.id.topbar_title);

		this.menuBtn = (Button) this.controllView.findViewById(R.id.menuBtn);
		this.acbuwaList = (SlidingLinearLayout) this.controllView
				.findViewById(R.id.acbuwa_list);
		this.menuBtn.setOnClickListener(onClickListener);

		View leftView = new View(this);
		leftView.setBackgroundColor(Color.TRANSPARENT);
		children = new View[] { leftView, controllView };
		this.scrollView.initViews(children, new SizeCallBackForMenu(
				this.menuBtn), this.menuList);
		this.scrollView.setMenuBtn(this.menuBtn);

		initMenu();
		if (!TextUtils.isEmpty(AppData.user_id.get())) {
			new AlisaHandler(this, AppData.user_id.get());
		}


		if(getIntent().getExtras()!=null){
			clickIndex(-2);
		}else{
			clickIndex(-1);
		}
	}


	private void initMenu() {

		containerChildLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bgUnSelectColor = Color.TRANSPARENT;

		View qinqingliebiao = findViewById(R.id.qinqinglist);
		View qinqingjianhu = findViewById(R.id.qinqingjianhu);
		View qingqingjiating = findViewById(R.id.qingqingjiating);
		View guanhuaidianhuabo = findViewById(R.id.guanhuaidianhuabo);
		View guanhuaianquan = findViewById(R.id.guanhuaianquan);
		View guanhuaichongzhi = findViewById(R.id.guanhuaichongzhi);

		qinqingliebiao.setOnClickListener(menuClickListener);
		qinqingjianhu.setOnClickListener(menuClickListener);
		qingqingjiating.setOnClickListener(menuClickListener);
		guanhuaidianhuabo.setOnClickListener(menuClickListener);
		guanhuaianquan.setOnClickListener(menuClickListener);
		guanhuaichongzhi.setOnClickListener(menuClickListener);

		menuViews.add(qinqingliebiao);
		menuViews.add(qinqingjianhu);
		menuViews.add(qingqingjiating);
		menuViews.add(guanhuaidianhuabo);
		menuViews.add(guanhuaianquan);
		menuViews.add(guanhuaichongzhi);
	}

	private OnClickListener menuClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			for (int i = 0; i < menuViews.size(); i++) {

				if (v == menuViews.get(i)) {
					currentMenuIndex = i;
					menuViews.get(i).setBackgroundColor(bgSelectColor);
					titleView.setText(titleArray[i]);
					clickIndex(currentMenuIndex);
				} else {
					menuViews.get(i).setBackgroundColor(bgUnSelectColor);
				}
			}

		}

	};

	BaseFragmentActivity activity;

	@SuppressWarnings("deprecation")
	public void switchActivity(Intent intent, String actvityName) {
		m_ActivityManager.startActivity(actvityName, intent);
		activity = (BaseFragmentActivity) m_ActivityManager
				.getActivity(actvityName);

		acbuwaList.addView(
				getLocalActivityManager().startActivity(actvityName, intent)
						.getDecorView(), containerChildLayoutParams);
	}

	private long firstTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		try {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				boolean dispatchKeyEvent = activity.dispatchKeyEvent(event);
				LogUtil.i("dispatchKey", "dispatchKeyEvent = boolean "
						+ dispatchKeyEvent);
				if (!dispatchKeyEvent) {
					long secondTime = System.currentTimeMillis();
					if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
						firstTime = secondTime;// 更新firstTime
						Toast.makeText(MainActivity.this, "再按一次退出程序",
								Toast.LENGTH_SHORT).show();
					} else {
						finish();
					}
				}
				return false;
			}
			return super.dispatchKeyEvent(event);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	// 跳转页面的函数
	private void clickIndex(int index) {
		acbuwaList.removeAllViews();
		home.setVisibility(View.VISIBLE);

		if (null == m_ActivityManager) {
			m_ActivityManager = getLocalActivityManager();
		}
		ThreadUtil.sendMessage(Constants.contactBack);
		switch (index) {
		case -2:
			switchActivity(new Intent(this, ChatMessage.class),
					ChatMessage.class.getSimpleName());
			break;
		case -1:
			isFirstRun = true;
			titleView.setText("iAcron");
			home.setVisibility(View.GONE);
			// 首页
			switchActivity(new Intent(this, HomeActivity.class),
					HomeActivity.class.getSimpleName());
			break;
		case 0:

			// 亲情列表
			switchActivity(new Intent(this, QinQingNews.class),
					QinQingNews.class.getSimpleName());
			break;
		case 1:
			Intent in = new Intent(this, JianHuActivity.class);
			in.putExtra("msg", getIntent().getStringExtra("msg"));
			switchActivity(in, JianHuActivity.class.getSimpleName());
			break;
		case 2: // 家庭管理
			switchActivity(new Intent(this, JiaTingMangerActivity.class),
					JiaTingMangerActivity.class.getSimpleName());
			break;
		case 3:
			if (app.bindWardLists.size() <= 0) {
				MyToast.showToast(this, "被监护人信息还没有被处理，请联系系统管理员");
				return;
			}

			if (app.bindWardLists.size() >= 1) {
				titleView.setText(app.bindWardLists.get(app.bindWardSelect)
						.getWard_name() + "电话簿");
			} else {
				titleView.setText("电话簿");
			}

			switchActivity(new Intent(this, ContactActivity.class),
					ContactActivity.class.getSimpleName());

			break;
		case 4:
			if (app.bindWardLists.size() == 0) {
				return;
			}
			ThreadUtil.sendMessage(SafetyActivity.CLEAR_ALL);
			switchActivity(new Intent(this, SafetyActivity.class),
					SafetyActivity.class.getSimpleName());
			break;
		}
		if (isFirstRun) {
			isFirstRun = false;
		} else {
			scrollView.clickMenuBtn();
		}
	}

	protected void regMsg(int msg, Handler.Callback callback) {
		ThreadUtil.addMessageListener(msg, callback);
		callbacks.add(new Pair<Integer, Handler.Callback>(msg, callback));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (Pair<Integer, Callback> p : callbacks) {
			ThreadUtil.removeMessageListener(p.first, p.second);
		}
		app.bindWardLists.clear();
		callbacks.clear();

		LogUtil.i("HTTP", MainActivity.class.getName() + "  onDestory()");
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			scrollView.clickMenuBtn();
		}
	};

	public MenuHorizontalScrollView getScrollView() {
		return scrollView;
	}

	public void setScrollView(MenuHorizontalScrollView scrollView) {
		this.scrollView = scrollView;
	}

}
