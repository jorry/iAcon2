package com.iAcron.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.iAcon.response.getWardRemindListResponse;
import com.iAcon.response.bean.getWardReindBean;
import com.iAcron.AlertActivity;
import com.iAcron.R;
import com.iAcron.fragment.BaseFregment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.view.MyToast;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class AlertDetails extends BaseFregment {

	RadioGroup actionTypeRG;
	getWardReindBean bean;

	WheelView hourW;
	WheelView miniW;
	WheelView helfW;

	EditText alert_input;

	LinearLayout alert_zhou;

	String remind_type;

	public void setRadioButton(RadioButton rb) {
		remind_type = rb.getText().toString();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bean = (getWardReindBean) getArguments().getSerializable("obj");

		View view = inflater.inflate(R.layout.activity_alert_detail, null);
		view.findViewById(R.id.homeBtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						activity.finish();
					}
				});
		view.findViewById(R.id.alert_detail_submit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						requestUpdate();
					}
				});
		alert_zhou = (LinearLayout) view.findViewById(R.id.alert_zhou);
		for (int i = 0; i < alert_zhou.getChildCount(); i++) {
			alert_zhou.getChildAt(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					arg0.setSelected(!arg0.isSelected());
				}
			});
		}
		String num[] = bean.loop_value.split(",");
		for (int i = 0; i < alert_zhou.getChildCount(); i++) {
			TextView tv = (TextView) alert_zhou.getChildAt(i);
			for (int j = 0; j < num.length; j++) {
				String n = getStringToNumber(num[j]);
				if (tv.getText().toString().equals(n)) {
					tv.setSelected(true);
				}
			}
		}

		alert_input = (EditText) view.findViewById(R.id.alert_input);

		alert_input.setText(bean.remind_content);
		actionTypeRG = (RadioGroup) view
				.findViewById(R.id.alert_action_type_rg);
		actionTypeRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton rb = (RadioButton) arg0.findViewById(arg1);
				setRadioButton(rb);
			}
		});
		for (int i = 0; i < actionTypeRG.getChildCount(); i++) {
			RadioButton rb = (RadioButton) actionTypeRG.getChildAt(i);
			String rbS = rb.getText().toString();
			if (rbS.equals(bean.remind_type)) {
				rb.setChecked(true);
			}
		}

		hourW = (WheelView) view.findViewById(R.id.wheel_d);
		miniW = (WheelView) view.findViewById(R.id.wheel_h);
		helfW = (WheelView) view.findViewById(R.id.wheel_helf);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue<=12){
					helfW.setCurrentItem(0,false);
				}else{
					helfW.setCurrentItem(1, false);
				}
			}
		};
		String hour[] = new String[24];
		for (int i = 1; i <= 24; i++) {
			hour[i - 1] = i + "时";
		}

		hourW.setViewAdapter(new DateArrayAdapter(activity, hour, hour.length));
		hourW.addChangingListener(listener);
		
		String m[] = new String[60];
		for (int i = 1; i <= 60; i++) {
			m[i - 1] = i + "分";
		}

		miniW.setViewAdapter(new DateArrayAdapter(activity, m, m.length));
	

		String[] h = { "上午", "下午" };

		helfW.setViewAdapter(new DateArrayAdapter(activity, h, h.length));
		setTiime(bean.remind_time);
		return view;
	}
	

	public static String getNumToyi(String cNumber) {
		try {
			Map<Character, Character> numMap = new HashMap<Character, Character>(
					10);
			numMap.put('一', '1');
			numMap.put('二', '2');
			numMap.put('三', '3');
			numMap.put('四', '4');
			numMap.put('五', '5');
			numMap.put('六', '6');
			numMap.put('七', '7');
			numMap.put('八', '8');
			numMap.put('九', '8');
			numMap.put('零', '0');

			char[] chars = cNumber.toCharArray();
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < chars.length; i++) {
				Character number = numMap.get(chars[i]);
				if (number != null) {
					result.append(number);
				}
			}
			return result.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	
	public String getJson() {
		JSONObject j = new JSONObject();
		try {
			j.put("ward_id", ""
					+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
			
			j.put("remind_type", remind_type);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < alert_zhou.getChildCount(); i++) {
				TextView zhouTv =  (TextView) alert_zhou.getChildAt(i);
				if(zhouTv.isSelected()){
					sb.append(getNumToyi(zhouTv.getText().toString())).append(",");
				}
			}
			
			j.put("loop_value", sb.substring(0, sb.lastIndexOf(",")).toString());
			
			j.put("loop_type", bean.loop_type);
			String time = hourW.getCurrentItem()+":"+miniW.getCurrentItem()+":"+"00";
			j.put("remind_time", time);
			j.put("remind_content",alert_input.getText().toString());
			j.put("content_type",bean.content_type);
			j.put("enabled", bean.enabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return j.toString();
	}

	public void requestUpdate() {
		((AlertActivity) getActivity()).startWaitDialog("", "",false);
		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("remind_info", getJson());
		rp.setKV("remind_id", "" + bean.remind_id);

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						((AlertActivity) getActivity()).finishWaitDialog();
						getWardRemindListResponse q = new getWardRemindListResponse(
								activity);
						q.parse(data);
						if (q.result == 1) {
							int fc = getFragmentManager().getBackStackEntryCount();
							if (fc >1) {
								onStart();
								getFragmentManager().popBackStackImmediate();
							} else {
								activity.finish();
							}
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						((AlertActivity) getActivity()).finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						((AlertActivity) getActivity()).finishWaitDialog();

					}

				});
		client.post("http://112.74.95.154:8080/iAcron/updateWardRemind",
				rp.getParams());

	}

	public static String getStringToNumber(String cNumber) {

		try {
			Map<Character, Character> numMap = new HashMap<Character, Character>(
					10);
			numMap.put('1', '一');
			numMap.put('2', '二');
			numMap.put('3', '三');
			numMap.put('4', '四');
			numMap.put('5', '五');
			numMap.put('6', '六');
			numMap.put('7', '七');

			char[] chars = cNumber.toCharArray();

			StringBuilder result = new StringBuilder();
			for (int i = 0; i < chars.length; i++) {
				Character number = numMap.get(chars[i]);
				if (number != null) {
					result.append(number);
				}
			}
			return result.toString();
		} catch (Exception e) {
			return "-1";
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void setTiime(String time) {
		SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");

		Date date;
		try {
			date = sf.parse(time);
			int hour = date.getHours();

			int min = date.getMinutes();

			int secent = date.getSeconds();

			hourW.setCurrentItem(hour);
			miniW.setCurrentItem(min);

			if (secent <= 12) {
				helfW.setCurrentItem(1);
			} else {
				helfW.setCurrentItem(2);
			}

			System.out.println(hour + "   " + min + "  " + secent);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0XFF66CCCC);
			} else {
				view.setTextColor(0XFF55CCCC);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
