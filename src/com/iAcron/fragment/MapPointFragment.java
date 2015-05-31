package com.iAcron.fragment;

import com.baidu.mapapi.map.BaiduMap;

import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.R;
import com.iAcron.SafetyActivity;
import com.iAcron.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/** 六边形
 LatLng pt1 = new LatLng(39.93923, 116.357428);  
LatLng pt2 = new LatLng(39.91923, 116.327428);  
LatLng pt3 = new LatLng(39.89923, 116.347428);  
LatLng pt4 = new LatLng(39.89923, 116.367428);  
LatLng pt5 = new LatLng(39.91923, 116.387428);  
List<LatLng> pts = new ArrayList<LatLng>();  
pts.add(pt1);  
pts.add(pt2);  
pts.add(pt3);  
pts.add(pt4);  
pts.add(pt5);  
//构建用户绘制多边形的Option对象  
OverlayOptions polygonOption = new PolygonOptions()  
    .points(pts)  
    .stroke(new Stroke(5, 0xAA00FF00))  
    .fillColor(0xAAFFFF00);  
//在地图上添加多边形Option，用于显示  
mBaiduMap.addOverlay(polygonOption);

 * @author jorry
 *
 */
public class MapPointFragment extends BaseFragmentActivity implements OnMapClickListener {

	MapView mMapView;
	BaiduMap mBaiduMap;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.ic_launcher);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mappoint);

		findViewById(R.id.shenglixinxi).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in = new Intent();
						Bundle bundle = new Bundle();
						bundle.putDouble("lat", latPoi);
						bundle.putDouble("long", longPoi);
						in.putExtras(bundle);
						setResult(200, in);
						finish();
					}
				});
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapClickListener(this);
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		LogUtil.i("MAP", arg0.latitude + "onMapClick 地图" + arg0.longitude);

		mBaiduMap.clear();

		latPoi = arg0.latitude;
		longPoi = arg0.longitude;

		LatLng llA = new LatLng(arg0.latitude, arg0.longitude);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9);
		Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

	}

	SafetyActivity acitivity;

	public void setResult() {
		Intent in = new Intent();
		Bundle bundle = new Bundle();
		bundle.putDouble("lat", latPoi);
		bundle.putDouble("long", longPoi);
		in.putExtras(bundle);
	}

	public double latPoi;
	public double longPoi;

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		LogUtil.i("MAP", "onMapPoiClick 地图");
		return false;
	}

	public static MapPointFragment newInstance() {
		// TODO Auto-generated method stub
		MapPointFragment po = new MapPointFragment();

		return po;
	};
}
