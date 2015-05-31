package com.iAcron.view;

import com.baidu.mapapi.map.MapView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class MyViewPaper extends ViewPager {

	public MyViewPaper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof MapView) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
