package com.iAcron.view;

import com.iAcron.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WardButton extends Button {

	Paint p;

	public WardButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		p = new Paint();
	}

	private int count;

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (count > 0) {
			p.setColor(Color.RED);
			int x = getWidth() / 2 + getTextX("短消息") + 10;
			int y = getHeight() / 2 - getTextY("短消息");

			canvas.drawRect(x, (float) (y - ((int)getTextY("" + count)*2.5)), x
					+ getTextX("" + count) * 3, y, p);

			p.setColor(Color.WHITE);
			p.setTextSize(30);

			canvas.drawText("" + count, x, y, p);

		}
	}

	public int getTextX(String text) {
		Paint pFont = new Paint();
		Rect bounds = new Rect();
		pFont.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.width();

		return width;
	}

	public int getTextY(String text) {
		Paint pFont = new Paint();
		Rect bounds = new Rect();
		pFont.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.height();

		return width;
	}

	public void setCount(int c) {
		count = c;
	}
}
