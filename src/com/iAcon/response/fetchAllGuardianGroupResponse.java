package com.iAcon.response;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.model.jianhu;
import com.iAcon.response.bean.fetchAllGuardianGroupBean;

import android.content.Context;

public class fetchAllGuardianGroupResponse extends BaseResponse{

	public ArrayList<fetchAllGuardianGroupBean> lists;
	public fetchAllGuardianGroupResponse(Context context) {
		super(context);
	}

	protected void onParse(org.json.JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if(jobj.has("result_content")){
			lists = (ArrayList<fetchAllGuardianGroupBean>) JSON.parseArray(jobj.getString("result_content"),
					fetchAllGuardianGroupBean.class);
			
		}
	};
}
