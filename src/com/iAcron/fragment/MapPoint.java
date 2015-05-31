package com.iAcron.fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;

import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.iAcron.BaseActivity;
import com.iAcron.R;
import com.iAcron.SafetyActivity;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MyToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 六边形 LatLng pt1 = new LatLng(39.93923, 116.357428); LatLng pt2 = new
 * LatLng(39.91923, 116.327428); LatLng pt3 = new LatLng(39.89923, 116.347428);
 * LatLng pt4 = new LatLng(39.89923, 116.367428); LatLng pt5 = new
 * LatLng(39.91923, 116.387428); List<LatLng> pts = new ArrayList<LatLng>();
 * pts.add(pt1); pts.add(pt2); pts.add(pt3); pts.add(pt4); pts.add(pt5);
 * //构建用户绘制多边形的Option对象 OverlayOptions polygonOption = new PolygonOptions()
 * .points(pts) .stroke(new Stroke(5, 0xAA00FF00)) .fillColor(0xAAFFFF00);
 * //在地图上添加多边形Option，用于显示 mBaiduMap.addOverlay(polygonOption);
 * 
 * @author jorry
 * 
 */
public class MapPoint extends BaseActivity implements OnMapClickListener {

	MapView mMapView;
	BaiduMap mBaiduMap;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.u155);

	int selectOpiont = 0;
	
	/**
	 * 	b.putSerializable("range", rangeObj.bean);
	    b.putSerializable("fence", r.getC());
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mappoint);
		
		TextView title = (TextView)findViewById(R.id.topbar_title);
		title.setText("地图选点");
		findViewById(R.id.menuBtn).setVisibility(View.GONE);
		Button homeBtn = (Button)findViewById(R.id.homeBtn);
		homeBtn.setVisibility(View.VISIBLE);
		homeBtn.setBackgroundResource(R.drawable.u8);
		homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		selectOpiont = getIntent().getExtras().getInt("selectOpiont");
		findViewById(R.id.reset).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sixPts.clear();
				mBaiduMap.clear();
			}
		});
		findViewById(R.id.shenglixinxi).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						if(selectOpiont==0){
							if(latPoi<=0){
								MyToast.showToast(MapPoint.this, "请选择一个坐标点");
								return;
							}
							Bundle bundle = new Bundle();
							bundle.putDouble("lat", latPoi);
							bundle.putDouble("long", longPoi);
							ThreadUtil.sendMessage(SafetyActivity.ONE_POINT,bundle);
						}else if(selectOpiont == 1){
							if(sixPts.size()<6){
								MyToast.showToast(MapPoint.this, "请选择6个坐标点");
								return;
							}
							ThreadUtil.sendMessage(SafetyActivity.MORE_POINT,sixPts);
						}
						
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
		if (selectOpiont == 0) {
			// 原型
			mBaiduMap.clear();

			latPoi = arg0.latitude;
			longPoi = arg0.longitude;

			LatLng llA = new LatLng(arg0.latitude, arg0.longitude);

			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
					.zIndex(9);
			Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		} else {
			// 多边形
			LatLng llA = new LatLng(arg0.latitude, arg0.longitude);
			six(llA);
		}

	}

	List<LatLng> sixPts = new ArrayList<LatLng>();
	
	/**
	 * LatLng pt1 = new LatLng(39.93923, 116.357428); LatLng pt2 = new
	 * LatLng(39.91923, 116.327428); LatLng pt3 = new LatLng(39.89923,
	 * 116.347428); LatLng pt4 = new LatLng(39.89923, 116.367428); LatLng pt5 =
	 * new LatLng(39.91923, 116.387428);
	 * 
	 * @param pt
	 */
	public void six(LatLng pt) {
		System.out.println("--sixPts-"+sixPts.size());
		if(sixPts.size()<6){
			OverlayOptions ooA = new MarkerOptions().position(pt).icon(bdA)
					.zIndex(9);
			Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			sixPts.add(pt);
			
			if (sixPts.size() == 6) {
				// // 构建用户绘制多边形的Option对象
				OverlayOptions polygonOption = new PolygonOptions().points(sixPts)
						.stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
				// 在地图上添加多边形Option，用于显示
				mBaiduMap.addOverlay(polygonOption);
			}
		}
		
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

}
