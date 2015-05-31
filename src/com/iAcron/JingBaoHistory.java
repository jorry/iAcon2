package com.iAcron;

import java.util.ArrayList;
import java.util.List;

import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.BaseResponse;
import com.iAcon.response.SOShistroyReponse;
import com.iAcron.data.Constants;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.ThreadUtil;
import com.iAcron.view.MyToast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 警报历史
 * 
 * @author jorry
 */
public class JingBaoHistory extends BaseFragmentActivity {

	ArrayList<Object> lists = new ArrayList<Object>();
	sosAdapter adapter;
	LayoutInflater flater;

	ListView jingbaohistory;

	BindWardBean bean;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);
		bean = (BindWardBean) getIntent().getExtras().getSerializable("obj");

		setContentView(R.layout.sos_history);
		jingbaohistory = (ListView) findViewById(R.id.jingbaohistory);
		adapter = new sosAdapter();
		jingbaohistory.setAdapter(adapter);

		flater = getLayoutInflater();
		requestHistorySOS();
	}

	/**
	 * 接触警报
	 */
	private void requestHistorySOS() {
		startWaitDialog(null, null, false);
		String url = "http://112.74.95.154:8080/iAcron/fetchSOSHistory";
		Common rp = new Common();
		rp.setKV("ward_id", "" + bean.getWard_id());

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				finishWaitDialog();
				SOShistroyReponse br = new SOShistroyReponse(con);
				br.parse(data);
				if (br.result == 1) {
					if (br.lists != null) {
						lists.addAll(br.lists);
						adapter.notifyDataSetChanged();
					}
				} else {
					MyToast.showToast(JingBaoHistory.this, br.result_msg);
				}

			}

			@Override
			public void onAppError(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				// TODO Auto-generated method stub
				finishWaitDialog();
			}

		});
		client.post(url, rp.getParams());
	}

	public class sosAdapter extends BaseAdapter {

		@Override
		public int getCount() {
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
			ViewHolder hoder;
			if (arg1 == null) {
				hoder = new ViewHolder();
				arg1 = flater.inflate(R.layout.sos_history_item, null);
				hoder.info = (TextView) arg1.findViewById(R.id.info);
				hoder.level = (TextView) arg1.findViewById(R.id.level);
				hoder.time = (TextView) arg1.findViewById(R.id.time);
				arg1.setTag(hoder);
			} else {
				hoder = (ViewHolder) arg1.getTag();
			}
			return arg1;
		}

		public final class ViewHolder {
			TextView info;
			TextView time;
			TextView level;

		}
	}

}
