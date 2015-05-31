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
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProgressLayout extends LinearLayout {

	private Context mCon;
	private Bitmap bmp;

	int res[];
	private String kedu[];

	public String[] getKedu() {
		return kedu;
	}

	public ProgressLayout setKedu(String[] kedu) {
		this.kedu = kedu;
		return this;
	}

	public int[] getRes() {
		return res;
	}

	public ProgressLayout setRes(int[] res) {
		this.res = res;
		return this;
	}

	public ProgressLayout(Context context) {
		super(context);
		setWillNotDraw(false);
		mCon = context;
		Resources res = getResources();

		bmp = BitmapFactory.decodeResource(res, R.drawable.u202);

	}

	public ProgressLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCon = context;
		setWillNotDraw(false);
		mCon = context;
		setOrientation(LinearLayout.HORIZONTAL);

		Resources res = getResources();

		bmp = BitmapFactory.decodeResource(res, R.drawable.youbiao);
	}

	public Bitmap getBitmap(int resId) {
		Resources res = getResources();

		return BitmapFactory.decodeResource(res, resId);

	}
	
	@SuppressLint({ "DrawAllocation", "NewApi" })
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (res == null) {
			return;
		}

		System.out.println("res");

		Paint p = new Paint();
		p.setTextSize(20);
		p.setColor(Color.BLACK);
		for (int i = 1; i < res.length; i++) {
			canvas.drawText(kedu[i-1], getWidth() / res.length * i- ConvertUtils.dip2px(mCon, 5), getHeight() / 2
					- ConvertUtils.dip2px(mCon, 5), p);
		}
		for (int i = 0; i < res.length; i++) {
			p.setColor(this.mCon.getResources().getColor(res[i]));
			RectF r2=new RectF();                           //RectF对象  
		    r2.left=getWidth() / res.length * i + 2;                                 //左边  
		    r2.top=getHeight() / 2
					- ConvertUtils.dip2px(mCon, 5);                                 //上边  
		    r2.right= getWidth() / res.length
					* i + getWidth() / res.length;                                   //右边  
		    r2.bottom= getHeight() / 2
					+ ConvertUtils.dip2px(mCon, 5); 
		    
			canvas.drawRoundRect(r2,5,5, p);

		}

		System.out.println(index);
		if (index != -1) {
			if(index>this.getWidth()){
				canvas.drawBitmap(bmp,
						getWidth()-bmp.getWidth(),
						getHeight() / 2 - bmp.getHeight() / 2, p);
				
			}else{
				int weight = getWidth()/res.length;
				canvas.drawBitmap(bmp,
						weight * index+weight/2,
						getHeight() / 2 - bmp.getHeight() / 2, p);
				
			}

		}

	}

	public int index = 1;
}
