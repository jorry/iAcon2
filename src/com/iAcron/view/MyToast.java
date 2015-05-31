package com.iAcron.view;

import android.app.Activity;
import android.widget.Toast;

public class MyToast {

	public static void showToast(Activity a,String msg){
		Toast.makeText(a, msg, Toast.LENGTH_LONG).show();
	}
}
