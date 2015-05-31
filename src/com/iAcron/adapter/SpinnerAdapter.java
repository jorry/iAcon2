package com.iAcron.adapter;

import com.iAcron.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends BaseAdapter{

	private String []  arrys;
	private Context contst;
	LayoutInflater flater;
	public SpinnerAdapter(String strs[],Activity con){
		this.arrys = strs;
		this.contst = con;
		flater = con.getLayoutInflater();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrys.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arrys[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = flater.inflate(R.layout.spinneritem, null);
		TextView t = (TextView)v.findViewById(R.id.spinner_item);
		t.setText(arrys[arg0]);
		return v;
	}

}
