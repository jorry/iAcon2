package com.iAcon.recever;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.iAcon.database.manager.fetchManager;
import com.iAcon.database.model.fetchmsg;
import com.iAcon.response.fetchMsgListResponse;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;

public class jPushService extends IntentService {

	public jPushService() {
		super("com.iAcon.recever.jPushService");
	}

	private String json;

	private String jsonString;

	private int min_unread_msg_id;
	private String type;

	public void fetChMsg() {
		final fetchManager massager = new fetchManager(getApplicationContext());
		Log.i("service", "请求数据");
		try {
			JSONObject json = new JSONObject(jsonString);
			if (json.has("notify_type")) {
				type = json.getString("notify_type");
			}
			if (json.has("min_unread_msg_id")) {
				min_unread_msg_id = json.getInt("min_unread_msg_id");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(type)) {
			String url = "http://112.74.95.154:8080/iAcron/fetchMsgList";
			Common rp = new Common();

			rp.setKV("last_msg_id", "" + min_unread_msg_id);

			RequstClient client = new RequstClient(this, new HttpCallBack() {

				@Override
				public void onSuccess(String data) {
					fetchMsgListResponse fm = new fetchMsgListResponse(
							getApplicationContext());
					fm.parse(data);
					if (fm.result == 1 && fm.lists != null) {
						fetchManager dao = new fetchManager(jPushService.this);
						SimpleDateFormat sf = new SimpleDateFormat(
								"yyyy-mm-dd hh:mm:ss");
						int size = fm.lists.size();
						ArrayList<fetchmsg> temp = new ArrayList<fetchmsg>();
						for (int i = 0; i < size; i++) {

							fetchmsg msg = fm.lists.get(i);
							
							String time = msg.getCreate_time();
							try {
								Date d = sf.parse(time);
								boolean is = delete(sf,
										d.getTime());
								Log.i("time", "time" + msg.getCreate_time()
										+ "  是否删除" + is);
								if (is||massager.getMsgType1FromWardId(msg.getMsg_id()).size()>=1) {
								}else{
									temp.add(msg);
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						dao.addDeposits(temp);
						Log.i("存储成功", "------存储成功");
						stopSelf();
					}
				}

				@Override
				public void onAppError(int error, Throwable message) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onFailure(int error, Throwable message) {
					// TODO Auto-generated method stub
				}

			});
			client.post(url, rp.getParams());
		}

	}

	public boolean delete(SimpleDateFormat sf,long msgTime) {
	
		if (time > msgTime) {
			return true;
		}
		return false;

	}
	long time;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		jsonString = intent.getStringExtra("json");
		fetChMsg();
		Date date = new Date();// 当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期

		calendar.add(Calendar.MONTH, -1);// 月份减一
		time = calendar.getTimeInMillis();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

}
