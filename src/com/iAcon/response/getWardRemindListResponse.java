package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.response.bean.getWardReindBean;

import android.content.Context;

public class getWardRemindListResponse extends BaseResponse{

	public getWardRemindListResponse(Context context) {
		super(context);
	}
	public ArrayList<getWardReindBean> lists;
	

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(result==1){
			if(jobj.has("remind_info")){
				JSONObject o = jobj.getJSONObject("remind_info");
				org.json.JSONArray s = o.getJSONArray("remind_list");
				lists = (ArrayList<getWardReindBean>) JSON.parseArray(s.toString(),
						getWardReindBean.class);
			}
		}
	}
}
