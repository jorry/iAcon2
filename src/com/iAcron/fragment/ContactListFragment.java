package com.iAcron.fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.fetchWardTelBookResponse;
import com.iAcon.response.bean.fetchWardTelBookBean;
import com.iAcon.response.bean.ward_fence.points;
import com.iAcron.HomeActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.data.AppData;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.LogUtil;
import com.iAcron.view.MyToast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ContactListFragment extends BaseFregment implements
		OnItemClickListener {

	/**
	 * http://112.74.95.154:8080/iAcron/fetchWardTelBook
	 */

	private String URL = "http://112.74.95.154:8080/iAcron/fetchWardTelBook";
	private String deleteUrl = "http://112.74.95.154:8080/iAcron/wardTelBookDel";
	private String TAG = ContactListFragment.class.getSimpleName();

	listAdapter adapter;
	iAconApplication app;
	public ArrayList<fetchWardTelBookBean> lists = new ArrayList<fetchWardTelBookBean>();

	
	private Handler.Callback msgCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.UPDATE:
				fetchWardTelBook();
				break;
			default:
				break;
			}

			return false;
		}
	};
	
	
	
	public static ContactListFragment newInstance(int index, Bundle args) {
		// TODO Auto-generated method stub
		ContactListFragment f = new ContactListFragment();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.i(TAG, "onCreate");
		if (savedInstanceState != null) {
			int sa = savedInstanceState.getInt("1");
		}
		
		app = (iAconApplication) getActivity().getApplication();
		super.onCreate(savedInstanceState);
	}

	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;

		return inflater.inflate(R.layout.contact_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		regMsg(Constants.UPDATE, msgCallback);
		adapter = new listAdapter();
		list.setAdapter(adapter); // 使用静态数组填充列表
		if (savedInstanceState != null) {
		}

		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(this);

	}

	ListView list;
	private int index = -1;
	View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		float x, y, ux, uy;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				y = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				ux = event.getX();
				uy = event.getY();
				index = ((ListView) v).pointToPosition((int) x, (int) y);// item的position
				int p2 = ((ListView) v).pointToPosition((int) ux, (int) uy);
				
				if (index == p2 && Math.abs(x - ux) > 20) {
					fetchWardTelBookBean bean = 	(fetchWardTelBookBean) adapter.getItem(p2);
					if(bean.getIs_guard()==0){
						MyToast.showToast(activity, "该联系人不能删除");
					}else{
						adapter.notifyDataSetChanged();
					}
				}
				return false;
			}

			return false;
		}

	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		list = (ListView) view.findViewById(R.id.contact_list);
		list.setOnTouchListener(onTouchListener);
		list.setOnItemClickListener(this);
		view.findViewById(R.id.contact_list_add).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						LayoutBuilder lb = new LayoutBuilder(activity);
						Bundle bundle = new Bundle();
						lb.replaceRegister(ContactDetailsFragment.class, bundle);
					}
				});

		fetchWardTelBook();
		super.onViewCreated(view, savedInstanceState);

	}

	public void fetchWardTelBook() {
		if(null==app.bindWardLists||app.bindWardLists.size()==0){
			return;
		}
		Common rp = new Common();

		rp.setKV("imei", AppData.getImei());
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("telbook_ver", "1");

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						fetchWardTelBookResponse base = new fetchWardTelBookResponse(
								getActivity());
						base.parse(data);
						if (base.result == 1) {
							lists.clear();
							adapter.setList(base.beanS);
							adapter.notifyDataSetChanged();
						} else {
							MyToast.showToast(getActivity(), base.result_msg);
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
		client.post(URL, rp.getParams());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		index = -1;
		showDetails(arg2);
	}

	class listAdapter extends BaseAdapter {

		public void setList(ArrayList<fetchWardTelBookBean> array) {
			lists.addAll(array);
			System.out.println("setList" + lists.size());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists == null || lists.size() == 0 ? 0 : lists.size();
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contact_list_item,
						null, false);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.contact_item_name);
				viewHolder.sos = (TextView) convertView
						.findViewById(R.id.contact_item_sos);
				viewHolder.delete = (Button) convertView
						.findViewById(R.id.delete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final fetchWardTelBookBean bean = lists.get(arg0);
			viewHolder.name.setText(bean.getName());
			if(bean.getIs_sos()==1){
				viewHolder.sos.setVisibility(View.VISIBLE);
			}
			if (arg0 == index) {
				if (viewHolder.delete.getVisibility() == View.VISIBLE) {
					viewHolder.delete.setVisibility(View.GONE);
					doAnimation(getActivity(), viewHolder.delete,
							R.anim.push_right_out);
				} else {
					viewHolder.delete.setVisibility(View.VISIBLE);
					doAnimation(getActivity(), viewHolder.delete,
							R.anim.push_right_in);
				}

				viewHolder.delete
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								wardTelBookDel(bean);
							}
						});

			} else {
				viewHolder.delete.setVisibility(View.GONE);
			}

			return convertView;
		}

		protected void doAnimation(Context context, View view, int animId) {
			Animation animation = AnimationUtils.loadAnimation(context, animId);
			view.startAnimation(animation);
		}

		final class ViewHolder {
			TextView name;
			TextView sos;
			Button delete;
		}

	}
	/**
	 * 删除电话薄
	 * @param telBean
	 */
	public void wardTelBookDel(final fetchWardTelBookBean telBean) {
		Common rp = new Common();
		rp.setKV("imei", AppData.getImei());
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("tel_id", ""+telBean.getTel_id());

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						fetchWardTelBookResponse base = new fetchWardTelBookResponse(
								getActivity());
						base.parse(data);
						if (base.result == 1) {
							lists.remove(telBean);
							adapter.notifyDataSetChanged();
						} else {
							MyToast.showToast(getActivity(), base.result_msg);
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
		client.post(deleteUrl, rp.getParams());
	}

	void showDetails(int index) {
		list.setItemChecked(index, true);

		Bundle bundle = new Bundle();
		bundle.putSerializable("obj", lists.get(index));
		
		LayoutBuilder lb = new LayoutBuilder(activity);
		lb.replaceRegister(ContactDetailsFragment.class, bundle);
		
	}

}
