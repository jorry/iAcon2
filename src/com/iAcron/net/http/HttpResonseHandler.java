package com.iAcron.net.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.iAcron.util.LogUtil;
import com.iAcron.util.ResParser;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 网络请求结果监听
 * @author jorry
 *
 */
public class HttpResonseHandler extends AsyncHttpResponseHandler {
	private Context context;
	private HttpCallBack mHandler;

	private boolean cancel;

	@Override
	public void onCancel() {
		cancel = true;
	}

	public HttpResonseHandler(Context context, HttpCallBack mHandler) {
		this.context = context;
		this.mHandler = mHandler;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onFinish() {
		super.onFinish();
	}

	/**
	 * 出错
	 * 
	 * @param error
	 * @param errorMessage
	 */
	public void onFailure(String error, Throwable errorMessage) {
		if (errorMessage != null) {
			mHandler.onFailure(5000, errorMessage);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		// TODO Auto-generated method stub
		if (cancel)
			return;
		String data = new String(responseBody);
		try {
			switch (statusCode) {
			case 200:
				System.out.println();
				LogUtil.i("NET", "得到的返回码 200 = 返回的内容"
						+ data);
				
				mHandler.onSuccess(data);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			mHandler.onAppError(5001, e);
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		// TODO Auto-generated method stub
		mHandler.onFailure(5000, error);
	}
}