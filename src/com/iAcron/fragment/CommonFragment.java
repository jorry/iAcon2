package com.iAcron.fragment;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ZoomControls;

import cn.jpush.android.data.u;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.response.fetchWardFenceResponse;
import com.iAcon.response.fetchWardHistory;
import com.iAcon.response.fetchWardPhyRangeResponse;
import com.iAcon.response.getWardPhyResponse;
import com.iAcon.response.getWardRemindListResponse;
import com.iAcon.response.kuaiDiList;
import com.iAcon.response.bean.getWardReindBean;
import com.iAcon.response.bean.ward_fence.points;
import com.iAcron.AlertActivity;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.BrokenLineView;
import com.iAcron.ChatMessage;
import com.iAcron.JianHuActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.util.Util;
import com.iAcron.view.CommonProgressLayout;
import com.iAcron.view.MyToast;
import com.iAcron.view.ProgressLayout;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class CommonFragment extends BaseFregment implements OnClickListener {

	// 定位相关
	LocationClient mLocClient;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	BindWardBean bean;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	boolean isFirstLoc = true;// 是否首次定位

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.u155);
	TextView bufaTv, sleepTv;

	TextView xintiaoTv, jiuhuTv, xueyangTv;

	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.updateMap:
				mBaiduMap.clear();
			case Constants.push_RELEASESOS:
			case Constants.push_SOS:
			case Constants.msg_SOS_releace:
				queryBindWardList();
				break;
			case Constants.msg_nofity_update:
//				fetchManager f = new fetchManager(activity);
//				List<fetchmsg> lists = f.getMsgType1FromWardId(
//						bean.getWard_id(), bean.getTo_type());
//				if (lists.size() > 0) {
//					alert.setText("" + lists.size());
//				}
				break;
			}
			return false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.i("MAP", "onCreate");

		super.onCreate(savedInstanceState);

	}

	public void map() {
		mMapView.invalidate();
	}

	public static int res[] = { R.color.le_2, R.color.le_1, R.color.le_2,
			R.color.le_3 };
	/**
	 * 血氧
	 */
	public static int res1[] = { R.color.le_3, R.color.le_2, R.color.le_0,
			R.color.le_1 }; // 血氧 0青，1绿，2橙；3红
	/**
	 * 跌倒
	 */
	public static int res2[] = { R.color.le_1, R.color.le_0, R.color.le_2,
			R.color.le_3 };
	TextView alert;

	getWardPhyResponse wardPhy;

	CommonProgressLayout bufaLaout;
	CommonProgressLayout timeLayout;
	CommonProgressLayout xintiaoLayout;
	CommonProgressLayout xueyangLayout;
	CommonProgressLayout jiuhuOLaout;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		bean = (BindWardBean) getArguments().getSerializable("ward");
		System.out.println("bean = " + bean.getWard_name() + "   "
				+ bean.getWard_id());

		alert = (TextView) view.findViewById(R.id.info_alert);
