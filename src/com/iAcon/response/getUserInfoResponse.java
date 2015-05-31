package com.iAcon.response;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.response.bean.UserInfoBean;

import android.content.Context;

public class getUserInfoResponse extends BaseResponse{

	public getUserInfoResponse(Context context) {
		super(context);
	}
	public UserInfoBean user;
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(jobj.has("user_info")){
			JSONObject j = jobj.getJSONObject("user_info");
			user = JSON.parseObject(j.toString(), UserInfoBean.class);
		}
	}
	
}
