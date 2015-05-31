package com.iAcron.util;

import java.util.Set;

import com.iAcron.data.AppData;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AlisaHandler extends Handler {

	private String TAG = AlisaHandler.class.getSimpleName();
	private Activity con;
	public reqeustRun run;
	private String userId;
	public AlisaHandler(Activity con,String userId) {
		this.con = con;
		this.userId = userId;
		run = new reqeustRun();
		run.run();
	}



	int requestCount;

	class reqeustRun implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			requestCount++;
			if (requestCount >= 10) {
				removeCallbacks(this);
				return;
			}
			System.out.println("jpush  alias"+"   acron"+userId);
			Log.i("jpush", "jpush   acron" +userId);
			
			JPushInterface.setAlias(con, "acron"+userId,
					new TagAliasCallback() {

						@Override
						public void gotResult(int arg0, String arg1,
								Set<String> arg2) {
							// TODO Auto-generated method stub
							LogUtil.i(TAG, arg1+"jpush   setAlias = " + arg0);
							if (arg0 != 0) {
								postDelayed(run,
										requestCount * 5 * 1000);
							} else {
								AppData.alia.set("true");
								LogUtil.i(TAG, arg1+"jpush   setAlias = 绑定成功");
							}

						}
					});
		}

	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
	}

	public void removeHandler() {
		// TODO Auto-generated method stub
		if(run!=null){
			removeCallbacks(run);
			run = null;
		}
		
	}
}
