package com.iAcron.brokenline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {
	public int XPoint = 0; // 原点的X坐标
	public int YPoint = 0; // 原点的Y坐标
	public int XScale = 0; // X的刻度长度
	public int YScale = 0; // Y的刻度长度
	public int XLength = 0; // X轴的长度
	public int YLength = 0; // Y轴的长度
	public String[] XLabel; // X的刻度
	public String[] YLabel; // Y的刻度
	public int[] Data; // 数据
	public String Title; // 显示的标题

	public ChartView(Context context) {
		super(context);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, int[] AllData,
			String strTitle) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 重写onDraw方法

		if (YLabel == null) {
			return; 
		}
		XPoint = this.getLeft();
		YPoint = this.getBottom();
		System.out.println(""+YPoint);
		int xLineXStartCoord = this.getLeft() ;
		int xLineXEndCoord = this.getRight() ;

		int xLineYStartCoord = this.getBottom();
		int xLineYEndCoord = xLineYStartCoord;
		XLength = xLineXEndCoord - xLineXStartCoord;

		Integer[] xPoint = new Integer[XLabel.length];
		XScale = XLength / XLabel.length;

		int yLineXStartCoord = xLineXStartCoord;
		int yLineXEndCoord = xLineXStartCoord;

		int yLineYStartCoord = xLineYStartCoord;
		int yLineYEndCoord = this.getTop() ;

		YLength = yLineYStartCoord > yLineYEndCoord == true ? yLineYStartCoord
				- yLineYEndCoord+50 : yLineYEndCoord - yLineYStartCoord+50;

		YScale = (YLength / YLabel.length)*2;
		Paint paintTv = new Paint();
		paintTv.setStyle(Paint.Style.STROKE);
		paintTv.setAntiAlias(true);// 去锯齿
		paintTv.setColor(Color.BLACK);// 颜色
		paintTv.setTextSize(20); // 设置轴文字大小
		Paint paintLine = new Paint();
		paintLine.setStrokeWidth((float) 3.0);
		paintLine.setAntiAlias(true);// 去锯齿
		paintLine.setColor(0XFF000088);

		int a = YPoint - YLength;
		
		System.out.println(a+"  "+YPoint);
		// 设置Y轴
		canvas.drawLine(XPoint, 0, XPoint, YPoint, paintLine); // 轴线
		for (int i = 0; i * YScale/2 < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paintLine); // 刻度
			try {
				canvas.drawText(YLabel[i], XPoint - 52,
						YPoint - i * YScale + 5, paintTv); // 文字
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paintLine); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paintLine);
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paintLine); // 轴线
		for (int i = 0; i * XScale < XLength; i++) {

			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 30,
						YPoint + 20, paintTv); // 文字
				// 数据值
				if (i > 0 && YCoord(Data[i - 1]) != -999
						&& YCoord(Data[i]) != -999) { // 保证有效数据
					canvas.drawLine(XPoint + (i - 1) * XScale,
							YCoord(Data[i - 1]), XPoint + i * XScale,
							YCoord(Data[i]), paintLine);
					canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i
							* XScale, YCoord(Data[i]), paintLine); // 刻度
				}
				
				canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 5,
						paintLine);
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paintLine); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paintLine);
	}

	private int YCoord(int y0) // 计算绘制时的Y坐标，无数据时返回-999
	{
		int y = y0;
		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}