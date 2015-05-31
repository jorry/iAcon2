package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.response.bean.HistoryBean;
import com.iAcon.response.bean.queryWardGuardianList;

import android.content.Context;

public class SOShistroyReponse extends BaseResponse{

	
	public SOShistroyReponse(Context context) {
		super(context);
	}
	public ArrayList<HistoryBean> lists;
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(jobj.has("sos_hisory")){
			lists = (ArrayList<HistoryBean>) JSON.parseArray(jobj.getString("sos_hisory").toString(), HistoryBean.class);
		}
	}
}
