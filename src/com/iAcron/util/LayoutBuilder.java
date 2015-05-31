package com.iAcron.util;

import com.iAcron.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class LayoutBuilder {

	FragmentManager manager;
	FragmentActivity context;
	public LayoutBuilder(FragmentActivity activity){
		manager = activity.getSupportFragmentManager();
		context  = activity;
	}
	
	public void replaceRegister(Class<?> f,Bundle b){
		manager.beginTransaction().replace(R.id.register_fragmment, getFregment(context, f.getName(), b),f.getName()).addToBackStack(f.getName()).commitAllowingStateLoss();
	}
	
	
	
	public Fragment getFregment(Context con,String name,Bundle b){
		return Fragment.instantiate(con, name, b);
	}
	
}
