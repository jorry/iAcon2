package com.iAcron.util;

import com.iAcron.data.AppData;
import com.loopj.android.http.RequestParams;

public class Common {

	public RequestParams pr;

	public Common() {
		pr = new RequestParams();
		pr.put("user_id", AppData.user_id.get());
		pr.put("token", AppData.token.get());
	}

	public void setKV(String key, String value) {
		pr.put(key, value);
	}

	public RequestParams getParams() {
		return pr;
	}
}
