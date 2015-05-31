package com.iAcron.fragment;

import java.util.ArrayList;

import u.aly.ar;

import com.iAcon.response.fetchAllGuardianGroupResponse;
import com.iAcon.response.getWardRemindListResponse;
import com.iAcon.response.bean.fetchAllGuardianGroupBean;
import com.iAcon.response.bean.getWardReindBean;
import com.iAcron.AlertActivity;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.JianHuActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.LogUtil;
import com.iAcron.view.MyToast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlertListActicty extends BaseFregment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	ListView alertList;
	LayoutInflater flater;
	alertItemAdatper itemAdapter;

	ArrayList<getWardReindBean> lists = new ArrayList<getWardReindBean>();

	iAconApplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.activity_alert, null);
		app = (iAconApplication) activity.getApplication();
		mainView.findViewById(R.id.button_add).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				LayoutBuilder lb = new LayoutBuilder(activity);
				lb.replaceRegister(AlertAddDetails.class, null);
			}
		});
		mainView.findViewById(R.id.homeBtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						activity.finish();
					}
				});
		flater = inflater;
		itemAdapter = new alertItemAdatper();
		alertList = (ListView) mainView.findViewById(R.id.alert_listview);
		alertList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LayoutBuilder lb = new LayoutBuilder(activity);
				Bundle bundle = new Bundle();
				getWardReindBean bean = lists.get(arg2);
				bundle.putSerializable("obj", lists.get(arg2));
				lb.replaceRegister(AlertDetails.class, bundle);
			}
		});
		alertList.setAdapter(itemAdapter);

		request();
		return mainView;
	}

	public void reuqestQ(final getWardReindBean bean, int remind_id, final int remind_enabled) {
		((BaseFragmentActivity)activity).startWaitDialog("", "", false);
		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("remind_id", "" + remind_id);
		rp.setKV("remind_enabled", "" + remind_enabled);

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						((BaseFragmentActivity)activity).finishWaitDialog();
						getWardRemindListResponse q = new getWardRemindListResponse(
								activity);
						q.parse(data);
						if (q.result == 1) {
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						((BaseFragmentActivity)activity).finishWaitDialog();
					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						((BaseFragmentActivity)activity).finishWaitDialog();

					}

				});
		client.post("http://112.74.95.154:8080/iAcron/updateWardRemindEnabled",
				rp.getParams());

	}

	public void request() {
		Common rp = new Common();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {

						getWardRemindListResponse q = new getWardRemindListResponse(
								activity);
						q.parse(data);
						if (q.result == 1) {
							lists.clear();
							if (null != q.lists && q.lists.size() >= 1) { // 如果有绑定过被被监护人
								lists.addAll(q.lists);
								itemAdapter.notifyDataSetChanged();
							}
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						((AlertActivity) getActivity()).finishWaitDialog();

					}

				});
		client.post("http://112.74.95.154:8080/iAcron/getWardRemindList",
				rp.getParams());
	}

	/*
	 * 8 URL: 返回结果如下： 参数名称 是否必须 类型 描述 result 是 Int 1:成功；0：失败 result_msg 否 String
	 * 描述失败原因 remind_info 是 String JSON格式数据
	 * "remind_info":{"ward_id":6,"remind_list"
	 * :[{"remind_id":5,"loop_type":1,"loop_value"
	 * :"2014-09-03","remind_time":"11:30:00"
	 * ,"remind_type":"吃饭","content_type":1
	 * ,"remind_content":"该吃饭了","enabled":1},
	 * {"remind_id":7,"loop_type":1,"loop_value"
	 * :"2014-09-03","remind_time":"11:30:00"
	 * ,"remind_type":"吃饭","content_type":2
	 * ,"remind_content":"7a7a6a","enabled":1}]} JSON数据字段说明 字段名称 字段类型 字段含义
	 * remind_id Int 用户ID loop_type Int 提醒循环类型。 1:一次性提醒，2：每周周期性提醒，3：每月周期性提醒
	 * loop_value String loop_type=1，此处为yyyy-MM-dd，表示在这一天的remind_time时间进行提醒。
	 * loop_type=2，此处为周几，可以多个数值，逗号分隔。每个值的范围为1～7，分别代表周一到周日。
	 * loop_type=3，类同每周，其值范围为从1～31。 remind_time String 提醒时间。 remind_type String
	 * 提醒类型，可选值包括：运动，睡觉，吃饭，喝水，吃药，其他。 content_type Int 提醒内容类型。1：文本，2：语音
	 * remind_content String 提醒内容 enabled Int 该提醒是否为启用状态。1：启用，2：不启用
	 */

	class alertItemAdatper extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			ViewHodler v = null;
			arg1 = flater.inflate(R.layout.activity_alert_item, null);
			v = new ViewHodler();
			v.swit = (ToggleButton) arg1.findViewById(R.id.alert_item_switch);
			v.time = (TextView) arg1.findViewById(R.id.alert_item_time);
			v.info = (TextView) arg1.findViewById(R.id.alert_item_title);
			v.icon = (ImageView) arg1.findViewById(R.id.alert_item_icon);

			final getWardReindBean bean = lists.get(arg0);
			v.swit.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton bt, boolean arg1) {
					LogUtil.i("alert", bean.remind_content + "开关" + arg1);
					int eboolean = arg1?1:0;
					if (bean.enabled != eboolean){
						bean.enabled = eboolean;
						reuqestQ(bean,bean.remind_id,arg1?1:2);
					}
				}
			});

			int type = bean.loop_type;
			if (type == 2) {
				if (bean.enabled == 1) { // 启用
					v.swit.setChecked(true);
				} else { // 关闭
					v.swit.setChecked(false);
				}

				if ("运动".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_yd);

				} else if ("睡觉".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_sleep);
				} else if ("吃饭".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_cf);
				} else if ("喝水".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_shui);
				} else if ("吃药".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_yao);
				} else if ("其他".equals(bean.remind_type)) {
					v.icon.setImageResource(R.drawable.a_type_other);
				} else {

				}

				v.info.setText(bean.remind_content);

				v.time.setText(bean.loop_value);

			} else {
				v.info.setText("类型错误" + type);
			}

			return arg1;
		}

		final class ViewHodler {
			ImageView icon;
			ToggleButton swit;
			TextView time;
			TextView info;
		}
	}
}
