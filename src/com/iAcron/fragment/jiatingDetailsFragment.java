package com.iAcron.fragment;

import java.util.ArrayList;

import com.iAcon.response.getUserInfoResponse;
import com.iAcon.response.bean.AllContactBean;
import com.iAcron.HomeActivity;
import com.iAcron.JiaTingMangerActivity;
import com.iAcron.R;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.LayoutBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * 添加联系人详情或者修改页面
 * 
 * @author jorry_liu
 * 
 */
public class jiatingDetailsFragment extends BaseFregment {

	public static jiatingDetailsFragment newInstance(int index,Bundle args) {
		// TODO Auto-generated method stub
		jiatingDetailsFragment f = new jiatingDetailsFragment();
		// Supply index input as an argument.
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.jiatingdetail, null);
	}

	EditText contact_name;
	EditText contact_phone;
	EditText contact_sex;
	EditText contact_brith;
	EditText contact_bgs_tel;
	EditText contact_jt_tel;
	EditText contact_qq;
	EditText contact_email;
	EditText contact_address;
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		contact_name = (EditText)view.findViewById(R.id.jiating_name);
		contact_phone = (EditText)view.findViewById(R.id.jiating_phone);
		contact_sex = (EditText)view.findViewById(R.id.jiating_sex);
		contact_brith = (EditText)view.findViewById(R.id.jiating_brith);
		contact_jt_tel = (EditText)view.findViewById(R.id.jiating_tel);
		contact_qq = (EditText)view.findViewById(R.id.jiating_qq);
		contact_email = (EditText)view.findViewById(R.id.jiating_email);
		contact_address = (EditText)view.findViewById(R.id.jiating_address);
		
		Bundle b = getArguments();
		if(b!=null){
			AllContactBean bean = (AllContactBean) b.getSerializable("obj");
			contact_name.setText(bean.getNick_name());
			getUserInfo(bean);
		}
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	private void getUserInfo(AllContactBean bean) {
		
		((JiaTingMangerActivity)activity).startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/getUserInfo";
		Common rp = new Common();
		rp.setKV("guard_id", ""+bean.getGuard_id());
		RequstClient client = new RequstClient(activity, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				((JiaTingMangerActivity)activity).finishWaitDialog();
				getUserInfoResponse q = new getUserInfoResponse(
						activity);
				q.parse(data);
				if(q.result==1&&q.user!=null){
					contact_name.setText(q.user.getNick_name());
					contact_phone.setText(q.user.getMob_tel_no());
					contact_brith.setText(q.user.getBirthday());
					contact_jt_tel.setText(q.user.getHome_tel());
					contact_qq.setText(q.user.getQq_no());
					contact_email.setText(q.user.getEmail());
					contact_address.setText(q.user.getAddr());
				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				((JiaTingMangerActivity)activity).finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				((JiaTingMangerActivity)activity).finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}
	
}
