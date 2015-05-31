package com.iAcron.fragment;

import java.util.ArrayList;

import com.iAcron.R;
import com.iAcron.adapter.MenuAdapter;
import com.iAcron.adapter.MenuContent;
import com.iAcron.adapter.MenuGone;
import com.iAcron.adapter.MenuTitle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MenuFragment extends BaseFregment{

	MenuAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View menuView = inflater.inflate(R.layout.menu, null);
		ListView list = (ListView) menuView.findViewById(R.id.men_list);
		
		ArrayList<Object> menuList = new ArrayList<Object>();
		menuList.add(new MenuTitle("客厅"));
		menuList.add(new MenuContent("亲情列表"));
		menuList.add(new MenuContent("监护申请"));
		menuList.add(new MenuContent("家庭管理"));
		
		menuList.add(new MenuGone());
		
		menuList.add(new MenuTitle("亲情关怀"));
		menuList.add(new MenuContent("电话薄"));
		menuList.add(new MenuContent("安全配置"));
		menuList.add(new MenuContent("充值"));   //二期	
		menuList.add(new MenuGone());
		
		adapter = new MenuAdapter(menuList, getActivity());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter.selectColor = arg2 ;
				adapter.notifyDataSetChanged();
			}
		});
		return menuView;
	}
}
