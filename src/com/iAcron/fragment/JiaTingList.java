package com.iAcron.fragment;

import java.util.ArrayList;

import com.iAcon.response.bean.AllContactBean;
import com.iAcron.HomeActivity;
import com.iAcron.JiaTingMangerActivity;
import com.iAcron.R;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class JiaTingList extends BaseFregment implements OnItemClickListener {

	Activity ac;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public ArrayList<AllContactBean> lists = new ArrayList<AllContactBean>();
	LayoutInflater inflater;
	QinQingAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.jiating_list,null);
		
		ListView list = (ListView) view.findViewById(R.id.jiating_list);
		list.setOnItemClickListener(this);
		 adapter = new QinQingAdapter();
		list.setAdapter(adapter);
		requestAllContect();
		return view;
	}
	
	private void requestAllContect() {
		// TODO Auto-generated method stub
		
		((JiaTingMangerActivity)ac).startWaitDialog(null, null, false);
//		.startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/fetchFamilyMemberList";
		Common rp = new Common();
		RequstClient client = new RequstClient(ac, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				com.iAcon.response.AllContectresonse q = new com.iAcon.response.AllContectresonse(
						ac);
				q.parse(data);
				if(lists == null){
					lists  = new ArrayList<AllContactBean>();
				}
				if (q.result == 1) {
					lists.clear();
					lists.addAll(q.lists);
				}
				adapter.notifyDataSetChanged();
				((JiaTingMangerActivity)ac).finishWaitDialog();
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				((JiaTingMangerActivity)ac).finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				((JiaTingMangerActivity)ac).finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}

	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.ac = activity;
		super.onAttach(activity);
	}

	class QinQingAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			arg1 = inflater.inflate(R.layout.jiatingmanager_item, null);
			TextView t = (TextView)arg1.findViewById(R.id.jiatingmanager_item_name);
			t.setText(lists.get(arg0).getNick_name());
			return arg1;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putSerializable("obj", lists.get(arg2));
		
		LayoutBuilder lb = new LayoutBuilder(activity);
		lb.replaceRegister(jiatingDetailsFragment.class, bundle);
	}

}
