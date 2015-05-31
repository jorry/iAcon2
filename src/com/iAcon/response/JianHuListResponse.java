package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iAcon.database.model.jianhu;
import com.iAcon.response.bean.queryWardGuardianList;

import android.content.Context;
import android.text.TextUtils;

public class JianHuListResponse extends BaseResponse {

	public JianHuListResponse(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<jianhu> lists;

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		try {
			org.json.JSONArray s = jobj.getJSONArray("result_content");

			lists = (ArrayList<jianhu>) JSON.parseArray(s.toString(),
					jianhu.class);
			System.out.println("亲情列表  " + lists);
			if (null == lists || lists.size() == 0) {
				lists = new ArrayList<jianhu>();
				for (int i = 0; i < 10; i++) {
					jianhu j = new jianhu();
					j.setNick_name("测试");
					j.setStatus("1");
					j.setUser_id(1);
					lists.add(j);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			lists = new ArrayList<jianhu>();

			for (int i = 0; i < 10; i++) {
				jianhu j = new jianhu();
				j.setNick_name("测试");
				j.setStatus("1");
				j.setUser_id(1);
				lists.add(j);
			}
		}

	}

}
