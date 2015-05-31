package com.iAcron.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONObject;

import com.iAcon.database.manager.JianhuManager;
import com.iAcon.database.model.jianhu;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.kuaiDiList;
import com.iAcon.response.bean.family_express_lis;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.JianHuActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.data.Constants;
import com.iAcron.data.DataBase;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MyToast;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("NewApi")
public class QinQingKuaiDiListView extends BaseFregment {

	ListView flipper;
	QinQingAdapter adapter;
	LayoutInflater inflater;
	List<family_express_lis> list = new ArrayList<family_express_lis>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.qinqing_kuaidi_list, null);
		view.findViewById(R.id.menuBtn).setVisibility(View.GONE);
		TextView tv = (TextView)view.findViewById(R.id.topbar_title);
		tv.setText("亲情快递");
		Button home = (Button) view.findViewById(R.id.homeBtn);
		home.setVisibility(View.VISIBLE);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ThreadUtil.sendMessage(Constants.openMenu);
				activity.finish();
			}
		});
		flipper = (ListView) view.findViewById(R.id.qinqinglistview);
		flipper.setOnTouchListener(onTouchListener);

		flipper.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				index = -1;
				LayoutBuilder lb = new LayoutBuilder(activity);
				Bundle b = new Bundle();
				b.putSerializable("obj", list.get(arg2));
				if(list.get(arg2).getContent_type()==1){
					lb.replaceRegister(KuaiDiTxtFragment.class, b);
				}else{
					lb.replaceRegister(KuaiDiVedioFragment.class, b);
										
				}
			}
		});
		adapter = new QinQingAdapter();
		flipper.setAdapter(adapter);

		view.findViewById(R.id.zdy_bt).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutBuilder lb = new LayoutBuilder(activity);
				lb.replaceRegister(KuaiDiTxtFragment.class, null);
			}
		});
		reuqestHttp();
		return view;
	}

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
					adapter.notifyDataSetChanged();
				}
				return false;
			}

			return false;
		}

	};

	iAconApplication app;

	/**
	 * 
	 * @param state
	 */
	public void reuqestHttp() {
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/fetchFamilyExpressList";

		Common rp = new Common();
		app = (iAconApplication) getActivity().getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

						kuaiDiList q = new kuaiDiList(activity);
						q.parse(data);
						if (q.result == 1) {
							list.clear();
							list.addAll(q.list);
							adapter.notifyDataSetChanged();
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	/**
	 * ward_id 必须 Int 被监护人ID token 必须 String family_express 必须 String JSON格式数据
	 */
	public void reuqestHttpOfSend(family_express_lis bean) {
		index = -1;
		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/sendFamilyExpress";

		Common rp = new Common();
		app = (iAconApplication) getActivity().getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		try {
			JSONObject jb = new JSONObject();
			jb.put("express_id", bean.getExpress_id());
			jb.put("express_content", bean.getExpress_id());
			jb.put("content_type", bean.getExpress_id());
			rp.setKV("family_express", jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		rp.setKV("need_save", "0");

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						activity.finishWaitDialog();
						BaseResponse q = new BaseResponse(activity);
						q.parse(data);
						if (q.result == 1) {
							MyToast.showToast(activity, "发送完成");
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	public void reuqestHttpOfDelete(family_express_lis bean) {
		index = -1;
		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/deleteFamilyExpressItems";

		Common rp = new Common();
		app = (iAconApplication) getActivity().getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		rp.setKV("family_express_ids", "" + bean.getExpress_id());

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						activity.finishWaitDialog();
						BaseResponse q = new BaseResponse(activity);
						q.parse(data);
						if (q.result == 1) {
							reuqestHttp();
							MyToast.showToast(activity, "删除成功");
						} else {
							MyToast.showToast(activity, q.result_msg);
						}
					}

					@Override
					public void onAppError(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

					@Override
					public void onFailure(int error, Throwable message) {
						// TODO Auto-generated method stub
						activity.finishWaitDialog();

					}

				});
		client.post(url, rp.getParams());
	}

	class QinQingAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
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
			final ViewHolder viewho;
			if (arg1 == null) {
				arg1 = inflater.inflate(R.layout.qinqing_kuaidi_item, null);
				viewho = new ViewHolder();

				viewho.shenfen = (TextView) arg1.findViewById(R.id.info);
				viewho.delete = (ImageView) arg1.findViewById(R.id.delete);
				viewho.send = (Button) arg1.findViewById(R.id.send);
				viewho.menu = (LinearLayout) arg1.findViewById(R.id.item_menu);

				arg1.setTag(viewho);
			} else {
				viewho = (ViewHolder) arg1.getTag();
			}

			final family_express_lis bean = list.get(arg0);

			if (bean.getContent_type() == 2) { // 音频
				Drawable nav_up = getResources().getDrawable(R.drawable.u112);
				nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
						nav_up.getMinimumHeight());
				viewho.shenfen.setCompoundDrawables(null, null, nav_up, null);
				viewho.shenfen.setText(">"+list.get(arg0).getContent_desc());
			} else {
				viewho.shenfen.setCompoundDrawables(null, null, null, null);
				viewho.shenfen.setText(">"+list.get(arg0).getExpress_content());
			}
			

			if (arg0 == index) {
				if (viewho.menu.getVisibility() == View.VISIBLE) {
					viewho.menu.setVisibility(View.GONE);
					doAnimation(getActivity(), viewho.menu,
							R.anim.push_right_out);
				} else {
					viewho.menu.setVisibility(View.VISIBLE);
					doAnimation(getActivity(), viewho.menu,
							R.anim.push_right_in);
				}

			} else {
				viewho.menu.setVisibility(View.GONE);
			}
			viewho.send.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					viewho.menu.setVisibility(View.GONE);
					doAnimation(getActivity(), viewho.menu,
							R.anim.push_right_out);
					reuqestHttpOfSend(bean);
				}
			});
			viewho.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					viewho.menu.setVisibility(View.GONE);
					doAnimation(getActivity(), viewho.menu,
							R.anim.push_right_out);
					reuqestHttpOfDelete(bean);
				}
			});
			return arg1;

		}

	}

	protected void doAnimation(Context context, View view, int animId) {
		Animation animation = AnimationUtils.loadAnimation(context, animId);
		view.startAnimation(animation);
	}

	private final class ViewHolder {
		TextView shenfen;
		ImageView delete;
		Button send;
		LinearLayout menu;
	}

	public void onHttpOnSuccess(String data) {
		// TODO Auto-generated method stub

	}

	public void onHttpFailure(int error) {
		// TODO Auto-generated method stub

	}

}
