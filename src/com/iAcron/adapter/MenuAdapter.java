package com.iAcron.adapter;

import java.util.ArrayList;

import com.iAcron.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

	ArrayList items;
	private Activity mContext;

	public MenuAdapter(ArrayList items, Activity con) {
		this.items = items;
		this.mContext = con;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		Object o = getItem(position);
		if (o instanceof MenuTitle) {
			return 0;
		} else if (o instanceof MenuContent) {
			return 1;
		} else if (o instanceof MenuGone) {
			return 2;
		}
		return 0;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	public boolean isEnabled(int position) {

		return getItem(position) instanceof MenuContent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = convertView;
		Object item = getItem(position);
		if (item instanceof MenuGone) {
			if (view == null) {
				view = this.mContext.getLayoutInflater().inflate(
						R.layout.menu_item_no, parent, false);
			}
			

		} else if (item instanceof MenuContent) {
			if (view == null) {
				view = this.mContext.getLayoutInflater().inflate(
						R.layout.menu_item_content, parent, false);
			}
			TextView tv = (TextView)view.findViewById(R.id.menu_item_content);
			tv.setText(((MenuContent) item).str);
			
			if(selectColor == position){
				view.setBackgroundColor(0xfff1f1f1);
			}else{
				view.setBackgroundColor(0xffffffff);
			}
			
		} else if (item instanceof MenuTitle) {
			if (view == null) {
				view = this.mContext.getLayoutInflater().inflate(
						R.layout.menu_item_title, parent, false);
			}
			TextView tv = (TextView)view.findViewById(R.id.menu_item_title);
			tv.setText(((MenuTitle) item).str);
		}
		
		
		
		return view;
	}
	
	public int selectColor;
}
