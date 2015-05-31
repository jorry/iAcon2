package com.iAcon.response;

import org.json.JSONObject;

import android.content.Context;

public class queryWardBindResponse extends BaseResponse{

	public queryWardBindResponse(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public int bind_result;
	public String ward_name;
	
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		if(jobj.has("bind_result")){
			bind_result = jobj.getInt("bind_result");
		}
		if(jobj.has("ward_name")){
			ward_name = jobj.getString("ward_name");
		}
	}

	
}
