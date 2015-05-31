package com.iAcon.response;

import org.json.JSONObject;

import com.iAcron.data.AppData;
import com.iAcron.util.LogUtil;
import com.iAcron.util.Util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

public class BaseResponse {
	public int result;
	public String result_msg = "操作成功";
	private Context mContext;

	public int ward_id;
	public int msg_id;

	public BaseResponse(Context context) {
		this.mContext = context;
	}

	public final boolean parse(String response) {
		try {
			if (TextUtils.isEmpty(response)) {
				return false;
			}
			
			
			
			LogUtil.i("HTTP", "response  =\n "+response);
			
			onParse(new JSONObject(response));
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	protected void onParse(JSONObject jobj) throws Exception {
		if (null == jobj)
			return;

		if (!jobj.isNull("result") && jobj.has("result"))
			result = Integer.valueOf(jobj.getString("result"));
		if (!jobj.isNull("reset_result") && jobj.has("reset_result")) {
			result = Integer.valueOf(jobj.getString("reset_result"));
		}
		if (!jobj.isNull("result_msg") && jobj.has("result_msg"))
			result_msg = jobj.getString("result_msg");

		if (!jobj.isNull("token") && jobj.has("token"))
			AppData.token.set(jobj.getString("token"));

		if (!jobj.isNull("user_id") && jobj.has("user_id"))
			AppData.user_id.set(jobj.getString("user_id"));

		if (!jobj.isNull("ward_id") && jobj.has("ward_id")) {
			ward_id = jobj.getInt("ward_id");
		}
		if (!jobj.isNull("msg_id") && jobj.has("msg_id")) {
			msg_id = jobj.getInt("msg_id");
		}

	}
}
