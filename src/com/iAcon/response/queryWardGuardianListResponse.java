package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.iAcon.response.bean.queryWardGuardianList;

import android.content.Context;
import android.text.TextUtils;

public class queryWardGuardianListResponse extends BaseResponse{

	public queryWardGuardianListResponse(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ArrayList<queryWardGuardianList> lists;
	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		try {
			org.json.JSONArray s = jobj.getJSONArray("result_content");
			
			lists = (ArrayList<queryWardGuardianList>) JSON.parseArray(s.toString(), queryWardGuardianList.class);
			System.out.println("亲情列表  "+lists);
			if(null == lists||lists.size()==0){
				lists = new ArrayList<queryWardGuardianList>();
				for (int i = 0; i < 10; i++) {
					lists.add(new queryWardGuardianList(1,"测试",""+2));
				}
			}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			lists = new ArrayList<queryWardGuardianList>();
			for (int i = 0; i < 10; i++) {
				lists.add(new queryWardGuardianList(1,"测试",""+2));
			}
		}
		
	}
	
}
