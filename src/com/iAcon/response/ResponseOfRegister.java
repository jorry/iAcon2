package com.iAcon.response;

import org.json.JSONObject;

import com.iAcron.data.AppData;

import android.content.Context;

/**
 * 注册
 * 
 * @author jorry
 * 
 */
public class ResponseOfRegister extends BaseResponse {

	public ResponseOfRegister(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if (result == 1) {
		}
	}
}
