package com.iAcron.fragment;

import java.util.ArrayList;

import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.queryWardGuardianListResponse;
import com.iAcon.response.bean.queryWardGuardianList;
import com.iAcron.HomeActivity;
import com.iAcron.QinQingNews;
import com.iAcron.R;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LogUtil;
import com.iAcron.util.ThreadUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QinQingFragmentList extends BaseFregment {

	int mCurCheckPosition = 0;
	int mShownCheckPosition = -1;

	QinQingAdapter adapter;

	public ArrayList<queryWardGuardianList> lists;

	private String url = "http://112.74.95.154:8080/iAcron/queryWardGuardianList";
	BindWardBean bean;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		news = (QinQingNews) getActivity();
		return inflater.inflate(R.layout.qinqing_list, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ListView list = (ListView) view.findViewById(R.id.qinqinglistview);
		adapter = new QinQingAdapter();
		list.setAdapter(adapter);
		bean = (BindWardBean) getArguments().getSerializable("obj");
		queryBindWardList();

		super.onViewCreated(view, savedInstanceState);
	}

	public void queryBindWardList() {
		news.startWaitDialog(null, null, true);
		Common rp = new Common();
		rp.setKV("ward_id", "" + bean.getWard_id());
		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();

						queryWardGuardianListResponse q = new queryWardGuardianListResponse(
								getActivity());
						q.parse(data);
						if (q.result == 1) {
							if (null != q.lists && q.lists.size() >= 1) { // 如果有绑定过被被监护人
								lists = q.lists;
								adapter.notifyDataSetChanged();
							} else {

							}
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	QinQingNews news;

	public static QinQingFragmentList newInstance(BindWardBean index,
			Bundle args) {
		// TODO Auto-generated method stub
		QinQingFragmentList f = new QinQingFragmentList();
		// Supply index input as an argument.
		Bundle bundle = new Bundle();
		bundle.putSerializable("obj", index);
		f.setArguments(bundle);
		return f;
	}

	class QinQingAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists == null ? 0 : lists.size();
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder viewho;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.qinqing_item, null);
				viewho = new ViewHolder();
				viewho.jiebang = (Button) arg1
						.findViewById(R.id.qinqing_item_jiebang);
				viewho.shanchu = (Button) arg1
						.findViewById(R.id.qinqing_item_shanchu);

				viewho.shenfen = (TextView) arg1
						.findViewById(R.id.qinqing_item_jianhuren);
				viewho.jianhuren = (TextView) arg1
						.findViewById(R.id.qinqing_item_jianhuren);
				viewho.sos = (ImageView) arg1
						.findViewById(R.id.qinqing_item_sos);
				arg1.setTag(viewho);
			} else {
				viewho = (ViewHolder) arg1.getTag();
			}
			
			queryWardGuardianList b = lists.get(arg0);
			if(b.getGuard_identity().equals("1")){
				viewho.jiebang.setVisibility(View.VISIBLE);
				viewho.shanchu.setVisibility(View.GONE);
			}else{
				viewho.jiebang.setVisibility(View.GONE);
				viewho.shanchu.setVisibility(View.VISIBLE);
			}
			
			viewho.jianhuren.setText(lists.get(arg0).getNick_name());
			viewho.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DFragment dialog = new DFragment("提示", "是否删除与"
							+ bean.getWard_name() + "解绑","确定", "取消");
					
					dialog.setButtonOnClick(new DFragment.ButtonOnClick() {

						@Override
						public void poOnclick() {
							// TODO Auto-generated method stub
							LogUtil.i("Qinqing", "确定删除");
							unbindWard(lists.get(arg0),false);
						}

						@Override
						public void neOnclick() {
							// TODO Auto-generated method stub
							LogUtil.i("Qinqing", "取消删除");
						}

					});
					dialog.show(getFragmentManager(), "qinqing");
				}
			});
			viewho.jiebang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DFragment dialog = new DFragment("提示", "是否解绑与"
							+ bean.getWard_name() + "解绑", "确定", "取消");
					dialog.setButtonOnClick(new DFragment.ButtonOnClick() {

						@Override
						public void poOnclick() {
							// TODO Auto-generated method stub
							
							unbindWard(lists.get(arg0),true);
						}

						@Override
						public void neOnclick() {
							// TODO Auto-generated method stub

						}

					});
					dialog.show(getFragmentManager(), "qinqing");
				}
			});

			return arg1;

		}

	}

	public void unbindWard(final queryWardGuardianList b,final boolean isDeleteTab) {
		news.startWaitDialog(null, null, true);
		Common rp = new Common();
		rp.setKV("ward_id", "" + bean.getWard_id());
		rp.setKV("guard_user_id",""+b.getUser_id());
		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();
						
						BaseResponse q = new BaseResponse(
								getActivity());
						q.parse(data);
						if (q.result == 1) {
							if(isDeleteTab){
							   //如果是解绑：删除对应的tab
								ThreadUtil.sendMessage(Constants.addTab);
							}
							lists.remove(b);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(getActivity(), q.result_msg, Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						news.finishWaitDialog();

					}

				});
		client.post("http://112.74.95.154:8080/iAcron/unbindWard", rp.getParams());
	}
	
	
	private final class ViewHolder {
		TextView jianhuren;
		TextView shenfen;
		ImageView sos;
		Button jiebang;
		Button shanchu;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("curChoice", mCurCheckPosition);
		outState.putInt("shownChoice", mShownCheckPosition);
	}

}