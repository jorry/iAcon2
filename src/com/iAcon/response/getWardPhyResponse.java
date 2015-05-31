package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.bean.getWardPhy;
import com.iAcon.response.bean.ward_fence.points;
import com.iAcon.response.bean.ward_phy_range;

import android.content.Context;

public class getWardPhyResponse extends BaseResponse {

	/**
	 * 生理信息
	 */
	public com.iAcon.response.bean.phy_result p;
	/**
	 * 电子围栏
	 */
	public  com.iAcon.response.bean.ward_fence c;

	/**
	 * 运动计划/生理信息
	 */
	public ward_phy_range r;

	public getWardPhyResponse(Context context) {
		super(context);
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);

		if (!jobj.isNull("phy_result") && jobj.has("phy_result")) {
			JSONObject phy = jobj.getJSONObject("phy_result");
			phy_result(phy);
		}
		if (!jobj.isNull("ward_fence") && jobj.has("ward_fence")) {
			JSONObject phy = jobj.getJSONObject("ward_fence");
			ward_fence(phy);
		}
		if (!jobj.isNull("ward_phy_range") && jobj.has("ward_phy_range")) {
			JSONObject phy = jobj.getJSONObject("ward_phy_range");
			ward_phy_range(phy);
		}
	}

	/**
	 * /** "ward_id": 1, "plan_step_num": 998, "sleep_time_long": 15.5,
	 * "heart_rate_low": 72, "heart_rate_high": 110, "blood_oxygen_low": 77.5,
	 * "blood_oxygen_high": 99.5
	 */

	public void ward_phy_range(JSONObject json) {
		try {
			r = new ward_phy_range();
			r.setWard_id(json.getInt("ward_id"));
			r.setPlan_step_num(json.getInt("plan_step_num"));
			
			r.setSleep_time_long(json.getDouble("sleep_time_long"));
			r.setHeart_rate_low(json.getInt("heart_rate_low"));
			r.setHeart_rate_high(json.getInt("heart_rate_high"));
			
			r.setBlood_oxygen_high(json.getDouble("blood_oxygen_high"));
			r.setBlood_oxygen_low(json.getDouble("blood_oxygen_low"));
		} catch (Exception e) {

		}
	}

	/**
	 * "phy_result": { "phy_time": 4567896, "ward_id": 1, "step_num": 0,
	 * "heart_rate": 80, "blood_oxygen": 80, "calorie": 456, "phy_get_type": 1,
	 * "slump_rate": 50, "sleep_time": "5.00", "ward_loc": { "ew": "E", "ns":
	 * "N", "long": "2310.1210", "lat": "11327.4938" }, "watch_take_state": 1,
	 * "healthy_state": 1 }
	 */
	public void phy_result(JSONObject json) {
		try {
			p = new com.iAcon.response.bean.phy_result();
			p.setPhy_time(json.getInt("phy_time"));
			p.setWard_id(json.getInt("ward_id"));
			p.setStep_num(json.getInt("step_num"));
			p.setStep_num(json.getInt("step_num"));
			p.setHeart_rate(json.getInt("heart_rate"));
			p.setBlood_oxygen(json.getInt("blood_oxygen"));
			p.setCalorie(json.getInt("calorie"));
			p.setPhy_get_type(json.getInt("phy_get_type"));
			p.setSlump_rate(json.getInt("slump_rate"));
			p.setSleep_time(json.getInt("sleep_time"));

			JSONObject centerObj = json.getJSONObject("ward_loc");
			p.getMyCenter().ew = centerObj.getString("ew");
			p.getMyCenter().ns = centerObj.getString("ns");
			p.getMyCenter().latS = centerObj.getString("long");
			p.getMyCenter().latS = centerObj.getString("lat");

			p.setPhy_time(json.getInt("watch_take_state"));
			p.setPhy_time(json.getInt("healthy_state"));

		} catch (Exception e) {
		}

	}

	/**
	 * /** "ward_fence": { "ward_id": 1, "set_time": "2014-08-30", "fence_type":
	 * 2, "error_range": 3, "center": { "ew": "E", "ns": "N", "long":
	 * "2310.1210", "lat": "11327.4938" }, "radius": 4, "points": [ { "ew": "E",
	 * "ns": "N", "long": "2310.1210", "lat": "11327.4938" }, { "ew": "E", "ns":
	 * "N", "long": "2310.1210", "lat": "11327.4939" }, { "ew": "E", "ns": "N",
	 * "long": "2310.1210", "lat": "11327.4938" } ] }
	 */
	public void ward_fence(JSONObject json) {
		try {

			c = new com.iAcon.response.bean.ward_fence();
			c.setWard_id(json.getInt("ward_id"));
			c.setSet_time(json.getString("ward_id"));
			c.setFence_type(json.getInt("fence_type"));
			c.setError_range(json.getInt("error_range"));
			if(json.has("center")){
				JSONObject centerObj = json.getJSONObject("center");
				c.getCenterPoint().longS = centerObj.getString("long");
				c.getCenterPoint().latS = centerObj.getString("lat");
			}
			if(json.has("radius")){
				c.setRadius(json.getDouble("radius"));
			}
			if(json.has("points")){
				org.json.JSONArray pointsObj = json.getJSONArray("points");
				for (int i = 0; i < pointsObj.length(); i++) {
					JSONObject o = pointsObj.getJSONObject(i);
					points p = c.getPoint();
					p.ew = o.getString("ew");
					p.ns = o.getString("ns");
					p.longS = o.getString("long");
					p.latS = o.getString("lat");
					c.getPointsList().add(p);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
