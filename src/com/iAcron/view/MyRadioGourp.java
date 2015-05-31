package com.iAcron.view;

import com.iAcron.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MyRadioGourp extends RadioButton {

	Paint p;

	public MyRadioGourp(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		p = new Paint();
	}

	private int count;

	private int level = 0;
	public void setBack(int level){
		this.level = level;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (count > 0) {
			p.setColor(Color.RED);
			p.setTextSize(30);
			canvas.drawText("" + count, getWidth() - 30, 30, p);
		}
		switch (level) {
		case 0:
			setBackgroundResource(R.drawable.tab_level1_select);
			break;
		case 1:
			setBackgroundResource(R.drawable.tab_level2_select);
			break;
		case 2:
			setBackgroundResource(R.drawable.tab_level3_select);
			break;
		}
	}
	
	public void setCount(int c) {
		count = c;
	}
}
