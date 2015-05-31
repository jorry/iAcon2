package com.iAcon.response;

import org.json.JSONObject;

import com.iAcon.response.bean.fetchWardPhyRangeBean;


import android.content.Context;

public class fetchWardPhyRangeResponse extends BaseResponse {

	public fetchWardPhyRangeBean bean;

	public fetchWardPhyRangeResponse(Context context) {
		super(context);
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		JSONObject main = jobj.getJSONObject("ward_phy_range");
		if (main.has("ward_id")) {
			ward_id = main.getInt("ward_id");
		}
		bean = new fetchWardPhyRangeBean();
		if (main.has("plan_step_num")) {
			bean.setPlan_step_num(main.getInt("plan_step_num"));
		}
		if (main.has("blood_oxygen_low")) {
			bean.setBlood_oxygen_low((float) main.getDouble("blood_oxygen_low"));
		}

		if (main.has("blood_oxygen_high")) {
			bean.setBlood_oxygen_height((float) main.getDouble("blood_oxygen_high"));
		}

		if (main.has("sleep_time_long")) {
			bean.setSleep_time_long((float) main.getDouble("sleep_time_long"));
		}

		if (main.has("heart_rate_low")) {
			bean.setHeart_rate_low(main.getInt("heart_rate_low"));
		}
		if (main.has("heart_rate_high")) {
			bean.setHeart_rate_high(main.getInt("heart_rate_high"));
		}
		if (main.has("slump_threshold")) {
			bean.setSlump_threshold(main.getInt("slump_threshold"));
		}
		
		if(main.has("slump_level")){
			bean.setSlump_level(main.getInt("slump_level"));
		}
		
		
		
	}
}
