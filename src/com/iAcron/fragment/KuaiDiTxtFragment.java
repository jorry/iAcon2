package com.iAcron.fragment;

import com.alibaba.fastjson.JSONObject;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.kuaiDiList;
import com.iAcon.response.bean.family_express_lis;
import com.iAcron.BaseFragmentActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;
import com.iAcron.view.MyToast;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 如果是自定义消息：保存和发送都请求send接口 如果编辑进入的 ，发送请求发送接口。保存调用update
 * 
 * @author jorry_liu
 * 
 */
public class KuaiDiTxtFragment extends BaseFregment implements OnClickListener {

	EditText edit;

	TextView cont;
	ImageView changeVedio;
	Button send, save;
	family_express_lis bean;
	int tvCount;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.kuaidi_txt_view, null);
		v.findViewById(R.id.menuBtn).setVisibility(View.GONE);

		if (getArguments() != null) {
			bean = (family_express_lis) getArguments().getSerializable("obj");
		}
		
		TextView tv = (TextView)v.findViewById(R.id.topbar_title);
		tv.setText("自定义亲情快递");
		
		Button img = (Button) v.findViewById(R.id.homeBtn);
		img.setVisibility(View.VISIBLE);
		img.setBackgroundResource(R.drawable.u8);
		img.setOnClickListener(this);
		edit = (EditText) v.findViewById(R.id.txt_edit);
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				if(arg0.length()>70){
					MyToast.showToast(activity, "最多可以输入70个文字");
					arg0.delete(5, arg0.length());
				}else{
					setCountText(arg0.length());
				}
			}
		});
		if (bean != null) {
			if(bean.getContent_type()==1){
				edit.setText(bean.getExpress_content());
				edit.setSelection(bean.getExpress_content().length());
			}
		}
		
		cont = (TextView) v.findViewById(R.id.txt_cont_tv);
		
		changeVedio = (ImageView) v.findViewById(R.id.txt_changeto_vedio);
		changeVedio.setOnClickListener(this);
		send = (Button) v.findViewById(R.id.txt_send);
		send.setOnClickListener(this);

		save = (Button) v.findViewById(R.id.txt_save);
		save.setOnClickListener(this);
		setCountText(edit.getText().length());
		return v;
	}
	
	public void setCountText(int size){
		if(cont!=null){
			cont.setText(size+"/70");
		}
	}
/**
 * 更新
 * @param bean
 */
	public void reuqestHttpOfUpdate(family_express_lis bean) {

		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/updateFamilyExpressItem";

		Common rp = new Common();
		iAconApplication app = (iAconApplication) getActivity()
				.getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		try {
			JSONObject jb = new JSONObject();
			jb.put("express_id", bean.getExpress_id());
			jb.put("express_content", bean.getExpress_content());
			jb.put("content_type", bean.getContent_type());
			rp.setKV("family_express", jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						backStackFragment(QinQingKuaiDiListView.class);
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
	 * 增加
	 * @param bean
	 */
	public void reuqestHttpOfAdd(family_express_lis bean) {

		activity.startWaitDialog("提示", "发送消息", false);
		((BaseFragmentActivity) activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/addFamilyExpressItem";

		Common rp = new Common();
		iAconApplication app = (iAconApplication) getActivity()
				.getApplication();
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());
		try {
			JSONObject jb = new JSONObject();
			jb.put("express_content", bean.getExpress_content());
			jb.put("content_type", bean.getContent_type());
			rp.setKV("family_express_content", jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						backStackFragment(QinQingKuaiDiListView.class);
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.txt_changeto_vedio:
			LayoutBuilder lb = new LayoutBuilder(activity);
			lb.replaceRegister(KuaiDiVedioFragment.class, getArguments());
			break;
		case R.id.homeBtn:
			backStackFragment();
			break;
		case R.id.txt_save:
		case R.id.txt_send:
			if(bean ==null){
				bean = new family_express_lis();
			}
			bean.setContent_type(1);
			bean.setExpress_content(edit.getText().toString());
			
			if (getArguments() == null) {
				reuqestHttpOfAdd(bean);
			} else {
				reuqestHttpOfUpdate(bean);
			}
			break;
		default:
			break;
		}
	}
}
