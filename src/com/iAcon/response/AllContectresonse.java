package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.response.bean.AllContactBean;
import com.iAcon.response.bean.fetchMsgListBean;

import android.content.Context;

public class AllContectresonse extends BaseResponse{

	public AllContectresonse(Context context) {
		super(context);
	}	
	
	public ArrayList<AllContactBean> lists = new ArrayList<AllContactBean>();
	
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(jobj.has("result_content")){
			org.json.JSONArray s = jobj.getJSONArray("result_content");
			lists = (ArrayList<AllContactBean>) JSON.parseArray(s.toString(),
					AllContactBean.class);
		}
	}
}
