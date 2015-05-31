package com.iAcron.net.http;

import com.iAcron.data.DataBase;

public interface HttpCallBack {

	/**
	 * 当调用服务器接口成功获取数据时,调用这个方法
	 * 
	 * @param data
	 */
	public void onSuccess(String data);

	public void onAppError(int error, Throwable message);

	/**
	 * 当调用服务器接口获取数据失败时,调用这个方法
	 * 
	 * @param error
	 *            出错原因
	 * @param message
	 *            出错原因描述
	 */
	public void onFailure(int error, Throwable message);

}