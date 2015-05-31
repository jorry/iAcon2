package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.bean.HistoryBean;

import android.content.Context;

public class queryBindWardList extends BaseResponse {

	public ArrayList<BindWardBean> lists = new ArrayList<BindWardBean>();

	public queryBindWardList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		if (jobj.has("result_content")) {
			lists = (ArrayList<BindWardBean>) JSON.parseArray(
					jobj.getString("result_content").toString(),
					BindWardBean.class);
		}
	}

}
