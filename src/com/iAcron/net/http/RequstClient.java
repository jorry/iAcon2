package com.iAcron.net.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.params.HttpParams;

import com.iAcron.util.LogUtil;
import com.iAcron.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Context;
import android.util.Log;

public class RequstClient {

	private static AsyncHttpClient mClient = new AsyncHttpClient();

	static {
		mClient.setTimeout(20000);
	}
	private String url;
	private Context mContex;
	HttpCallBack listener;

	public RequstClient(Context context, HttpCallBack listener) {
		this.mContex = context;
		this.listener = listener;
		resonse = new HttpResonseHandler(mContex, listener);
	}

	public String getUri() {
		return url;
	}

	HttpResonseHandler resonse;

	/**
	 * 网络不可用，0 请求失败1 返回状态吗
	 */

	public void post(String url, RequestParams params) {
		if (!Util.hasConnectedNetwork(mContex)) {
			if (listener != null) {
				listener.onFailure(1000, new Exception("没有网络"));
			}
			return;
		}
		LogUtil.i("HTTP", url+"");
		LogUtil.i("HTTP", "params = "+params.toString());
		
		mClient.post(url, params, resonse);
	}

	

	public void get(String url) {
		if (!Util.hasConnectedNetwork(mContex)) {
			if (listener != null) {
				listener.onFailure(1000, new Exception("没有网络"));
			}
			return;
		}
		this.url = url;
		mClient.get(mContex, url, resonse);
	}

	public void cancle(String url) {
		if (resonse != null) {
			resonse.onCancel();
		}
		mClient.delete(url, new ResponseHandlerInterface() {

			@Override
			public void sendResponseMessage(HttpResponse response)
					throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendStartMessage() {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendFinishMessage() {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendProgressMessage(int bytesWritten, int bytesTotal) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendCancelMessage() {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendSuccessMessage(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendFailureMessage(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void sendRetryMessage(int retryNo) {
				// TODO Auto-generated method stub

			}

			@Override
			public URI getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Header[] getRequestHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setRequestURI(URI requestURI) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setRequestHeaders(Header[] requestHeaders) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setUseSynchronousMode(boolean useSynchronousMode) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean getUseSynchronousMode() {
				// TODO Auto-generated method stub
				return false;
			}

		});
		mClient.cancelRequests(mContex, true);

	}

}
