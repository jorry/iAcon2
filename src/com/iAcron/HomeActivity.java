package com.iAcron;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.iAcon.database.manager.BindWardBeanManager;
import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.bean.AllContactBean;
import com.iAcron.adapter.MenuAdapter;
import com.iAcron.adapter.MenuContent;
import com.iAcron.adapter.MenuGone;
import com.iAcron.adapter.MenuTitle;
import com.iAcron.data.AppData;
import com.iAcron.data.Constants;
import com.iAcron.fragment.CommonFragment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MMAlert;
import com.iAcron.view.MyRadioGourp;
import com.iAcron.view.MyToast;
import com.iAcron.view.MyViewPaper;
import com.iAcron.view.TabHorizontalScrollView;
import com.iAcron.view.WardButton;
import com.loopj.android.http.RequestParams;

/**
 * 主页：控制器，用于fragment切换
 * 
 * 侧边栏菜单用listView显示
 * 
 * @author jorry_liuo
 * 
 */
public class HomeActivity extends BaseFragmentActivity implements
		OnClickListener {

	private MyViewPaper vp;
	private ImageView iv_indicator, iv_nav_right, iv_nav_left;
	private RadioGroup rg;
	private int currentNavItemWidth, currentIndicatorLeft = 0;
	private TabHorizontalScrollView tsv;
	private int cardinality; //
	private RelativeLayout rl;

	TabFragmentPagerAdapter pagerAdapter;

	private RelativeLayout main_center_info;

	LinearLayout main_canter_leftll;
	private ListView main_canter_listview;
	ArrayList<AllContactBean> contactLists;
	itemAdapter items;

	LayoutInflater inflater;

	RelativeLayout nojianhu;
	Button addBt;

	private String TAG = HomeActivity.class.getSimpleName();

	iAconApplication app;

	BindWardBeanManager manager ;
	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			Map<String, Object> map = null;
			int ward_id;
			if (msg.obj != null) {
				if(msg.obj instanceof String){
					map = (Map<String, Object>) JSON.parse((String) msg.obj);
				}
			}
			switch (msg.what) {
			case Constants.addTab:
				queryBindWardList();
				break;
			case Constants.push_BINDAPPLY:
				break;
			case Constants.push_APPLYRESULT:
				// 如果申请被批准，则在首页底部添加该被监护人的按钮
				int result = (Integer) map.get("apply_result");
				if (result == 1) {
					intBuildWard();
					queryBindWardList();
					requestAllContect();

				}
				break;
			case Constants.push_UNBIND: // 解绑 删除对应的被监护人
				ward_id = (Integer) map.get("ward_id");
				for (int i = 0; i < app.bindWardLists.size(); i++) {
					BindWardBean bean = (BindWardBean) rg.getChildAt(i)
							.getTag();
					if (bean.getWard_id() == ward_id) {
						app.bindWardLists.remove(i);
						queryBindWardList();
					}
				}

				break;
			case Constants.push_RELEASESOS:
				ward_id = (Integer) map.get("ward_id");
				for (int i = 0; i < rg.getChildCount(); i++) {
					BindWardBean bean = (BindWardBean) rg.getChildAt(i)
							.getTag();
					if (bean.getWard_id() == ward_id) {
						MyRadioGourp radio = (MyRadioGourp) rg.getChildAt(i);
						radio.setBack(0);
						radio.invalidate();
					}
				}
				break;
			case Constants.push_SOS:
				int sos_type = 0;// SOS类型。1：生理数据触发，2：被监护人手动触发
				String sos_name = "";
				ward_id = (Integer) map.get("ward_id");
				sos_type = (Integer) map.get("sos_type"); // SOS类型。1：生理数据触发，2：被监护人手动触发
				sos_name = (String) map.get("sos_name");
				int sos_level = (Integer) map.get("sos_level");
	/**
	 * 更新数据库			
	 */
				
				BindWardBean dbBean = manager.getUserInfo(ward_id);
				dbBean.setReleaseSOS(sos_level);
				manager.update(dbBean);
				ThreadUtil.sendMessage(Constants.msg_SOS_releace);
				
				for (int i = 0; i < rg.getChildCount(); i++) {
					BindWardBean bean = (BindWardBean) rg.getChildAt(i)
							.getTag();
					if (bean.getWard_id() == ward_id) {
						MyRadioGourp radio = (MyRadioGourp) rg.getChildAt(i);
						radio.setBack(sos_level);
						radio.invalidate();
					}
				}
				break;
			case Constants.msg_SOS_releace:
				BindWardBean beanO = (BindWardBean) msg.obj;

				for (int i = 0; i < rg.getChildCount(); i++) {
					BindWardBean bean = (BindWardBean) rg.getChildAt(i)
							.getTag();
					if (bean.getWard_id() == beanO.getWard_id()) {
						MyRadioGourp radio = (MyRadioGourp) rg.getChildAt(i);
						radio.setBack(0);
						radio.invalidate();
					}

				}
				break;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_center);
		manager =  new BindWardBeanManager(HomeActivity.this);
		findViewById(R.id.checkuserevent).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						xStartActivity(UserInfoActivity.class, null);
					}
				});
		app = (iAconApplication) getApplication();
		addBt = (Button) findViewById(R.id.addBt);
		addBt.setOnClickListener(this);
		nojianhu = (RelativeLayout) findViewById(R.id.nojianhu);
		items = new itemAdapter();

		findView();
		setCardinality();

		setRadioGorupSelecctListener();
		inflater = getLayoutInflater();

		setLeftTitle(AppData.userName.get());

		regMsg(Constants.addTab, msgCallback);
		regMsg(Constants.push_SOS, msgCallback);
		regMsg(Constants.msg_SOS_releace, msgCallback);
		regMsg(Constants.push_APPLYRESULT, msgCallback);
		regMsg(Constants.push_BINDAPPLY, msgCallback);
		regMsg(Constants.push_UNBIND, msgCallback);
		regMsg(Constants.push_RELEASESOS, msgCallback);
		intBuildWard();
		queryBindWardList();
		requestAllContect();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	private void requestAllContect() {
		// TODO Auto-generated method stub
		startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/fetchFamilyMemberList";
		Common rp = new Common();
		client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				com.iAcon.response.AllContectresonse q = new com.iAcon.response.AllContectresonse(
						HomeActivity.this);
				q.parse(data);
				if (contactLists == null) {
					contactLists = new ArrayList<AllContactBean>();
				}
				if (q.result == 1) {
					contactLists.addAll(q.lists);
				}
				addAdapterData();
				finishWaitDialog();
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}

	RequstClient client;

	/**
	 * 获取被监护人列表
	 * 
	 * [{“ward_id”:1,”ward_name”:”张三”,”status”:1},{“ward_id”:2,”ward_name”:”李四”,
	 * ”status”:0}] 这里张三已经被绑定成功，李四尚未绑定成功（未被批准）。
	 * 
	 * 
	 */
	public void queryBindWardList() {
		startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/queryBindWardList";
		Common rp = new Common();
		client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				com.iAcon.response.queryBindWardList q = new com.iAcon.response.queryBindWardList(
						HomeActivity.this);
				q.parse(data);
				if (q.result == 1) {
					if (q.lists.size() >= 1) { // 如果有绑定过被被监护人
						if (app.bindWardLists != null) {
							app.bindWardLists.clear();
						}
						
						LogUtil.i("db", "数据库 监护人 大小 = "+manager.getList().size());
						for (BindWardBean b:manager.getList()) {
							if(!"1".equals(b.getStatus())){
								manager.getList().remove(b);
							}
						}
						for (BindWardBean beans :q.lists) {
							BindWardBean b = manager.getUserInfo(beans.getWard_id());
							LogUtil.i("db", "insert 新监护人"+b);
							if(b==null&&beans.getStatus().equals("1")){
								manager.insertDeposit(beans);
							}
						}
						
						pagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
						vp.setAdapter(pagerAdapter);
						
						app.bindWardLists = (ArrayList<BindWardBean>) manager.getList();
						nojianhu.setVisibility(View.GONE);
						pagerAdapter.notifyDataSetChanged();
						addRadioView();
					} else {

					}
				}
				finishWaitDialog();
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}

	/**
	 * 添加左边列表项
	 * 
	 * @param o
	 */
	public void addAdapterData() {
		if (contactLists == null) {
			contactLists = new ArrayList<AllContactBean>();
		}
		items.notifyDataSetChanged();
	}

	private void setRadioGorupSelecctListener() {

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (rg.getChildAt(checkedId) == null) {
					return;
				}

				app.bindWardSelect = checkedId;

				Log.i(TAG, rg.getChildCount() + "    " + checkedId
						+ "checkedId:" + rg.getChildAt(checkedId));

				rg.getChildAt(checkedId).setClickable(true);
				rg.check(checkedId);

				// indicator
				TranslateAnimation animation = new TranslateAnimation(
						currentIndicatorLeft, ((RadioButton) rg
								.getChildAt(checkedId)).getLeft(), 0f, 0f);
				animation.setInterpolator(new LinearInterpolator());
				animation.setDuration(300);
				animation.setFillAfter(true);

				iv_indicator.startAnimation(animation);//

				currentIndicatorLeft = rg.getChildAt(checkedId).getLeft();//

				// ViewPager
				vp.setCurrentItem(checkedId,false);// ViewPager
				//
				// // ScrollVie
				int x = (checkedId > 0 ? ((RadioButton) rg
						.getChildAt(checkedId)).getLeft() : 0)
						- ((RadioButton) rg.getChildAt(1)).getLeft();
				tsv.smoothScrollTo(x, 0);
			}
		});
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				Log.i(TAG, "position: " + position);

				if (rg != null) {
					rg.getChildAt(position).performClick();
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 */
	private void setCardinality() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		cardinality = 4;
		currentNavItemWidth = dm.widthPixels / cardinality;

		LayoutParams param = iv_indicator.getLayoutParams();
		param.width = currentNavItemWidth;//
		iv_indicator.setLayoutParams(param);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -1);
		lp.width = dm.widthPixels / 6;

		main_center_info.setLayoutParams(lp);
		// findViewById(R.id.guanhuai).setLayoutParams(params)arams(new
		// android.widget.LinearLayout.LayoutParams(lp.width,LayoutParams.WRAP_CONTENT));

		main_canter_leftll.setLayoutParams(lp);
	}

	/**
	 * 用户名
	 * 
	 * @param title
	 */
	public void setLeftTitle(String title) {

	}

	/**
	 * 
	 */
	private void addRadioView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		rg.removeAllViews();
		ArrayList<BindWardBean> beans = (ArrayList<BindWardBean>) manager.getList();
		
		for (int i=0;i<beans.size();i++) {
			final MyRadioGourp rb = (MyRadioGourp) inflater.inflate(
					R.layout.nav_rg_item, null);
			rb.setTag(beans.get(i));
			rb.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					showWardAlert((BindWardBean) arg0.getTag());
					return true;
				}
			});
			int sos =beans.get(i).getReleaseSOS();
			LogUtil.i("addRadioView", "user = "+sos);
			rb.setBack(sos);

			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
					currentNavItemWidth, LayoutParams.MATCH_PARENT);
			ll.gravity = Gravity.CENTER;
			rb.setLayoutParams(ll);
			rb.setId(i);
			rb.setText(beans.get(i).getWard_name());

			rg.addView(rb);
		}
		if (rg.getChildAt(0) != null)
			rg.getChildAt(0).performClick();
	}

	Dialog wardDialog;

	/**
	 * 长按
	 * 
	 * @param bean
	 */
	public void showWardAlert(final BindWardBean bean) {
		View view = getLayoutInflater().inflate(
				R.layout.alert_dialog_ward_layout, null);
		Button sos = (Button) view.findViewById(R.id.alert_ward_sos_history);
		Button jiechu = (Button) view.findViewById(R.id.alert_ward_sos_jiechu);
		
		 BindWardBean b = manager.getUserInfo(bean.getWard_id());
		
		
		
		if (b.getGuard_identity() == 1
				&& (b.getReleaseSOS() != null && (b.getReleaseSOS() == 1||b.getReleaseSOS()==2))) {
			jiechu.setVisibility(View.VISIBLE);
		}

		jiechu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestReleaseSOS(bean);
			}
		});
		view.findViewById(R.id.alert_ward_info).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						wardDialog.cancel();
					}
				});

		sos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle b = new Bundle();
				b.putSerializable("obj", bean);
				xStartActivity(JingBaoHistory.class, b);
				wardDialog.cancel();
			}
		});
		Button kuaidi = (Button) view.findViewById(R.id.alert_ward_qinqing);
		kuaidi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				wardDialog.cancel();
				xStartActivity(QinQingKuaiDiActivity.class, null);
			}
		});
		WardButton info = (WardButton) view.findViewById(R.id.alert_ward_info);
		fetchManager manager = new fetchManager(HomeActivity.this);
		info.setCount(manager.getMsgType1());

		view.findViewById(R.id.alert_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						wardDialog.cancel();
					}
				});
		wardDialog = MMAlert.showAlert(this, view);
		wardDialog.show();
	}

	/**
	 * 接触警报
	 */
	private void requestReleaseSOS(final BindWardBean bean) {
		// TODO Auto-generated method stub
		startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/releaseSOS";
		Common rp = new Common();
		rp.setKV("ward_id", "" + bean.getWard_id());

		client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				finishWaitDialog();
				BaseResponse br = new BaseResponse(con);
				br.parse(data);
				if (br.result == 1) {
					bean.setReleaseSOS(0);
					manager.update(bean);
					ThreadUtil.sendMessage(Constants.msg_SOS_releace, bean);
				} else {
					MyToast.showToast(HomeActivity.this, br.result_msg);
				}

			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}

	public void xinFeng() {
		View view = getLayoutInflater().inflate(
				R.layout.alert_dialog_xinfeng_layout, null);
		Button sos = (Button) view.findViewById(R.id.exit);
		sos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppData.cleanUserPre();
				xStartActivityForResult(LandingActivity.class, null, 100);

			}
		});
		view.findViewById(R.id.alert_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						wardDialog.cancel();
					}
				});
		wardDialog = MMAlert.showAlert(this, view);
		wardDialog.show();
	}

	protected void xStartActivity(Class<?> cls, Bundle bundle) {

		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.setClass(this, cls);
		startActivity(intent);
	}

	protected void xStartActivityForResult(Class<?> cls, Bundle bundle,
			int requestCode) {

		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.setClass(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}

	private void intBuildWard() {

		addRadioView();

		// ViewPaper
		

		// TabHorizontalScrollView
		tsv.setParams(rl, iv_nav_left, iv_nav_right, this);
		iv_nav_left.setVisibility(View.GONE); //
		if (app.bindWardLists.size() <= 4) {
			iv_nav_right.setVisibility(View.GONE); //
		}
	}

	class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			System.out.println("getitemt");
			Fragment fg = new CommonFragment();
			Bundle args = new Bundle();
			args.putSerializable("ward", app.bindWardLists.get(arg0));
			fg.setArguments(args);

			return fg;
		}

		@Override
		public int getCount() {
			return app.bindWardLists == null || app.bindWardLists.size() == 0 ? 0
					: app.bindWardLists.size();
		}

	}

	private void findView() {

		rl = (RelativeLayout) findViewById(R.id.rl_nav);
		tsv = (TabHorizontalScrollView) findViewById(R.id.sv_nav);
		rg = (RadioGroup) findViewById(R.id.rg_nav);

		iv_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);

		vp = (MyViewPaper) findViewById(R.id.vp);
		main_center_info = (RelativeLayout) findViewById(R.id.main_center_info);
		main_center_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				xinFeng();
			}
		});
		main_canter_leftll = (LinearLayout) findViewById(R.id.main_center_left);
		main_canter_listview = (ListView) findViewById(R.id.main_canter_listview);
		main_canter_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent in = new Intent(HomeActivity.this, ChatMessage.class);
				Bundle b = new Bundle();
				BindWardBean bean = new BindWardBean();
				bean.setWard_id(contactLists.get(arg2).getGuard_id());
				bean.setWard_name(contactLists.get(arg2).getNick_name());
				bean.setTo_type(1);
				b.putSerializable("obj", bean);
				in.putExtras(b);
				startActivity(in);
			}
		});
		main_canter_listview.setAdapter(items);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 */
	Handler scrollHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int leftCurrentItem = ((RadioButton) rg.getChildAt(vp
					.getCurrentItem())).getLeft();
			int leftOneRadio = ((RadioButton) rg.getChildAt(1)).getLeft();
			int x = (vp.getCurrentItem() > 0 ? leftCurrentItem : 0)
					- leftOneRadio;
			tsv.smoothScrollTo(x, 0);
			super.handleMessage(msg);
		};
	};

	public class itemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contactLists == null ? 0 : contactLists.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.main_leftitem, null);
			TextView t = (TextView) view.findViewById(R.id.main_leftitem_title);
			t.setText(contactLists.get(arg0).getNick_name());
			return view;
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.addBt:

			startActivityForResult(new Intent(HomeActivity.this,
					QinQingAddActivity.class), 101);
			break;

		default:
			break;
		}
	}
}
