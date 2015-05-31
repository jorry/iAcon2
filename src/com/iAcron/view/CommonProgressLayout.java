package com.iAcron.view;

import com.iAcron.R;
import com.iAcron.util.ConvertUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CommonProgressLayout extends LinearLayout {

	private Context mCon;

	public CommonProgressLayout(Context context) {
		super(context);
		setWillNotDraw(false);
		mCon = context;
	}

	public CommonProgressLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCon = context;
		setWillNotDraw(false);
		setOrientation(LinearLayout.VERTICAL);
	}

	@SuppressLint({ "DrawAllocation", "NewApi" })
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (index == 1) {
			setBackgroundColor(0XFF68CDB0);
		} else if (index == 2) {
			setBackgroundColor(0XFFE35E03);
		} else if (index == 3) {
			setBackgroundColor(0Xffdb0d00);
		} else if (index == 4) {
			setBackgroundColor(0Xffdb0d00);
		}
	}

	public int index = 1;
}
