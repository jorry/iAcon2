package com.iAcron.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

	private SharedPreferences sp = null;

	public void init(Context c, String name) {
		sp = c.getSharedPreferences(name, 0);
	}

	public void putBool(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public boolean getBool(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void putInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public void putLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	public long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public void putString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

}
