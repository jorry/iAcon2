package com.iAcron;

import java.util.ArrayList;

import roboguice.application.RoboApplication;
import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.iAcon.database.dao.DaoMaster;
import com.iAcon.database.dao.DaoMaster.OpenHelper;
import com.iAcon.database.dao.DaoSession;
import com.iAcon.database.model.BindWardBean;
import com.iAcron.data.AppData;

public class iAconApplication extends RoboApplication {

	public int bindWardSelect;
	public String token;

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	private static iAconApplication mInstance;

	public ArrayList<BindWardBean> bindWardLists = new ArrayList<BindWardBean>();

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		AppData.init(this);
		SDKInitializer.initialize(this);
		JPushInterface.setDebugMode(true); //
		JPushInterface.init(this);

		
	}

	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context, "iAcon",
					null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}