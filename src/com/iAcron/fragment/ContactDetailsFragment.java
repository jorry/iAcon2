package com.iAcron.fragment;

import com.alibaba.fastjson.JSON;
import com.baidu.android.bbalbs.common.a.b;
import com.iAcon.response.fetchWardTelBookResponse;
import com.iAcon.response.bean.fetchWardTelBookBean;
import com.iAcron.ContactActivity;
import com.iAcron.HomeActivity;
import com.iAcron.R;
import com.iAcron.iAconApplication;
import com.iAcron.adapter.SpinnerAdapter;
import com.iAcron.data.AppData;
import com.iAcron.data.UpdateContact;
import com.iAcron.fragment.DFragment.ButtonOnClick;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.view.MyToast;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

/**
 * 添加联系人详情或者修改页面
 * 
 * @author jorry_liu
 * 
 */
public class ContactDetailsFragment extends BaseFregment {

	public static ContactDetailsFragment newInstance(int index, Bundle args) {
		// TODO Auto-generated method stub
		ContactDetailsFragment f = new ContactDetailsFragment();
		// Supply index input as an argument.
		f.setArguments(args);
		return f;
	}

	ContactActivity ac;

	@Override
	public void onAttach(Activity activity) {
		this.ac = (ContactActivity) activity;
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
		return inflater.inflate(R.layout.contact_details, null);
	}

	EditText contact_name;
	EditText contact_phone;
	RadioGroup contact_sex;
	EditText contact_brith;
	EditText contact_bgs_tel;
	EditText contact_jt_tel;
	EditText contact_qq;
	EditText contact_email;
	Spinner contact_sos;
	EditText contact_guanxi;
	int sex;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contact_name = (EditText) view.findViewById(R.id.contact_name);
		contact_phone = (EditText) view.findViewById(R.id.contact_phone);
		contact_sex = (RadioGroup) view.findViewById(R.id.contact_groupsex);
		contact_email = (EditText) view.findViewById(R.id.contact_email);
		contact_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (R.id.nan == arg1) {
					sex = 1;
				} else if (R.id.nv == arg1) {
					sex = 2;
				}
			}
		});
		contact_brith = (EditText) view.findViewById(R.id.contact_brith);
		contact_bgs_tel = (EditText) view.findViewById(R.id.contact_bgs_tel);
		contact_brith.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					DatePickerDialog date = new DatePickerDialog(getActivity(),
							new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker arg0,
										int arg1, int arg2, int arg3) {
									// TODO Auto-generated method stub
									arg2 += 1;
									contact_brith.setText(arg1 + "-" + arg2
											+ "-" + arg3);
								}
							}, 2000, 1, 1);
					date.show();
					return true;
				}
				return false;
			}
		});
		contact_jt_tel = (EditText) view.findViewById(R.id.contact_jt_tel);
		contact_qq = (EditText) view.findViewById(R.id.contact_qq);
		contact_sos = (Spinner) view.findViewById(R.id.sosspinner);

		// 建立数据源
		String[] mItems = { "sos警报组", "普通警报组" };
		// 建立Adapter并且绑定数据源

		contact_sos.setAdapter(new SpinnerAdapter(mItems, ac));
		contact_guanxi = (EditText) view.findViewById(R.id.contact_guanxi);
		view.findViewById(R.id.contact_add_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						wardTelBookAdd();
					}
				});
		Bundle bu = getArguments();
		if (bu != null) { // 表示修改
			bean = (fetchWardTelBookBean) bu.getSerializable("obj");
			if (bean != null) {

				contact_name.setText(bean.getName());
				contact_phone.setText(bean.getTel_no());

				int child = bean.getSex() == 1 ? 1 : 2;
				contact_sex.getChildAt(child - 1);
				contact_email.setText(bean.getEmail());
				contact_brith.setText(bean.getBirthday());
				contact_bgs_tel.setText(bean.getOffice_tel());
				contact_jt_tel.setText(bean.getHome_tel());
				contact_sos.setSelection(bean.getIs_sos());

				if (bean.getIs_guard() == 1) {
					LinearLayout main = (LinearLayout) view
							.findViewById(R.id.mainlayout);
					for (int i = 0; i < main.getChildCount(); i++) {
						View v = main.getChildAt(i);
						v.setEnabled(false);
					}
				}
			}
		}

		super.onViewCreated(view, savedInstanceState);
	}

	public void setEnabled(View view) {
		view.setEnabled(false);
	}

	public void wardTelBookAdd() {
		if (app.bindWardLists.size() <= 0) {
			MyToast.showToast(ac, "添加被监护人信息还没有被处理，请联系系统管理员");
			return;
		}
		if (TextUtils.isEmpty(contact_name.getText().toString())) {
			MyToast.showToast(ac, "姓名必须填写");
			return;
		}
		if (contact_name.getText().toString().length() > 5) {
			MyToast.showToast(ac, "姓名长度不能大于5位");
			return;
		}

		Common rp = new Common();
		UpdateContact update = new UpdateContact();

		String URL = "http://112.74.95.154:8080/iAcron/";
		if (bean != null) {
			update.setTel_id(bean.getTel_id());
			URL += "wardTelBookAlter";
		} else {
			URL += "wardTelBookAdd";
		}
		iAconApplication app = ((ContactActivity) ac).app;

		update.setAddr("");
		update.setBirthday(contact_brith.getText().toString());
		update.setEmail(contact_email.getText().toString());
		update.setHome_tel(contact_jt_tel.getText().toString());
		update.setName(contact_name.getText().toString());
		update.setOffice_tel(contact_bgs_tel.getText().toString());
		update.setQq_no(contact_qq.getText().toString());
		update.setTel_no(contact_phone.getText().toString());
		int select = contact_sos.getSelectedItemPosition() == 0 ? 2 : 1;
		// update.setGroup(""+select);
		update.setSex(sex);
		rp.setKV("imei", AppData.getImei());
		rp.setKV("ward_id", ""
				+ app.bindWardLists.get(app.bindWardSelect).getWard_id());

		// rp.setKV("telbook_ver", "1");
		String json = JSON.toJSONString(update);
		rp.setKV("telbook", json);

		RequstClient client = new RequstClient(getActivity(),
				new HttpCallBack() {

					@Override
					public void onSuccess(String data) {
						// TODO Auto-generated method stub
						fetchWardTelBookResponse base = new fetchWardTelBookResponse(
								getActivity());
						base.parse(data);
						if (base.result == 1) {
							save();
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

	fetchWardTelBookBean bean;

	public void save() {
		ac.onStart();
		getFragmentManager().popBackStackImmediate();
	}
}