package com.iAcron;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.fetchWardFenceResponse;
import com.iAcon.response.fetchWardPhyRangeResponse;
import com.iAcon.response.bean.ward_fence.points;
import com.iAcron.adapter.SpinnerAdapter;
import com.iAcron.data.Constants;
import com.iAcron.data.ShengliXinxi;
import com.iAcron.fragment.MapPoint;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MyToast;
import com.loopj.android.http.RequestParams;

/**
 * 安全管理
 * 
 * @author jorry
 * 
 */
public class SafetyActivity extends BaseFragmentActivity {

	private String TAG = SafetyActivity.class.getSimpleName();

	private EditText banjing;
	private EditText wucha;
	private EditText map;
	private EditText yunDong;
	private EditText xueyang_start;
	private EditText xueyang_end;
	private EditText xinlue_start;
	private EditText xinlue_end;

	private EditText shuimian;
	private Button save;
	SafetyActivity acitivity;
	public Spinner typeSn;
	Spinner tandaoSn;
	private LinearLayout weilan;
	ShengliXinxi shengliObj = new ShengliXinxi();

	iAconApplication app;

	public String[] mItems = { "圆形", "多边形" };
	public String[] taodaoMitems = { "一级", "二级","三级","四级", "五级"};

	public static final int ONE_POINT = 1;
	public static final int MORE_POINT = 2;
	public static final int CLEAR_ALL = 3;
	int selectOpiont;
	int tandaoSelectOptiont;

