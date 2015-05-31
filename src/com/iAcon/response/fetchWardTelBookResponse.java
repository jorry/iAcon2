package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iAcon.response.bean.fetchWardTelBookBean;

import android.content.Context;

public class fetchWardTelBookResponse extends BaseResponse {

	public fetchWardTelBookResponse(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	String telbook_ver;
	public ArrayList<fetchWardTelBookBean> beanS;

	@Override
	protected void onParse(JSONObject j) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(j);
		if (j.has("telbook_ver")) {
			telbook_ver = j.getString("telbook_ver");
		}
		if (j.has("telbook")) {
			telbook_ver = j.getString("telbook");
			org.json.JSONArray json = j.getJSONArray("telbook");
			beanS = (ArrayList<fetchWardTelBookBean>) JSON.parseArray(
					json.toString(), fetchWardTelBookBean.class);
			
		}
	}

}
