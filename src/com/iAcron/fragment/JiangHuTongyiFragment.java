package com.iAcron.fragment;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.manager.JianhuManager;
import com.iAcon.database.model.jianhu;
import com.iAcon.response.JianHuListResponse;
import com.iAcon.response.fetchAllGuardianGroupResponse;
import com.iAcon.response.bean.fetchAllGuardianGroupBean;
import com.iAcron.JianHuActivity;
import com.iAcron.R;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.view.MyToast;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class JiangHuTongyiFragment extends BaseFregment {

	private String value = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}
	
	private String url = "http://112.74.95.154:8080/iAcron/fetchAllGuardianGroup";

	LayoutInflater inflater;
	private View rootView;// 缓存Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.jianhulistview, null);
			this.inflater = inflater;
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		LogUtil.i("Fragment", "onCreateView");

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.i("Fragment", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public listAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LogUtil.i("Fragment", "onActivityCreated");

		ListView list = (ListView) rootView.findViewById(R.id.jianhulistview);
		adapter = new listAdapter();
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});

		Common rp = new Common();
		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						((JianHuActivity) getActivity()).finishWaitDialog();

						fetchAllGuardianGroupResponse q = new fetchAllGuardianGroupResponse(
								getActivity());
						q.parse(data);
						if (q.result == 1) {
							if (null != q.lists && q.lists.size() >= 1) { // 如果有绑定过被被监护人
								adapter.list.clear();
								for (int i = 0; i <q.lists.size(); i++) {
									fetchAllGuardianGroupBean b = q.lists.get(i);
									if(b.getStatus()==1){
										adapter.setList(b);
									}
								}
								adapter.notifyDataSetChanged();
							}
						}else{
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						((JianHuActivity) getActivity()).finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						((JianHuActivity) getActivity()).finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());

		super.onActivityCreated(savedInstanceState);
	}

	public class listAdapter extends BaseAdapter {
		public ArrayList<fetchAllGuardianGroupBean> list = new ArrayList<fetchAllGuardianGroupBean>();

		public void setList(fetchAllGuardianGroupBean array) {
			list.add(array);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			arg1 = inflater.inflate(R.layout.jianhu_item, null);
			TextView time = (TextView) arg1.findViewById(R.id.time);
			TextView zhu = (TextView) arg1.findViewById(R.id.zhu);
			TextView beijianhuren = (TextView) arg1
					.findViewById(R.id.beijianhuren);
			TextView name = (TextView) arg1.findViewById(R.id.name);
			
			fetchAllGuardianGroupBean bean = list.get(arg0);
			
			time.setText(bean.getCreate_time());
			name.setText(bean.getNick_name());
			beijianhuren.setText(bean.getWard_name());
			zhu.setText(bean.getGroup_name());
			
			return arg1;
		}

	}

}