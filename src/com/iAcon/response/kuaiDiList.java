package com.iAcon.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.response.bean.family_express_lis;

import android.content.Context;

public class kuaiDiList extends BaseResponse {

	public kuaiDiList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public List<family_express_lis> list = new ArrayList<family_express_lis>();

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		super.onParse(jobj);
		if (this.result == 1) {
			if (jobj.has("family_express_list")) {
				org.json.JSONArray js = jobj.getJSONArray("family_express_list");
				
				list = (List<family_express_lis>) JSON.parseArray(
						js.toString(),
						family_express_lis.class);
			}
		}
	}

}
