package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.iAcon.response.bean.HistoryBean;

import android.content.Context;

public class fetchWardHistory extends BaseResponse {

	public String phy_type;

	public int plan_step_num;
	public float blood_oxygen_low;
	public float blood_oxygen_height;
	public float sleep_time_long;
	public int heart_rate_low;
	public int heart_rate_high;

	public ArrayList<HistoryBean> lists = new ArrayList<HistoryBean>();

	public fetchWardHistory(Context context) {
		super(context);
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(this.result!=1){
			return;
		}
		JSONObject main = jobj.getJSONObject("result_data");
		if (main.has("phy_type")) {
			phy_type = main.getString("phy_type");
		}

		if (main.has("plan_step_num")) {
			plan_step_num = main.getInt("plan_step_num");
		}
		if (main.has("blood_oxygen_low")) {
			blood_oxygen_low = (float) main.getDouble("blood_oxygen_low");
		}

		if (main.has("blood_oxygen_high")) {
			blood_oxygen_height = (float) main.getDouble("blood_oxygen_high");
		}

		if (main.has("sleep_time_long")) {
			sleep_time_long = (float) main.getDouble("sleep_time_long");
		}

		if (main.has("heart_rate_low")) {
			heart_rate_low = main.getInt("heart_rate_low");
		}
		if (main.has("heart_rate_high")) {
			heart_rate_high = main.getInt("heart_rate_high");
		}

		if (main.has("history_data")) {
			JSONArray arrs = main.getJSONArray("history_data");
			for (int i = 0; i < arrs.length(); i++) {
				JSONObject o = arrs.getJSONObject(i);
				String p = o.getString("phy_date");
				String v = o.getString("value");
				HistoryBean b = new HistoryBean();
				b.setPhy_date(p);
				b.setValue(v);
				lists.add(b);
			}
		}
	}

	/**
	 * {“phy_type”:”step_num”,”plan_step_num”:”1200”,“history_data”: [
	 * {“phy_date”:”2014-9-8”,”value”:”320”},
	 * {“phy_date”:”2014-9-9”,”value”:”310”},
	 * {“phy_date”:”2014-9-10”,”value”:”330”},
	 * {“phy_date”:”2014-9-11”,”value”:”420”},
	 * {“phy_date”:”2014-9-12”,”value”:”120”},
	 * {“phy_date”:”2014-9-13”,”value”:”420”},
	 * {“phy_date”:”2014-9-14”,”value”:”610”}, ]}
	 */
}
