package com.iAcron;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

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
import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.getWardPhyResponse;
import com.iAcon.response.bean.ward_fence.points;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.BrokenLineView;
import com.iAcron.ChatMessage;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.util.Util;
import com.iAcron.view.MyToast;
import com.iAcron.view.ProgressLayout;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
@SuppressLint("HandlerLeak")
public class RealTimeActivity extends BaseFragmentActivity implements
		OnClickListener {

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
	ProgressBar bufaPro, sleepPro;
	TextView bufaTv, sleepTv;

	ProgressLayout xintiao, jiuhu, xueyang;
	TextView xintiaoTv, jiuhuTv, xueyangTv;

	TextView hTv;
	TextView mTv;

	iAconApplication app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		app = (iAconApplication) getApplication();
		View view = getLayoutInflater().inflate(R.layout.common_realtime, null);
		setContentView(view);
		hTv = (TextView) view.findViewById(R.id.h_tv);
		mTv = (TextView) view.findViewById(R.id.m_tv);
		bean = app.bindWardLists.get(app.bindWardSelect);
		System.out.println("bean = " + bean.getWard_name() + "   "
				+ bean.getWard_id());
		view.findViewById(R.id.chatlayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转到聊天界面
						Intent in = new Intent();
						in.setClass(RealTimeActivity.this, ChatMessage.class);
						startActivity(in);
					}
				});

		view.findViewById(R.id.tellayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				});

		bufaPro = (ProgressBar) view.findViewById(R.id.common_bufa_pro);
		bufaTv = (TextView) view.findViewById(R.id.common_bufa_tv);
		sleepPro = (ProgressBar) view.findViewById(R.id.common_time_pro);
		sleepTv = (TextView) view.findViewById(R.id.common_time_tv);

		jiuhu = (ProgressLayout) view
				.findViewById(R.id.common_jiuhu_pro_layout);
		jiuhu.setRes(res2).setKedu(new String[] { "25%", "50%", "75%" }).index = 0;

		jiuhuTv = (TextView) view.findViewById(R.id.common_jiuhu_tv);
		xintiaoTv = (TextView) view.findViewById(R.id.common_xintiao_tv);
		xueyangTv = (TextView) view.findViewById(R.id.common_xinlv_tv);
		xueyang = (ProgressLayout) view
				.findViewById(R.id.common_xinlv_pro_layout);
		xueyang.setRes(res1).setKedu(new String[] { "85%", "90%", "95%" }).index = 1;
		xintiao = (ProgressLayout) view
				.findViewById(R.id.common_xintiao_pro_layout);
		xintiao.setRes(res).setKedu(new String[] { "70", "90", "120" }).index = 2;

		// 地图初始化
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		UiSettings mapSetting = mBaiduMap.getUiSettings();
		mapSetting.setZoomGesturesEnabled(false);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(RealTimeActivity.this);

		view.findViewById(R.id.bufaOnClick).setOnClickListener(this);
		view.findViewById(R.id.timeOnClick).setOnClickListener(this);
		view.findViewById(R.id.xintiaoOnClick).setOnClickListener(this);
		view.findViewById(R.id.xinlveOnClick).setOnClickListener(this);
		view.findViewById(R.id.jiuhuOnClick).setOnClickListener(this);

		queryBindWardList();

		int count = mMapView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				child.setVisibility(View.INVISIBLE);
			}
		}
		
		TextView title = (TextView)findViewById(R.id.topbar_title);
		title.setText("实时信息");
		Button menu = (Button)findViewById(R.id.menuBtn);
		menu.setVisibility(View.GONE);
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ThreadUtil.sendMessage(Constants.openMenu);
				finish();
				
			}
		});
		Button homeBtn = (Button)findViewById(R.id.homeBtn);
		homeBtn.setVisibility(View.VISIBLE);
		homeBtn.setBackgroundResource(R.drawable.u8);
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		realTimeHandler.sendEmptyMessageAtTime(1, 60*1*1000);
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

	/**
	 * 设置电子围栏
	 * 
	 * @param res3
	 */
	/**
	 * 设置电子围栏
	 * 
	 * @param res3
	 */

	protected void setPoint(getWardPhyResponse res) {
		if(res.p==null){
			MyToast.showToast(this, "服务器内部错误");
			return;
		}
		if(!TextUtils.isEmpty(res.c.getCenterPoint().latS)){	
			addUserCenterToMap(res.c.getCenterPoint().latS,
					res.c.getCenterPoint().longS);
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
			LatLng pt = new LatLng(Double.parseDouble(p.latS), Double.parseDouble(p.longS));
			pts.add(pt);
		}
		addUserCenterToMap(""+pts.get(0).latitude,""+pts.get(0).latitude);
//
		OverlayOptions polygonOption = new PolygonOptions().points(pts)
				.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
		// 在地图上添加多边形Option，用于显示
		mBaiduMap.addOverlay(polygonOption);
		
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(pts.get(0),10);
		
		mBaiduMap.animateMapStatus(u);
		
		
		
	}

	private void addCircleToMap(String latS, String longS, double radius) {
		try {
			// 添加圆
			LatLng llCircle = new LatLng(Double.parseDouble(latS),
					Double.parseDouble(longS));
			OverlayOptions ooCircle = new CircleOptions().fillColor(0X00FF0000)
					.center(llCircle).stroke(new Stroke(2, 0xffff0000))
					.radius((int) (radius * 1000)); // 半径
			mBaiduMap.addOverlay(ooCircle);
			
			
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llCircle);
			
			mBaiduMap.animateMapStatus(u);
			
			
			
		} catch (Exception e) {

			MyToast.showToast(this, "圆形围栏坐标错误");
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
			MyToast.showToast(this, "用户坐标错误");
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
		startWaitDialog(null, null, true);
		String url = "http://112.74.95.154:8080/iAcron/getWardPhy";
		Common rp = new Common();
		rp.setKV("last_phy_time", "1");
		rp.setKV("ward_id", "" + bean.getWard_id());
		rp.setKV("need_real_time", "1");
		rp.setKV("need_ward_fence", "1");
		rp.setKV("need_phy_range", "1");

		RequstClient client = new RequstClient(RealTimeActivity.this,
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						System.out.println("data" + data);

						finishWaitDialog();
						getWardPhyResponse res = new getWardPhyResponse(
								RealTimeActivity.this);
						res.parse(data);
						if (res.result == 1) {
							setWard(res);
							setPoint(res);
						} else {
							MyToast.showToast(RealTimeActivity.this, res.result_msg);
						}

					}

					@Override
					public void onAppError(int error, Throwable message) {
						finishWaitDialog();
					}

					@Override
					public void onFailure(int error, Throwable message) {
						finishWaitDialog();
					}

				});
		client.post(url, rp.getParams());
	}

	Handler realTimeHandler  = new Handler(){
		public void handleMessage(Message msg) {
			hTv.setText(Util.getHour());
			mTv.setText(Util.getminute());
			queryBindWardList();	
			sendEmptyMessageDelayed(1, 60*1*1000);
			
		};
	};
	

	public void setBufa(getWardPhyResponse res) {
		int bufa = Util.getProgetssIndex(res.r.getPlan_step_num(),
				res.p.getStep_num());
		System.out.println("bufa = " + bufa);
		bufaPro.setProgress(bufa); // 步伐
		bufaTv.setText("" + res.p.getStep_num() + "/"
				+ res.r.getPlan_step_num());
	}

	public void setSleep(getWardPhyResponse res) {
		sleepPro.setProgress(Util.getProgetssIndex(
				(float) res.r.getSleep_time_long(),
				(float) res.p.getSleep_time())); // 睡眠
		sleepTv.setText("" + res.p.getSleep_time() + "h/"
				+ res.r.getSleep_time_long() + "h");
	}

	public void setXinLv(getWardPhyResponse res) {
		int bool_oxygen = Util.getProgressBarFour(
				(float) res.r.getHeart_rate_high(),
				(float) res.r.getHeart_rate_low(),
				(float) res.p.getHeart_rate());

		int proIndex = 0;
		if (bool_oxygen < 60) {
			proIndex = 0;
		} else if (bool_oxygen >= 60 && bool_oxygen < 90) {
			proIndex = 1;
		} else if (bool_oxygen >= 90 && bool_oxygen < 120) {
			proIndex = 2;
		} else {
			proIndex = 3;
		}

		xintiao.setRes(RealTimeActivity.res1).setKedu(
				new String[] { "60", "90", "120" }).index = proIndex;
		xintiaoTv.setText(res.p.getHeart_rate() + "次");
	}

	public void setXueYang(getWardPhyResponse res) {
		int proIndex = 0;
		int bool_oxygen = Util.getProgressBarFour(
				(float) res.r.getBlood_oxygen_high(),
				(float) res.r.getBlood_oxygen_low(),
				(float) res.p.getBlood_oxygen());
		LogUtil.i(
				"user",
				bool_oxygen + "血氧" + res.r.getBlood_oxygen_high() + "  "
						+ res.r.getBlood_oxygen_low() + "  "
						+ res.p.getBlood_oxygen());
		if (bool_oxygen < 80) {
			proIndex = 0;
		} else if (bool_oxygen >= 80 && bool_oxygen < 90) {
			proIndex = 1;
		} else if (bool_oxygen >= 90 && bool_oxygen < 93) {
			proIndex = 2;
		} else {
			proIndex = 3;
		}

		if (bool_oxygen < 0) {
			bool_oxygen = 0;
		}
		xueyang.setRes(RealTimeActivity.res1).setKedu(
				new String[] { "80%", "90%", "93%" }).index = proIndex;
		xueyangTv.setText(bool_oxygen + "%");
	}

	public void jiuhu(getWardPhyResponse res) {
		int dateIn = res.p.getSlump_rate();
		int proIndex = 0;
		if (dateIn < 25) {
			proIndex = 0;
		} else if (dateIn >= 25 && dateIn < 50) {
			proIndex = 1;
		} else if (dateIn >= 50 && dateIn < 75) {
			proIndex = 2;
		} else {
			proIndex = 3;
		}
		// --------跌倒
		jiuhuTv.setText(dateIn + "%");
		jiuhu.setRes(RealTimeActivity.res).setKedu(
				new String[] { "25%", "50%", "75%" }).index = dateIn;
	}

	public void setWard(getWardPhyResponse res) {
		try {
			setBufa(res);
			setSleep(res);
			// --------------心率
			setXinLv(res);
			// -----------血氧
			setXueYang(res);
		} catch (Exception e) {
			MyToast.showToast(RealTimeActivity.this, "数据解析出错咯");
		}
	}

	public void onPause() {
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
//			bufaOnClick(v);
			break;
		case R.id.timeOnClick:
//			timeOnClick(v);
			break;
		case R.id.xueyangOnClick:
//			xueyangeOnClick(v);
			break;
		case R.id.xintiaoOnClick:
//			xintiaoOnClick(v);
			break;
		case R.id.jiuhuOnClick:
//			jiuhuOnClick(v);
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
		in.setClass(RealTimeActivity.this, BrokenLineView.class);
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
		in.setClass(RealTimeActivity.this, BrokenLineView.class);
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
		in.setClass(RealTimeActivity.this, BrokenLineView.class);
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
		in.setClass(RealTimeActivity.this, BrokenLineView.class);
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
		in.setClass(RealTimeActivity.this, BrokenLineView.class);
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
		realTimeHandler.removeMessages(1);
		super.onDestroy();
	}
}