	fetchWardFenceResponse r;
	fetchWardPhyRangeResponse rangeObj;

	
	public String petToJson() {  
        String jsonresult = "";//定义返回字符串  
        
        return jsonresult;  
    }
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safety);
		onFinishInflate();
		
		petToJson();
		acitivity = this;
		app = (iAconApplication) getApplication();
		regMsg(ONE_POINT, msgCallback);
		regMsg(MORE_POINT, msgCallback);
		regMsg(CLEAR_ALL, msgCallback);

		setTandaoAdapter(taodaoMitems);
		setAdapter(mItems);
		
		setSpinner(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectOpiont = arg2;
				map.setText("");
				if (arg2 == 1) {
					weilan.setVisibility(View.GONE);
				} else {

					weilan.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		setTandaoSpinner(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				tandaoSelectOptiont = arg2+1;				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		findViewById(R.id.goMap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent();
				Bundle b = new Bundle();
				b.putInt("selectOpiont", selectOpiont);
				in.putExtras(b);
				in.setClass(SafetyActivity.this, MapPoint.class);
				startActivityForResult(in, 101);
			}
		});

		requestfetchWardPhyRange();
		requestfetchWardFence();
	}



	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case CLEAR_ALL:
				System.out.println("广播   清楚全部");
				clearAll();
				requestfetchWardFence();
				requestfetchWardPhyRange();
				break;
			case ONE_POINT:
				r.getC().getPointsList().clear();

				Bundle b = (Bundle) msg.obj;
				double lat = b.getDouble("lat", 0.0);
				double lon = b.getDouble("long", 0.0);
				r.getC().getCenterPoint().latS = "" + lat;
				r.getC().getCenterPoint().longS = "" + lon;
				setMap();

				break;
			case MORE_POINT:
				List<LatLng> sixPts = (List<LatLng>) msg.obj;
				for (int i = 0; i < sixPts.size(); i++) {
					double latP = sixPts.get(i).latitude;
					double lonP = sixPts.get(i).longitude;
					points p = r.getC().getPoint();
					p.longS = "" + lonP;
					p.latS = "" + latP;
					r.getC().getPointsList().add(p);
				}
				r.getC().setCenterPoint(null);
				setMap();
				break;
			default:
				break;
			}

			return false;
		}
	};

	private void setMap() {
		StringBuilder sb = new StringBuilder();
		if (selectOpiont == 0) {
			sb.append("(").append(r.getC().getCenterPoint().latS).append(",")
					.append(r.getC().getCenterPoint().longS).append(")");
		} else {
			sb.append("(");
			List<points> pointsList = r.getC().getPointsList();
			for (int i = 0; i < pointsList.size(); i++) {
				points ll = pointsList.get(i);
				if (i != pointsList.size() - 1) {
					sb.append("" + ll.latS).append(",").append(ll.longS);

				} else {
					sb.append("" + ll.latS).append(",").append(ll.longS)
							.append(";");
				}
			}
			sb.append(")");
		}

		map.setText(sb.toString());
	}

	private void requestfetchWardPhyRange() {

		if (app.bindWardLists.size() == 0) {
			return;
		}
		String url = "http://112.74.95.154:8080/iAcron/fetchWardPhyRange";

		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());

		startWaitDialog("网络连接", "努力加载中.....", true);
		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				finishWaitDialog();
				rangeObj = new fetchWardPhyRangeResponse(acitivity);
				rangeObj.parse(data);
				if (rangeObj.result == 1) {
					setPhyRange();
					return;
				}
				MyToast.showToast(acitivity, rangeObj.result_msg);

			}

			@Override
			public void onAppError(int error, Throwable message) {
				r = new fetchWardFenceResponse(acitivity);
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				r = new fetchWardFenceResponse(acitivity);
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());

	}

	protected void setPhyRange() {
		shuimian.setText("" + rangeObj.bean.getSleep_time_long());
		xinlue_start.setText("" + rangeObj.bean.getHeart_rate_low());
		xinlue_end.setText("" + rangeObj.bean.getHeart_rate_high());

		xueyang_start.setText("" + rangeObj.bean.getBlood_oxygen_low());
		xueyang_end.setText("100");
		yunDong.setText("" + rangeObj.bean.getPlan_step_num());
		tandaoSelectOptiont = rangeObj.bean.getSlump_level();
		
		tandaoSn.setSelection(rangeObj.bean.getSlump_level()-1);

	}

	public void requestfetchWardFence() {
		if (app.bindWardLists.size() == 0) {
			return;
		}
		String url = "http://112.74.95.154:8080/iAcron/fetchWardFence";

		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());

		startWaitDialog("网络连接", "努力加载中.....", true);
		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				finishWaitDialog();
				r = new fetchWardFenceResponse(acitivity);
				r.parse(data);
				if (r.result == 1) {
					if (!TextUtils.isEmpty(r.getC().getCenterPoint().latS)) {
						selectOpiont = 0;
						typeSn.setSelection(0);
						banjing.setText("" + r.getC().getRadius());
						wucha.setText("" + r.getC().getError_range());
						setMap();
					} else {
						selectOpiont = 1;
						typeSn.setSelection(1);
						wucha.setText("" + r.getC().getError_range());
						setMap();
					}
				} else {
					MyToast.showToast(acitivity, r.result_msg);
				}

			}

			@Override
			public void onAppError(int error, Throwable message) {
				r = new fetchWardFenceResponse(acitivity);
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				r = new fetchWardFenceResponse(acitivity);
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());

	}

	public boolean isNull(EditText edit) {
		return TextUtils.isEmpty(edit.getText().toString());

	}

	public String isAlloK() {
		if (isNull(xueyang_end) || isNull(xueyang_start) || isNull(xinlue_end)
				|| isNull(xinlue_start) || isNull(shuimian) || isNull(yunDong)) {
			MyToast.showToast(getParent(), "请把资料填写完整");
			return null;
		}

		shengliObj.setWard_id(app.bindWardLists.get(app.bindWardSelect)
				.getWard_id());
		shengliObj.setBlood_oxygen_high(""
				+ Float.parseFloat(xueyang_end.getText().toString()));
		shengliObj.setBlood_oxygen_low(""
				+ Float.parseFloat(xueyang_start.getText().toString()));

		shengliObj.setHeart_rate_low(Integer.parseInt(xinlue_start.getText()
				.toString()));
		shengliObj.setHeart_rate_high((int) Float.parseFloat(xinlue_end
				.getText().toString()));

		shengliObj.setSleep_time_long(""
				+ Float.parseFloat(shuimian.getText().toString()));
		shengliObj.setPlan_step_num(Integer.parseInt(yunDong.getText()
				.toString()));
		shengliObj.setSlump_level(tandaoSn.getSelectedItemPosition()+1);
		return JSON.toJSONString(shengliObj).replace("lot", "long");
	}

	public String isWeilanOk() {
		if (selectOpiont == 0) {
			if (isNull(wucha) || isNull(banjing) || isNull(map)) {
				MyToast.showToast(getParent(), "请把资料填写完整");
				return null;
			}
		} else {
			if (isNull(wucha) || isNull(map)) {
				MyToast.showToast(getParent(), "请把资料填写完整");
				return null;
			}
		}
		if (selectOpiont == 0) { // 圆心
			if (TextUtils.isEmpty(map.getText().toString())) {
				return null;
			}
			org.json.JSONObject main = new org.json.JSONObject();
			try {
				org.json.JSONObject centerJson = new org.json.JSONObject();
				centerJson.put("ew", "E");
				centerJson.put("ns", "N");
				centerJson.put("long", r.getC().getCenterPoint().longS);
				centerJson.put("lat", r.getC().getCenterPoint().latS);

				int type = selectOpiont + 1;
				main.put("fenceType", type);
				main.put("radius",
						Double.parseDouble(banjing.getText().toString()));
				main.put("error_range",
						"" + Double.parseDouble(wucha.getText().toString()));

				main.put("ward_id", app.bindWardLists.get(app.bindWardSelect)
						.getWard_id());

				main.put("center", centerJson.toString());

			} catch (Exception e) {
				// TODO: handle exception
			}

			return main.toString();
		} else { // 多边形
			if (r.getC().getPointsList().size() == 0) {
				return null;
			}

			/**
			 * {"fence_type":2,"error_range":1.8,"points":[{"ew":"E","ns":"N",
			 * "long":"2310.1210","lat":"11327.4938"},{"ew":"E","ns":"N","long":
			 * "2310.1210"
			 * ,"lat":"11327.4938"},{"ew":"E","ns":"N","long":"2310.1240"
			 * ,"lat":"11327.4222"}]}
			 */
			org.json.JSONObject main = new org.json.JSONObject();

			try {

				main.put("ward_id", app.bindWardLists.get(app.bindWardSelect)
						.getWard_id());
				int type = selectOpiont + 1;
				main.put("fence_type", type);
				main.put("error_range",
						"" + Double.parseDouble(wucha.getText().toString()));
				List<points> lists = r.getC().getPointsList();
				JSONArray ajosn = new JSONArray();

				for (int i = 0; i < lists.size(); i++) {

					JSONObject o = new JSONObject();
					o.put("ew", "E");
					o.put("ns", "N");
					o.put("lat", Double.parseDouble(lists.get(i).latS));
					o.put("long", Double.parseDouble(lists.get(i).longS));
					ajosn.put(o);
				}
				main.put("points", ajosn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return main.toString();
		}
	}

	public void clearAll() {
		shuimian.setText(null);
		xueyang_start.setText(null);
		xueyang_end.setText(null);
		xinlue_start.setText(null);
		xinlue_end.setText(null);
		yunDong.setText(null);
		map.setText(null);
		wucha.setText(null);
		banjing.setText(null);
		selectOpiont = 0;
	}

	public void goneInput(EditText et) {
		// et.setInputType(InputType.TYPE_NULL);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

	}

	protected void onFinishInflate() {
		tandaoSn = (Spinner)findViewById(R.id.safety_detials_taodan_spinner);
		
		weilan = (LinearLayout) findViewById(R.id.dianzhiweilan);
		shuimian = (EditText) findViewById(R.id.safety_shuimian);
		shuimian.setOnTouchListener(onTouchListener);
		goneInput(shuimian);

		xueyang_start = (EditText) findViewById(R.id.safety_xy_start);
		xueyang_start.setOnTouchListener(onTouchListener);
		goneInput(xueyang_start);

		xueyang_end = (EditText) findViewById(R.id.safety_xy_end);
		xueyang_end.setOnTouchListener(onTouchListener);
		goneInput(xueyang_end);

		xinlue_start = (EditText) findViewById(R.id.safety_xl_start);
		xinlue_start.setOnTouchListener(onTouchListener);
		goneInput(xinlue_start);

		xinlue_end = (EditText) findViewById(R.id.safety_xl_end);
		xinlue_end.setOnTouchListener(onTouchListener);
		goneInput(xinlue_end);

		yunDong = (EditText) findViewById(R.id.safety_yd_plan);
		yunDong.setOnTouchListener(onTouchListener);
		goneInput(yunDong);

		map = (EditText) findViewById(R.id.safety_map);
		wucha = (EditText) findViewById(R.id.safety_wucha);
		wucha.setOnTouchListener(onTouchListener);
		goneInput(wucha);

		banjing = (EditText) findViewById(R.id.safety_banjing);
		banjing.setOnTouchListener(onTouchListener);
		goneInput(banjing);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkInfo();

			}
		});
		typeSn = (Spinner) findViewById(R.id.safety_detials_spinner);
	}

	public void setAdapter(String[] mItems) {

		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		System.out.println(typeSn + "   " + _Adapter);
		typeSn.setAdapter(new SpinnerAdapter(mItems, this));
	}

	private void setTandaoAdapter(String[] taodaoMitems2) {
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		System.out.println(typeSn + "   " + _Adapter);
		tandaoSn.setAdapter(new SpinnerAdapter(taodaoMitems2, this));
	}
	
	
	public void setSpinner(OnItemSelectedListener select) {
		typeSn.setOnItemSelectedListener(select);
	}
	public void setTandaoSpinner(OnItemSelectedListener select){
		tandaoSn.setOnItemSelectedListener(select);
	}
	public int stringToInteger(EditText tv) {
		try {
			if (TextUtils.isEmpty(tv.getText().toString())) {
				return 0;
			}
			return Integer.parseInt(tv.getText().toString());
		} catch (Exception e) {
			return (int) Double.parseDouble(tv.getText().toString());
		}

	}

	public void checkInfo() {
		String ward_fence = isAlloK();
		String ward_wl_fence = isWeilanOk();

		Log.i("safe", "centerToJson = " + ward_wl_fence);
		if (ward_fence != null && ward_wl_fence != null) {

			if (stringToInteger(xinlue_start) < 30
					|| stringToInteger(xinlue_end) > 100) {
				MyToast.showToast(acitivity, "心率范围是30-100");
				return;
			}
			if (stringToInteger(shuimian) < 0 || stringToInteger(shuimian) > 24) {
				MyToast.showToast(acitivity, "睡眠时间范围是0-24");
				return;
			}

			if (stringToInteger(xueyang_start) < 90
					|| stringToInteger(xueyang_end) > 100) {
				MyToast.showToast(acitivity, "血氧时间范围是90%-100%");
				return;
			}
			if (stringToInteger(wucha) < 0 || stringToInteger(wucha) > 100) {
				MyToast.showToast(acitivity, "误差时间范围是0-1000米");
				return;
			}

			if (stringToInteger(banjing) < 0 || stringToInteger(banjing) > 5) {
				MyToast.showToast(acitivity, "时间范围是0-5公里");
				return;
			}

			request(ward_fence, ward_wl_fence);
		}
	}

	public View.OnTouchListener onTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {

				if (arg0 == shuimian) {
					openWaistlineDialog("睡眠时间", 0, 24, 1, shuimian);
				} else if (arg0 == xinlue_start) {
					openWaistlineDialog("心率最低阀值", 30, 100, 50, xinlue_start);
				}
				if (arg0 == xinlue_end) {
					openWaistlineDialog("心率最高阀值", 40, 180, 90, xinlue_end);
				} else if (arg0 == xueyang_start) {
					openWaistlineDialog("血氧（百分比）", 90, 100, 90, xueyang_start);
				}
				if (arg0 == xueyang_end) {
					openWaistlineDialog("血氧（百分比）", 90, 100, 100, xueyang_end);
				} else if (arg0 == yunDong) {
					openWaistlineDialog("运动（步伐）", 0, 100000, 1000, yunDong);
				}
				 if (arg0 == banjing) {
					openWaistlineDialog("电子围栏半径（公里）", 0, 5, 0, banjing);
				} else if (arg0 == wucha) {
					openWaistlineDialog("误差范围（米）", 0, 5, 0, wucha);
				}
			}
			return false;
		}
	};

	/**
	 * 腰围弹出框
	 */
	@SuppressLint("NewApi")
	private void openWaistlineDialog(String title, int min, int max, int value,
			final EditText et) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(title);
		final NumberPicker numberPicker = new NumberPicker(this);
		numberPicker
				.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		numberPicker.setMinValue(min);
		numberPicker.setMaxValue(max);
		numberPicker.setValue(value);
		builder.setView(numberPicker);
		builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				et.setText("" + numberPicker.getValue());
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/*
	 * 8 ward_id Int 被监护人ID。必须 plan_step_num Int 计划每天总步数 sleep_time_long Float
	 * 计划每天睡觉时间（小时） heart_rate_low Int 心率最低值 heart_rate_high Int 心率最高值
	 * blood_oxygen_low Float 血氧最低百分比 blood_oxygen_high Float 血氧最高百分比
	 */
	public void request(String ward_phy_range, String ward_wl_fence) {

		String url = "http://112.74.95.154:8080/iAcron/updateWardPhyRangeAndFence";

		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("ward_phy_range", ward_phy_range);
		rp.setKV("ward_fence", ward_wl_fence);
		reqeust(url, rp.getParams());
	}

	public void reqeust(String url, RequestParams rp) {
		startWaitDialog("网络连接", "努力加载中.....", true);
		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				BaseResponse r = new BaseResponse(acitivity);
				r.parse(data);
				finishWaitDialog();
				if (r.result == 1) {
					ThreadUtil.sendMessage(Constants.openMenu);
					ThreadUtil.sendMessage(Constants.updateMap);

				} else {
					MyToast.showToast(acitivity, r.result_msg);
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
		client.post(url, rp);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		clearAll();
		super.onDestroy();
	}
}
