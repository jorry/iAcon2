package com.iAcron.brokenline;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * ����ͼ�ؼ�
 * 
 * @author ��ָ����
 * @version 1.0
 */
public class LineChart extends View {

	Typeface font = Typeface.create("宋体", Typeface.NORMAL);
	Typeface blodFont = Typeface.create("宋体", Typeface.BOLD);

	private CharInfo charInfo;// ͼ����Ϣ
	private List<LineInfo> lineInfos;// ������Ϣ

	public LineChart(Context context) {
		super(context);
	}

	public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public LineChart(Context context, AttributeSet attrs) {
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

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setTypeface(blodFont);
//		canvas.drawText(charInfo.getTitle(), 100, 20, paint);

		int ySpliteNum = charInfo.getyScaleNum();
		int xSpliteNum = charInfo.getxScaleNum();

		// X����Ϣ
		int xLineXStartCoord = this.getLeft() + 50;
		int xLineXEndCoord = this.getRight() - 50;
		
		
		int xLineYStartCoord = this.getBottom() ;
		int xLineYEndCoord = xLineYStartCoord;
		int xLineLength = xLineXEndCoord - xLineXStartCoord;

		// Y����Ϣ
		int yLineXStartCoord = xLineXStartCoord;
		int yLineXEndCoord = xLineXStartCoord;
		
		int yLineYStartCoord = xLineYStartCoord;
		int yLineYEndCoord = this.getTop() ;
		
		int yLineLength = yLineYStartCoord > yLineYEndCoord == true ? yLineYStartCoord
				- yLineYEndCoord
				: yLineYEndCoord - yLineYStartCoord;

		// �������
		paint.setStrokeWidth(2);
		canvas.drawLine(xLineXStartCoord, xLineYStartCoord, xLineXEndCoord,
				xLineYEndCoord, paint);
		canvas.drawLine(yLineXStartCoord, yLineYStartCoord, yLineXEndCoord,
				yLineYEndCoord, paint);

		// ��X��(ʱ����)�̶�
		paint.setColor(0xCCCCCCFF);
		Integer[] xPoint = new Integer[xSpliteNum];
		int xStep = xLineLength / xSpliteNum;
		int xLine = xLineXStartCoord;
		for (int i = 0; i < xSpliteNum; i++) {
			xLine += xStep;
			xPoint[i] = xLine;
			canvas.drawLine(xLine, yLineYStartCoord, xLine, yLineYEndCoord, paint);
		}

		String[] xScaleDownLabel = charInfo.getxScaleDownLable();
		for (int i = 0; i < xPoint.length; i++) {
			paint.setColor(Color.BLACK);
			paint.setTextSize(14);
			paint.setTypeface(font);
			paint.setTextAlign(Align.RIGHT);
			if (i == 0) {
				canvas.drawText(xScaleDownLabel[i], yLineXStartCoord,
						xLineYStartCoord + 15, paint);
				canvas.drawText(xScaleDownLabel[i + 1], xPoint[i],
						xLineYStartCoord + 15, paint);
			} else {
				canvas.drawText(xScaleDownLabel[i], xPoint[i],
						xLineYStartCoord + 15, paint);
			}
		}

		String[] yScaleLeftLabel = charInfo.getyScaleLeftLable();
		// Y���߻���
		Integer[] yPoint = new Integer[ySpliteNum];// 6��
		int yStep = yLineLength / ySpliteNum;
		int yLine = yLineYStartCoord;
		for (int i = 0; i < ySpliteNum; i++) {
			if (i == ySpliteNum - 1) {
				yLine = yLineYEndCoord;
			} else {
				yLine -= yStep;
			}
			yPoint[i] = yLine; //
			 canvas.drawLine(yLineXStartCoord, yLine, yLineXStartCoord
			 + xLineLength, yLine, paint);
		}

		// Y��ɫ�顢����
		for (int i = 0; i < yPoint.length; i++) {
			paint.setStrokeWidth(3);
			paint.setColor(Color.BLACK);
			switch (i) {
			case 0:
				canvas.drawLine(yLineXStartCoord, xLineYStartCoord,
						yLineXStartCoord, yPoint[i], paint);

				paint.setTextSize(14);
				paint.setTypeface(font);
				paint.setTextAlign(Align.RIGHT);
				canvas.drawText("0", yLineXStartCoord - 6, xLineYStartCoord,
						paint);
				break;
			case 1:
				canvas.drawLine(yLineXStartCoord, yPoint[i - 1],
						yLineXStartCoord, yPoint[i], paint);
				break;
			case 2:
				canvas.drawLine(yLineXStartCoord, yPoint[i - 1],
						yLineXStartCoord, yPoint[i], paint);
				break;
			case 3:
				canvas.drawLine(yLineXStartCoord, yPoint[i - 1],
						yLineXStartCoord, yPoint[i], paint);
				break;
			case 4:
				canvas.drawLine(yLineXStartCoord, yPoint[i - 1],
						yLineXStartCoord, yPoint[i], paint);
				break;
			case 5:
				canvas.drawLine(yLineXStartCoord, yPoint[i - 1],
						yLineXStartCoord, yPoint[i], paint);
				break;
			default:
				break;
			}
			paint.setColor(Color.BLACK);
			paint.setTextSize(14);
			paint.setTypeface(font);
			paint.setTextAlign(Align.RIGHT);
			canvas.drawText(yScaleLeftLabel[i], yLineXStartCoord - 6,
					yPoint[i], paint);
			paint.setTextAlign(Align.LEFT);
		}

		System.out.println("(float)xScaleDownLabel.length = "+(float)xScaleDownLabel.length);
		// ��������
		float stepX = (float) xLineLength / 6f;
		float lowStepY = (float) (yLineLength - (yStep * 2));
		float middleStepY = (float) yStep / 100.0f;
		float highStepY = (float) yStep / 200.0f;
		for (int z = 0; z < lineInfos.size(); z++) {
			LineInfo lineInfo = lineInfos.get(z);

			int lastStepX = -1;
			int lastStepY = -1;
			for (int j = 0; j < lineInfo.getPoints().length; j++) {
				// ȷ�����λ��
				int pointX = xLineXStartCoord + (int) (j * stepX);
				int pointY = 0;
				// ����
				paint.setColor(lineInfo.getPointColor());
				canvas.drawCircle(pointX, pointY, 3, paint);
				pointY = xLineYEndCoord - lineInfo.getPoints()[j]*yStep/50;
				// ����
				if (lastStepX != -1 && lastStepY != -1) {
					paint.setColor(lineInfo.getLineColor());
					paint.setStrokeWidth(1.5f);
					canvas.drawLine(lastStepX, lastStepY, pointX, pointY, paint);
//					canvas.drawLine(startX, startY, stopX, stopY, paint)
				}

				// ��¼
				lastStepX = pointX;
				lastStepY = pointY;
			}

			// ��ͼ�?���ֱ�ʶ
			int markY = this.getBottom() - 40;
			int markX = xLineXStartCoord
					+ (xLineLength / (lineInfos.size() + 1)) * (z + 1) - 30;

			paint.setColor(lineInfo.getPointColor());
			canvas.drawCircle(markX, markY, 3, paint);
			
			paint.setColor(lineInfo.getLineColor());
			paint.setStrokeWidth(1.5f);
//			canvas.drawLine(markX - 10, markY, markX + 10, markY, paint);
//			paint.setColor(Color.BLACK);
//			paint.setTextAlign(Align.LEFT);
//			canvas.drawText(lineInfo.getName(), markX + 10, markY + 5, paint);
		}

	}

	public CharInfo getCharInfo() {
		return charInfo;
	}

	public void setCharInfo(CharInfo charInfo) {
		this.charInfo = charInfo;
	}

	public List<LineInfo> getLineInfos() {
		return lineInfos;
	}

	public void setLineInfos(List<LineInfo> lineInfos) {
		this.lineInfos = lineInfos;
	}

}