//		fetchManager f = new fetchManager(activity);
//		List<fetchmsg> lists = f.getMsgType1FromWardId(bean.getWard_id(),
//				bean.getTo_type());
//		if (lists.size() > 0) {
//			alert.setText("" + lists.size());
//		}

		bufaTv = (TextView) view.findViewById(R.id.common_bufa_tv);
		sleepTv = (TextView) view.findViewById(R.id.common_time_tv);

		jiuhuTv = (TextView) view.findViewById(R.id.common_jiuhu_tv);
		xintiaoTv = (TextView) view.findViewById(R.id.common_xintiao_tv);
		xueyangTv = (TextView) view.findViewById(R.id.common_xueyang_tv);
		// 地图初始化
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mMapView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				System.out.println("onTouch");
				return true;
			}
		});
		mBaiduMap = mMapView.getMap();
		UiSettings mapSetting = mBaiduMap.getUiSettings();
		mapSetting.setZoomGesturesEnabled(true);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(activity);

		bufaLaout = (CommonProgressLayout) view.findViewById(R.id.bufaOnClick);
		bufaLaout.setOnClickListener(this);

		timeLayout = (CommonProgressLayout) view.findViewById(R.id.timeOnClick);
		timeLayout.setOnClickListener(this);

		xintiaoLayout = (CommonProgressLayout) view
				.findViewById(R.id.xintiaoOnClick);
		xintiaoLayout.setOnClickListener(this);

		xueyangLayout = (CommonProgressLayout) view
				.findViewById(R.id.xueyangOnClick);
		xueyangLayout.setOnClickListener(this);

		jiuhuOLaout = (CommonProgressLayout) view
				.findViewById(R.id.jiuhuOnClick);
		jiuhuOLaout.setOnClickListener(this);

		queryBindWardList();

		requestAlert();

		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}

		super.onViewCreated(view, savedInstanceState);
	}

	public View.OnClickListener alertOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent in = new Intent();
			in.setClass(activity, AlertActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("obj", (getWardReindBean) v.getTag());
			in.putExtras(b);
			// switch (v.getId()) {
			// case R.id.alert_1:
			// in.putExtra("showType", 1);
			// break;
			// case R.id.alert_2:
			// in.putExtra("showType", 1);
			// break;
			// case R.id.alert_more:
			// in.putExtra("showType", 0);
			// break;
			// }
			startActivity(in);
		}
	};

	/**
	 * 设置电子围栏
	 * 
	 * @param res3
	 */

	protected void setPoint(getWardPhyResponse res) {
		if (res.p == null) {
			MyToast.showToast(activity, "服务器内部错误");
			return;
		}

		if (res.c.getFence_type() == 1) { // 圆形
			addCircleToMap(res.c.getCenterPoint().latS,
					res.c.getCenterPoint().longS, res.c.getRadius());
		} else { // 多边形
			addDuobianMap(res);
		}
	}

	private void addDuobianMap(getWardPhyResponse res) {

		List<LatLng> pts = new ArrayList<LatLng>();

		ArrayList<points> lists = (ArrayList<points>) res.c.getPointsList();
		//
		for (int i = 0; i < lists.size(); i++) {
			points p = lists.get(i);
			LatLng pt = new LatLng(Double.parseDouble(p.latS),
					Double.parseDouble(p.longS));
			pts.add(pt);
		}
		addUserCenterToMap("" + pts.get(0).latitude, "" + pts.get(0).latitude);
		//
		OverlayOptions polygonOption = new PolygonOptions().points(pts)
				.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
		// 在地图上添加多边形Option，用于显示
		mBaiduMap.addOverlay(polygonOption);

		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLngZoom(pts.get(0), 10);

		mBaiduMap.animateMapStatus(u);

	}

	private void addCircleToMap(String latS, String longS, double radius) {
		try {
			// 添加圆
			LatLng llCircle = new LatLng(Double.parseDouble(latS),
					Double.parseDouble(longS));

			addUserCenterToMap(latS, longS);

			OverlayOptions ooCircle = new CircleOptions().fillColor(0X00FF0000)
					.center(llCircle).stroke(new Stroke(2, 0xffff0000))
					.radius((int) (radius * 1000)); // 半径
			mBaiduMap.addOverlay(ooCircle);

			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llCircle);

			mBaiduMap.animateMapStatus(u);

		} catch (Exception e) {

			MyToast.showToast(activity, "圆形围栏坐标错误");
			e.printStackTrace();
		}
	}

	public void addUserCenterToMap(String lat, String longs) {
		try {

			LatLng llA = new LatLng(Double.parseDouble(lat),
					Double.parseDouble(longs));

			BaiduMapOptions o = new BaiduMapOptions();
			o.zoomControlsEnabled(false);

			MapStatusUpdateFactory.zoomTo(5);

			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(llA));

			mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
					.zIndex(9);
			Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

			MapStatus mapStatus = new MapStatus.Builder().target(llA).zoom(17)
					.build();

			BaiduMapOptions options = new BaiduMapOptions()
					.zoomControlsEnabled(false).scaleControlEnabled(false)
					.mapStatus(mapStatus);

			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);

		} catch (Exception e) {
			MyToast.showToast(activity, "用户坐标错误");
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	 * user_id 必须 Int 用户名编号 ward_id 必须 int 被监护人编号 last_phy_time 必须 Long 上一次下拉的时间
	 * need_real_time 必须 Int 1:需要先从腕表拉取实时数据，0：返回已有数据即可。 need_ward_fence 可选 Int
	 * 1：需要被监护人的电子围栏数据；0：不需要 need_phy_range 可选 Int 1：需要被监护人的运动计划及生理范围；0：不需要
	 * token 必须 String 用于后续交互的唯一session token
	 */

	public void queryBindWardList() {
		String url = "http://112.74.95.154:8080/iAcron/getWardPhy";
		Common rp = new Common();
		rp.setKV("last_phy_time", "1");
		rp.setKV("ward_id", "" + bean.getWard_id());
		rp.setKV("need_real_time", "1");
		rp.setKV("need_ward_fence", "1");
		rp.setKV("need_phy_range", "1");

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						System.out.println("data" + data);
						activity.finishWaitDialog();
						getWardPhyResponse res = new getWardPhyResponse(
								activity);
						res.parse(data);
						if (res.result == 1) {
							wardPhy = res;
							setWard(res);
							setPoint(res);
						} else {
							MyToast.showToast(activity, res.result_msg);
						}

					}

					@Override
					public void onAppError(int error, Throwable message) {
						activity.finishWaitDialog();
					}

					@Override
					public void onFailure(int error, Throwable message) {
						activity.finishWaitDialog();
					}

				});
		client.post(url, rp.getParams());
	}

	/**
	 * 步伐 // 运动步数>=安全配置60% 安全配置30%<=运动步数<安全配置60% 运动步数<安全配置30%
	 * 
	 * @param res
	 */
	public void setBufa(float plan_time, String time) {
		LogUtil.i("shouye","状态：步伐 "+plan_time+"   "+time);
		
		if (Integer.parseInt(time) >= plan_time / 100 * 60) {
			bufaLaout.index = 1;
		} else if (plan_time / 100 * 30 <= Integer.parseInt(time)
				&& Integer.parseInt(time) < plan_time / 100 * 60) {
			bufaLaout.index = 2;
		} else if (Integer.parseInt(time) < plan_time / 100 * 30) {
			bufaLaout.index = 3;
		}

		bufaLaout.invalidate();

		bufaTv.setText(time+"步");
	}

	/**
	 * 睡眠时长>=安全配置60%
	 * 
	 * 安全配置30%<=睡眠时长<安全配置60%
	 * 
	 * 睡眠时长<安全配置30%
	 * 
	 * @param res
	 */
	public void setSleep(float plan_time, String time) {
		LogUtil.i("shouye","状态：睡眠 "+plan_time+"   "+time);
		if (Integer.parseInt(time) >= plan_time / 100 * 60) {
			timeLayout.index = 1;
		} else if (plan_time / 100 * 30 <= Integer.parseInt(time)
				&& Integer.parseInt(time) < plan_time / 100 * 60) {
			timeLayout.index = 2;
		} else if (Integer.parseInt(time) < plan_time / 100 * 30) {
			timeLayout.index = 3;
		}
		timeLayout.invalidate();
		sleepTv.setText(time+"h");
	}

	/**
	 * 心率 1 = 从服务器获取 安全配置心率下限<=心率<=安全配置上限 2 = 安全配置心率下限-5<=心率<安全配置心率下限 或
	 * 安全配置上限<心率<=安全配置上限+10 3 = 心率<安全配置心率下限-5 或 安全配置上限+10<心率
	 * 
	 * @param res
	 */
	public void setXinLv(getWardPhyResponse res) {
		
		LogUtil.i("shouye","状态：心率 "+res.p.getHealthy_state()+"   高" +res.r.getHeart_rate_high()+"  低"+res.r.getHeart_rate_low());
		
		
		int user = res.p.getHealthy_state();
		int plan_h = res.r.getHeart_rate_high();
		int plan_l = res.r.getHeart_rate_low();
		int proIndex = 0;
		if (plan_l <= user && user <= plan_h) {
			proIndex = 0;
		} else if ((plan_l - 5 <= user && user < plan_h)
				|| (plan_h < user && user <= plan_h + 10)) {
			proIndex = 1;
		} else if (user < plan_l + 5 || plan_h + 10 < user) {
			proIndex = 2;
		}
		xintiaoLayout.index = proIndex;
		xintiaoLayout.invalidate();
		xintiaoTv.setText(res.p.getHeart_rate() + "次");
	}

	/**
	 * 1 = 实际血氧含量>=安全配置血氧含量 2 = 安全配置血氧含量-3%<血氧含量<安全配置血氧含量 3 = 血氧含量<安全配置血氧含量-3%
	 * 
	 * @param res
	 */
	public void setXueYang(getWardPhyResponse res) {
		
		
		
		LogUtil.i("shouye","状态：血氧 "+res.p.getHealthy_state()+"   高" +res.r.getHeart_rate_high()+"  低"+res.r.getHeart_rate_low());
		
		int proIndex = 0;

		int user_ox = res.p.getBlood_oxygen();
		int plan_h = (int) res.r.getBlood_oxygen_high();
		int plan_l = (int) res.r.getBlood_oxygen_low();
		if (user_ox >= plan_h) {
			proIndex = 0;
		} else if ((plan_l - plan_l / 100 * 3 < user_ox) && user_ox < plan_l) {
			proIndex = 1;
		} else if (user_ox < plan_l - plan_l / 100 * 3) {
			proIndex = 2;
		}
		xueyangLayout.index = proIndex;
		xueyangLayout.invalidate();
		xueyangTv.setText(user_ox+"%");
	}
	
	int slump_level = 0;
	int slump_rate = 0;

	/**
	 * 
	 * 1 瘫倒级别为5级（即瘫倒概率为50%）：瘫倒概率<=50% 瘫倒级别为4级（即瘫倒概率为55%）：瘫倒概率<=55%
	 * 瘫倒级别为3级（即瘫倒概率为60%）：瘫倒概率<=60% 瘫倒级别为2级（即瘫倒概率为65%）：瘫倒概率<=65%
	 * 瘫倒级别为1级（即瘫倒概率为70%）：瘫倒概率<=70%
	 * 
	 * 2 50%<瘫倒概率<70% 55%<瘫倒概率<70% 60%<瘫倒概率<75% 65%<瘫倒概率<80% 70%<瘫倒概率<85%
	 * 
	 * 3 70%<瘫倒概率 70%<瘫倒概率 75%<瘫倒概率 80%<瘫倒概率 85%<瘫倒概率
	 * 
	 * @param slump_threshold
	 */
	public void setDeitao() {
		int proIndex = 0;
		switch (slump_level) {
		case 1:
			if(slump_rate<=70){
				proIndex = 0;
			}else if(70<slump_rate&&slump_rate<85){
				proIndex = 1;
			}else if(85<slump_rate){
				proIndex = 2;
			}
			
			break;
		case 2:
			if(slump_rate<=65){
				proIndex = 0;
			}else if(65<slump_rate&&slump_rate<80){
				proIndex = 1;
			}else if(80<slump_rate){
				proIndex = 2;
			}
			break;
		case 3:
			if(slump_rate<=60){
				proIndex = 0;
			}else if(60<slump_rate&&slump_rate<75){
				proIndex = 1;
			}else if(75<slump_rate){
				proIndex = 2;
			}
			break;
		case 4:
			if(slump_rate<=55){
				proIndex = 0;
			}else if(55<slump_rate&&slump_rate<70){
				proIndex = 1;
			}else if(70<slump_rate){
				proIndex = 2;
			}
			break;
		case 5:
			if(slump_rate<=50){
				proIndex = 0;
			}else if(50<slump_rate&&slump_rate<70){
				proIndex = 1;
			}else if(70<slump_rate){
				proIndex = 2;
			}
			break;
		}
		
		// --------跌倒
		jiuhuTv.setText(slump_rate + "%");
		jiuhuOLaout.index = proIndex;
		jiuhuOLaout.invalidate();
	}

	public void setWard(getWardPhyResponse res) {
		try {
			setXinLv(res);
			// -----------血氧
			setXueYang(res);
			slump_rate = res.p.getSlump_rate();
			setDeitao();
		} catch (Exception e) {
			MyToast.showToast(activity, "数据解析出错咯");
		}
	}

	ImageView center;
	LinearLayout alert_layout;
	ImageView alert_img_more;
	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_frag, container, false);
		this.inflater = inflater;

		alert_layout = (LinearLayout) view.findViewById(R.id.alert_layout);
		alert_img_more = (ImageView) view.findViewById(R.id.alert_more_img);
		alert_img_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent in = new Intent();
				in.setClass(activity, AlertActivity.class);
				in.putExtra("showType", 0);
				activity.startActivity(in);
			}
		});
		center = (ImageView) view.findViewById(R.id.center);
		center.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (wardPhy != null) {
					setPoint(wardPhy);
				}
			}
		});
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.mapReceiver);
		menuRecever = new mapRecei();
		getActivity().registerReceiver(menuRecever, filter);

		regMsg(Constants.push_RELEASESOS, msgCallback);
		regMsg(Constants.push_SOS, msgCallback);
		regMsg(Constants.msg_SOS_releace, msgCallback);
		regMsg(Constants.updateMap, msgCallback);

		ThreadUtil.getUIHandler().post(new Runnable() {

			@Override
			public void run() {
				requestSleep_time();
				requestSem();
				requestfetchWardPhyRange();
			}
		});

		return view;
	}

	private void requestfetchWardPhyRange() {

		if (app.bindWardLists.size() == 0) {
			return;
		}
		String url = "http://112.74.95.154:8080/iAcron/fetchWardPhyRange";

		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				fetchWardPhyRangeResponse rangeObj = new fetchWardPhyRangeResponse(
						activity);
				rangeObj.parse(data);
				if (rangeObj.result == 1) {
					slump_level = rangeObj.bean.getSlump_level();
					setDeitao();
					return;
				}

			}

			@Override
			public void onAppError(int error, Throwable message) {
			}

			@Override
			public void onFailure(int error, Throwable message) {
			}

		});
		client.post(url, rp.getParams());

	}

	/**
	 * 
	 * @param day
	 *            0 = 当天；1等于上一天
	 * @return
	 */
	public String getTime(int day) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.add(Calendar.DAY_OF_YEAR, day);
		Date date = calendar.getTime();
		return sdf.format(date);
	}

	public void requestSem() {

		Log.i("UI", "fetchWardHistoryPhy = 请求开始");
		String url = "http://112.74.95.154:8080/iAcron/fetchWardHistoryPhy";
		Common com = new Common();
		com.setKV("ward_id", "" + bean.getWard_id());
		com.setKV("from_date", "" + getTime(-1));
		com.setKV("to_date", "" + getTime(0));
		com.setKV("phy_type", "step_num");

		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				Log.i("UI", "fetchWardHistoryPhy = 请求完成" + data);
				fetchWardHistory fetch = new fetchWardHistory(activity);
				fetch.parse(data);
				if (fetch.result == 1) {
					setBufa(fetch.plan_step_num, fetch.lists.get(0).getValue());
				} else {

				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				Log.i("UI", "fetchWardHistoryPhy = 请求 onAppError");
			}

			@Override
			public void onFailure(int error, Throwable message) {
				Log.i("UI", "fetchWardHistoryPhy = 请求 onFailure");
			}

		});
		client.post(url, com.getParams());

	}

	private void requestSleep_time() {
		Log.i("UI", "fetchWardHistoryPhy = 请求开始");
		String url = "http://112.74.95.154:8080/iAcron/fetchWardHistoryPhy";
		Common com = new Common();
		com.setKV("ward_id", "" + bean.getWard_id());
		com.setKV("from_date", "" + getTime(-1));
		com.setKV("to_date", "" + getTime(0));
		com.setKV("phy_type", "sleep_time");

		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				Log.i("UI", "fetchWardHistoryPhy = 请求完成" + data);
				fetchWardHistory fetch = new fetchWardHistory(activity);
				fetch.parse(data);
				if (fetch.result == 1) {
					setSleep(fetch.sleep_time_long, fetch.lists.get(0)
							.getValue());
				} else {

				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				Log.i("UI", "fetchWardHistoryPhy = 请求 onAppError");
			}

			@Override
			public void onFailure(int error, Throwable message) {
				Log.i("UI", "fetchWardHistoryPhy = 请求 onFailure");
			}

		});
		client.post(url, com.getParams());
	}

	public ArrayList<getWardReindBean> lists;

	public void requestAlert() {
		Common rp = new Common();
		rp.setKV("ward_id", "" + bean.getWard_id());

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						getWardRemindListResponse q = new getWardRemindListResponse(
								activity);
						q.parse(data);
						if (q.result == 1) {
							int size = q.lists.size();
							if (null != q.lists && size >= 1) { // 如果有绑定过被被监护人
								lists = q.lists;
								int lists = size > 3 ? 3 : q.lists.size();
								alert_img_more.setVisibility(View.VISIBLE);
								for (int i = 0; i < lists; i++) {
									View view = inflater
											.inflate(
													R.layout.comon_fram_alertitem,
													null);
									final TextView common_fram_alertitem_tv = (TextView) view
											.findViewById(R.id.common_fram_alertitem_tv);
									getWardReindBean bean = q.lists.get(i);

									common_fram_alertitem_tv
											.setText(bean.remind_content);
									common_fram_alertitem_tv.setTag(bean);
									common_fram_alertitem_tv
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View arg0) {
													Intent in = new Intent();
													in.setClass(activity,
															AlertActivity.class);
													in.putExtra("showType", 1);
													Bundle b = new Bundle();
													b.putSerializable(
															"obj",
															(getWardReindBean) arg0
																	.getTag());
													in.putExtras(b);
													activity.startActivity(in);
												}
											});
									alert_layout.addView(view);
								}

							}
						} else {
							alert_img_more.setVisibility(View.GONE);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {

					}

					@Override
					public void onFailure(int error, Throwable message) {

					}

				});
		client.post("http://112.74.95.154:8080/iAcron/getWardRemindList",
				rp.getParams());
	}

	mapRecei menuRecever;

	public class mapRecei extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			mMapView.onResume();
		}

	}

	public void onPause() {
		LogUtil.i("map", "onPause");
		mMapView.onPause();
		// todo ...
		super.onPause();
	}

	@Override
	public void onResume() {
		LogUtil.i("map", "onResume");
		try {
			mMapView.onResume();
		} catch (Exception e) {
			// TODO: handle exceptionp
			e.printStackTrace();
		}

		// todo ...
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bufaOnClick:
			bufaOnClick(v);
			break;
		case R.id.timeOnClick:
			timeOnClick(v);
			break;
		case R.id.xueyangOnClick:
			xueyangeOnClick(v);
			break;
		case R.id.xintiaoOnClick:
			xintiaoOnClick(v);
			break;
		case R.id.jiuhuOnClick:
			jiuhuOnClick(v);
			break;

		default:
			break;
		}

	}

	public final static String BUFA = "step_num";
	public final static String TIME = "sleep_time";
	public final static String XINLUE = "heart_rate";
	public final static String XUEYANG = "blood_oxygen";
	public final static String JIUHU = "slump_num";

	/**
	 * 步伐
	 * 
	 * @param view
	 */
	public void bufaOnClick(View view) {
		Intent in = new Intent();
		in.setClass(getActivity(), BrokenLineView.class);
		in.putExtra("obj", bean);
		bean.setType(BUFA);
		startActivity(in);
	}

	/**
	 * 心跳
	 * 
	 * @param view
	 */
	public void timeOnClick(View view) {
		Intent in = new Intent();
		in.setClass(getActivity(), BrokenLineView.class);
		bean.setType(TIME);
		in.putExtra("obj", bean);
		startActivity(in);
	}

	/**
	 * 心率
	 * 
	 * @param view
	 */
	public void xueyangeOnClick(View view) {
		Intent in = new Intent();
		in.setClass(getActivity(), BrokenLineView.class);
		bean.setType(XUEYANG);
		in.putExtra("obj", bean);
		startActivity(in);
	}

	/**
	 * 
	 * @param view
	 */
	public void xintiaoOnClick(View view) {
		Intent in = new Intent();
		in.setClass(getActivity(), BrokenLineView.class);
		bean.setType(XINLUE);
		in.putExtra("obj", bean);
		startActivity(in);
	}

	/**
	 * 救护
	 * 
	 * @param view
	 */
	public void jiuhuOnClick(View view) {
		Intent in = new Intent();
		in.setClass(getActivity(), BrokenLineView.class);
		bean.setType(JIUHU);
		in.putExtra("obj", bean);
		startActivity(in);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.i("MAP", "onDestory");
		super.onDestroy();
	}
}
