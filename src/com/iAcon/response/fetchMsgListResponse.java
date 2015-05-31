package com.iAcon.response;

import java.util.ArrayList;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.database.model.jianhu;
import com.iAcon.response.bean.fetchMsgListBean;

import android.content.Context;

/**
 * 3.1. 消息提取接口
 * 
 * @author jorry
 */
public class fetchMsgListResponse extends BaseResponse {

	public fetchMsgListResponse(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<fetchmsg> lists;

	@Override
	protected void onParse(JSONObject jobj) throws Exception {
		// TODO Auto-generated method stub
		super.onParse(jobj);
		if (jobj.has("msg_contents")) {
			org.json.JSONArray s = jobj.getJSONArray("msg_contents");
			lists = (ArrayList<fetchmsg>) JSON.parseArray(s.toString(),
					fetchmsg.class);
			for (fetchmsg msg:lists) {
				msg.setMsg_status(1);
			}
		}

	}

}
