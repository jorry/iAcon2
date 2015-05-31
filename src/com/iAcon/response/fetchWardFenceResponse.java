package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.iAcon.response.bean.ward_fence.points;

import android.content.Context;

public class fetchWardFenceResponse extends BaseResponse {

	/**
	 * 电子围栏
	 */
	private   com.iAcon.response.bean.ward_fence c;


	public com.iAcon.response.bean.ward_fence getC() {
		if(c==null){
			c = new com.iAcon.response.bean.ward_fence();
		}
		return c;
	}
	private Context mContext;

	public void setC(com.iAcon.response.bean.ward_fence c) {
		this.c = c;
	}

	public fetchWardFenceResponse(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);

		if (!jobj.isNull("wardFence") && jobj.has("wardFence")) {
			JSONObject phy = jobj.getJSONObject("wardFence");
			ward_fence(phy);
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
					if(o.has("lot")){
						p.longS = o.getString("lot");
					}
					if(o.has("long")){
						p.longS = o.getString("long");
					}
					p.latS = o.getString("lat");
					c.getPointsList().add(p);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
