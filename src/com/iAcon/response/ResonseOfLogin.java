package com.iAcon.response;

import org.json.JSONObject;

import com.iAcron.data.AppData;

import android.content.Context;

/**
 * 登录
 * @author jorry
 *
 */
public class ResonseOfLogin extends BaseResponse{

	
	public ResonseOfLogin(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		if(jobj.has("user_id")){
			String user_id = jobj.getString("user_id");
			AppData.user_id.set(user_id);
			
		}
	}
}
