package com.iAcron;

import com.iAcon.response.BaseResponse;
import com.iAcon.response.queryWardBindResponse;
import com.iAcron.data.AppData;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.ThreadUtil;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 
 * 添加被监护人页面
 * 
 * @author jorry_liu
 * 
 */
public class QinQingAddActivity extends BaseActivity implements OnClickListener {

	private EditText nameEt, wanbiaoEt, shengaoEt, tizhongEt, brithEt, phoneEt,
			historyEt;
	Button add;

	RadioGroup sexRadio;
	RadioButton nan;
	RadioButton nv;
	LinearLayout oneview;
	ScrollView twoview;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qinqing_add);
		context = this;
		initView();
	}

	/**
	 * imei 必须 String 腕表IMEI编号 watch_tel_no String 被监护人腕表所使用的号码 ward_name String
	 * 被监护人姓名 height float 身高，单位厘米 weight float 体重，单位公斤 birthday date 出生日期 sex
	 * String 0：不明确；1: 男, 2: 女 medical_history text 既往病史 token 必须 String
	 * 用于后续交互的唯一session token user_id 必须 Int 用户编号
	 */
	public void queryBindWardList() {
		String url = "http://112.74.95.154:8080/iAcron/bindWard";
		Common rp = new Common();
		rp.setKV("imei", wanbiaoEt.getText().toString());
		rp.setKV("watch_tel_no", phoneEt.getText().toString());
		rp.setKV("ward_name", nameEt.getText().toString());
		rp.setKV("height", shengaoEt.getText().toString());
		rp.setKV("weight", tizhongEt.getText().toString());
		int sex = nan.isChecked() ? 1 : 2;
		rp.setKV("sex", "" + sex);
		rp.setKV("birthday", brithEt.getText().toString());
		rp.setKV("medical_history", historyEt.getText().toString());

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				BaseResponse base = new BaseResponse(getApplication());
				base.parse(data);
				if (base.result == 1) {
					ThreadUtil.sendMessage(Constants.addTab);
					setResult(101);
					finish();
				} else {
					showBaseToast(base.result_msg);
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

	/**
	 * 腕表ID
	 */
	public void queryWardBind() {
		String url = "http://112.74.95.154:8080/iAcron/queryWardBind";
		Common rp = new Common();
		rp.setKV("imei", view1Edit.getText().toString());
		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				queryWardBindResponse base = new queryWardBindResponse(
						getApplication());
				base.parse(data);
				if (base.result == 1) {
					if (base.bind_result == 0) {
						wanbiaoEt.setText(view1Edit.getText().toString());
						wanbiaoEt.setEnabled(false);
						oneview.setVisibility(View.GONE);
						twoview.setVisibility(View.VISIBLE);
					} else {
						queryBindWardList();
//						Toast.makeText(getApplicationContext(),
//								base.ward_name + "已经绑定过此设备号", Toast.LENGTH_LONG)
//								.show();
					}
				} else {
					showBaseToast(base.result_msg);
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

	private EditText view1Edit;
	private Button view1Bt;

	public void initView() {
		view1Edit = (EditText) findViewById(R.id.view1_edit);

		view1Bt = (Button) findViewById(R.id.view1_bt);
		view1Bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(view1Edit.getText().toString())) {
					return;
				}
				queryWardBind();

			}
		});
		oneview = (LinearLayout) findViewById(R.id.oneview);
		twoview = (ScrollView) findViewById(R.id.scrollView1);

		findViewById(R.id.menuBtn).setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
		wanbiaoEt = (EditText) findViewById(R.id.wanbiaoEt);
		shengaoEt = (EditText) findViewById(R.id.shengaoEt);
		tizhongEt = (EditText) findViewById(R.id.tizhongEt);
		brithEt = (EditText) findViewById(R.id.brithEt);
		brithEt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					DatePickerDialog date = new DatePickerDialog(context,
							new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker arg0,
										int arg1, int arg2, int arg3) {
									// TODO Auto-generated method stub
									arg2 += 1;
									brithEt.setText(arg1 + "-" + arg2 + "-"
											+ arg3);
								}
							}, 2000, 1, 1);
					date.show();
					return true;
				}
				return false;
			}
		});
		sexRadio = (RadioGroup) findViewById(R.id.radio_sex);

		phoneEt = (EditText) findViewById(R.id.phoneEt);
		historyEt = (EditText) findViewById(R.id.historyEt);
		findViewById(R.id.addBt).setOnClickListener(this);
		nan = (RadioButton) findViewById(R.id.nan);
		nv = (RadioButton) findViewById(R.id.nv);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.menuBtn:
			finish();
			break;

		default:
			queryBindWardList();
			break;
		}

	}

}
