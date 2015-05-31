package com.iAcron.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

/**
 * 工具类
 * 
 * 1.
 * 
 * @author match
 * 
 */
public class Util {

	public static String savePlayer(String name, byte[] data) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
		}
		String rootRecorder = "mjGame/recorder/";
		File rootFile = new File(
				android.os.Environment.getExternalStorageDirectory() + "/"
						+ rootRecorder + name);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		File file = null;
		try {
			file = new File(rootFile, name + ".amr");
			FileOutputStream os = new FileOutputStream(file);
			os.write(data);
			os.flush();
			os.close();
			os = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" http   path " + file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	/**
	 * 一种颜色的进度条
	 * 
	 * @param total
	 * @param index
	 * @return
	 */
	public static int getProgetssIndex(float total, float index) {
		LogUtil.i("user", total + " getProgetssIndex " + index);
		if (index <= 0) {
			return 0;
		}
		return (int) (index / total * 100);
	}

	public static int myLayout(float hight, float low, int index, int viewWight) {
		System.out.println("myLayout = " + hight + "  " + low + "  " + index
				+ "   " + viewWight);
		float l = hight - low;
		float z = viewWight / l; // 每份
		return (int) (z * index);
	}

	/**
	 * 4种颜色的进度条
	 * 
	 * @param hight
	 *            最高
	 * @param low
	 *            最低
	 * @param index
	 *            用户值
	 * @return
	 */
	public static int getProgressBarFour(float hight, float low, float index) {
		float a = index - low;
		float b = hight - low;
		float f = a / b;

		return (int) (f * 100);
	}

	public static String convertVoiceToHexText(byte[] voiceData) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < voiceData.length; i++) {
			String hex = Integer.toHexString(voiceData[i] & 0xff);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			buf.append(hex);
		}
		return buf.toString();
	}

	/**
	 * 将十六进制文本转换为语音数据
	 * 
	 * @param strVoice
	 * @return
	 */
	public static byte[] convertHexTextToVoice(String strVoice) {
		short[] voiceData = new short[strVoice.length() / 2];
		int byteIndex = 0;
		for (int i = 0; i < strVoice.length(); i += 2) {
			voiceData[byteIndex++] = Short.parseShort(
					strVoice.substring(i, i + 2), 16);
		}
		byte[] vd = new byte[voiceData.length];
		for (int i = 0; i < voiceData.length; i++) {
			vd[i] = (byte) (voiceData[i] & 0xff);
		}
		return vd;
	}

	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day);

		return sbBuffer.toString();
	}

	public static String getHour() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		return year;
	}

	public static String getminute() {
		Calendar c = Calendar.getInstance();
		String month = String.valueOf(c.get(Calendar.MINUTE));
		return month;
	}

	public static boolean equal(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	public static boolean checkNet(Context context) {
		if (hasConnectedNetwork(context)) {
			return true;
		}
		showToast(context, "无网络连接");
		return false;
	}

	public static boolean hasConnectedNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService("connectivity");
		if (connectivity == null) {
			return false;
		}
		return connectivity.getActiveNetworkInfo() != null;
	}

	public static void showToast(Context context, String s) {
		if (true) {
			Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
		}

	}

	public static void showToastLong(Context context, String s) {
		Toast.makeText(context, s, 1).show();
	}

	public static boolean isPhone(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			return phone.matches("1+[3,5,8]+[0-9]{9}");
		}
		return false;
	}

	public static void alertToast(final Context context, final String str) {
		Handler messageHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message msg) {
				Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
				toast.show();
				removeMessages(1);
			}
		};
		messageHandler.sendEmptyMessage(1);
	}

	public static void sharedPreferencesSave(Bundle bundle, Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = uiState.edit();
		Set<String> keys = bundle.keySet();
		for (String key : keys) {
			editor.putString(key, bundle.getString(key));
		}
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, String value,
			Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, long value,
			Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void sharedPreferencesSave(Context context, String key,
			boolean is) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putBoolean(key, is);
		editor.commit();
	}

	public static void sharedPreferencesSave(String key, int value,
			Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getFromSharedPreferences(String key, Context context,
			int dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getInt(key, dafault);
	}

	public static long getFromSharedPreferences(String key, Context context,
			long dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		Long l = Long.valueOf(uiState.getLong(key, dafault));
		if (l == null) {
			l = Long.valueOf(0L);
		}
		return l.longValue();
	}

	public static String getFromSharedPreferences(String key, Context context,
			String dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getString(key, dafault);
	}

	public static boolean getFromSharedPreferences(Context context, String key,
			boolean dafault) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return dafault;
		}
		return uiState.getBoolean(key, dafault);
	}

	public static String getFromSharedPreferences(String key, Context context) {
		SharedPreferences uiState = context.getSharedPreferences("PREFS_NAME",
				1);
		if (uiState == null) {
			return null;
		}
		return uiState.getString(key, "");
	}
}