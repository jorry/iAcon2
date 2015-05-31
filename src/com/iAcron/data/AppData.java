package com.iAcron.data;

import java.io.File;

import com.iAcron.util.Getter;
import com.iAcron.util.PreferPropertyBool;
import com.iAcron.util.PreferPropertyInt;
import com.iAcron.util.PreferPropertyString;
import com.iAcron.util.PreferenceUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * @author yangentao 在Application.onCreate中初始化(init())
 */
public class AppData {

	
	private static final String SP_NAME = "iAcon";

	private static PreferenceUtil sp = new PreferenceUtil();

	private static Context context = null;

	private static String imei = "";
	private static String imsi = "";

	private static ConnectivityManager connMgr = null;

	/**
	 * 初始化, 在Application中调用
	 * 
	 * @param c
	 */
	public static void init(Context c) {
		context = c;
		sp.init(c, SP_NAME);

		TelephonyManager telephonyManager = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		imsi = telephonyManager.getSubscriberId();

		connMgr = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * Application的Context
	 * 
	 * @return
	 */
	public static Context getContext() {
		return context;
	}

	public static Getter<Integer> versionCode = new Getter<Integer>(0) {

		@Override
		protected Integer onInit(Integer defValue) {
			try {
				return getContext().getPackageManager().getPackageInfo(
						getContext().getPackageName(), 0).versionCode;
			} catch (Exception e) {
			}
			return defValue;
		}

	};
	public static Getter<String> versionName = new Getter<String>("1.0.0") {

		@Override
		protected String onInit(String defValue) {
			try {
				return getContext().getPackageManager().getPackageInfo(
						getContext().getPackageName(), 0).versionName;
			} catch (Exception e) {
			}
			return defValue;
		}

	};

	/**
	 * 手机设备ID
	 * 
	 * @return
	 */
	public static String getImei() {
		return imei;
	}

	/**
	 * 手机sim卡ID
	 * 
	 * @return
	 */
	public static String getImsi() {
		return imsi;
	}

	public static boolean isNetConnected() {
		NetworkInfo ni = connMgr.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}

	public static String getSDCardPath() {
		File f = Environment.getExternalStorageDirectory();
		return f.getAbsolutePath();
	}

	public static boolean isSDCardMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	// 终端标识终端标识
	public static Getter<String> termType = new Getter<String>("android");

	public static PreferPropertyString token = new PreferPropertyString(sp,
			"token", ""); //

	public static PreferPropertyBool zidonglogin = new PreferPropertyBool(sp,
			"zidonglogin", false); // 自动登录

	public static PreferPropertyBool jzmima = new PreferPropertyBool(sp,
			"jzmima", false); // 记住密码

	public static PreferPropertyString userName = new PreferPropertyString(sp,
			"userName", ""); // 用户名
	public static PreferPropertyString passWord = new PreferPropertyString(sp,
			"passWord", ""); // 密码

	public static PreferPropertyString user_id = new PreferPropertyString(sp,
			"userId", ""); // 密码

	public static PreferPropertyString alia = new PreferPropertyString(sp,
			"alia", ""); // 密码

	
	
	public static void cleanUserPre() {
		zidonglogin.set(false);
		jzmima.set(false);
		userName.set("");
		passWord.set("");
		user_id.set("");
	}

	public static void setLoginStatus(int loginStatus) {
	}

}
