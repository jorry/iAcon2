package com.iAcron.view;

import java.util.ArrayList;

import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.bean.HistoryBean;
import com.iAcron.fragment.CommonFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("DrawAllocation")
public class MyBrokenline extends View {
	public MyBrokenline(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setYlayout(String[] ylabel) {
		this.Ylabel = ylabel;
	}

	ArrayList<HistoryBean> lists = new ArrayList<HistoryBean>();

	/**
	 * 
	 * @param xlabel
	 *            鏃ユ湡
	 * @param ylabel
	 *            鐢熺悊淇℃伅
	 * @param data
	 *            K绾垮浘鏁版嵁
	 */
	public void setData(ArrayList<HistoryBean> his) {
		this.lists.clear();
		this.lists.addAll(his);
		init();
		invalidate();
	}

	public MyBrokenline(Context context) {
		super(context);
	}

	// 榛樿杈硅窛
	private int Margin = 40;
	// 鍘熺偣鍧愭爣
	private int Xpoint;
	private int Ypoint;
	// X,Y杞寸殑鍗曚綅闀垮害
	private int Xscale = 20;
	private int Yscale = 20;
	// X,Y杞翠笂闈㈢殑鏄剧ず鏂囧瓧
	private String[] Ylabel = { "0", "10", "20", "30", "40", "50", "60", "70",
			"80", "90", "1000" };
	private boolean isMonth = true;
	String xLine[] = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	public void setMonth(boolean isMonth) {
		this.isMonth = isMonth;
	}

	// 鍒濆鍖栨暟鎹�
	public void init() {
		Xpoint = this.Margin;
		Ypoint = this.getHeight() - this.Margin;
		if (!isMonth) {
			Xscale = (this.getWidth() - 2 * this.Margin)
					/ (this.xLine.length - 1);
			System.out.println("init" + Xscale);

		} else {
			Xscale = (this.getWidth() - 2 * this.Margin) / this.lists.size();
			System.out.println(getWidth() + "-----" + lists.size() + "init"
					+ Xscale);

		}
		Yscale = (this.getHeight() - 2 * this.Margin)
				/ (this.Ylabel.length - 1);
	}

	public int getMargin() {
		return Margin;
	}

	public void setMargin(int margin) {
		Margin = margin;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (lists == null || lists.size() == 0) {
			return;
		}
		if (Ylabel == null) {
			return;
		}
		Paint p1 = new Paint();
		p1.setStyle(Paint.Style.STROKE);
		p1.setAntiAlias(true);
		p1.setColor(Color.BLACK);
		p1.setStrokeWidth(2);
		init();
		this.drawXLine(canvas, p1);
		this.drawYLine(canvas, p1);
		this.drawTable(canvas);
		this.drawData(canvas);
	}

	int YlableX = 15;

	/**
	 * 如果是月视图， 第一个for循环，i表示的是下标索引
	 * 
	 * @param canvas
	 */
	private void drawTable(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		Path path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		if (!isMonth) {
			for (int i = 0; i * Xscale <= (this.getWidth() - this.Margin); i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				int startY = Ypoint;
				int stopY = Ypoint - (this.Ylabel.length - 1) * Yscale;
				paint.setPathEffect(effects);
				path.moveTo(startX, startY);

				path.lineTo(startX, stopY);
				canvas.drawPath(path, paint);
			}
		} else {
			for (int i = 0; i * Xscale <= (this.getWidth() - this.Margin * 3); i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				int startY = Ypoint;
				int stopY = Ypoint - (this.Ylabel.length - 1) * Yscale;
				if (lists.get(i).isShowYline()) {
					paint.setPathEffect(effects);
					path.moveTo(startX, startY);
					path.lineTo(startX, stopY);
					canvas.drawPath(path, paint);

				}
			}
		}

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.BLACK);
		p.setTextSize(this.Margin / 3);
		if (!isMonth) {
			for (int i = 0; (Ypoint - i * Yscale) >= this.Margin; i++) {
				int startX = Xpoint + YlableX;
				int startY = Ypoint - i * Yscale;
				int stopX = Xpoint + (this.xLine.length - 1) * Xscale + YlableX;
				paint.setColor(Color.DKGRAY);
				canvas.drawPath(path, paint);
				paint.setColor(Color.BLACK);
				paint.setTextSize(this.Margin / 10);
				if (bean != null) {
					if (bean.getType().equals(CommonFragment.XUEYANG)) {
						if(Ylabel[i].length()>0){
							Ylabel[i] = Ylabel[i].substring(0,
									Ylabel[i].indexOf("%"));
							if (Integer.parseInt(Ylabel[i]) >= 60) {
								canvas.drawText(this.Ylabel[i], 1, startY
										+ this.Margin / 4, p);
							}	
						}
						
					} else {
						canvas.drawText(this.Ylabel[i], 1, startY + this.Margin
								/ 4, p);
					}
				}

			}
		} else {
			for (int i = 0; (Ypoint - i * Yscale) >= this.Margin; i++) {
				int startX = Xpoint + YlableX;
				int startY = Ypoint - i * Yscale;
				int stopX = Xpoint + (this.lists.size() - 1) * Xscale + YlableX;
				paint.setColor(Color.DKGRAY);
				canvas.drawPath(path, paint);
				paint.setColor(Color.BLACK);
				paint.setTextSize(this.Margin / 10);
				canvas.drawText(this.Ylabel[i], 1, startY + this.Margin / 4, p);
			}
		}

		if (bean != null) {
			if (bean.getType().equals(CommonFragment.XUEYANG)) {
				Paint p1 = new Paint();
				p1.setStyle(Paint.Style.STROKE);
				p1.setColor(0xffff9700);
				Path path1 = new Path();
				if (!isMonth) {
					for (int i = 0; i < Ylabel.length; i++) {
						int startX = Xpoint + YlableX;
						int startY = Ypoint - i * Yscale;
						int stopX = Xpoint + (this.xLine.length - 1) * Xscale
								+ YlableX;
						if (Ylabel[i].startsWith("90")
								|| Ylabel[i].startsWith("60")) {
							path1.moveTo(startX, startY);
							path1.lineTo(stopX, startY);
							canvas.drawPath(path1, p1);
						}
					}
				} else {
					for (int i = 0; i < Ylabel.length; i++) {
						int startX = Xpoint + YlableX;
						int startY = Ypoint - i * Yscale;
						int stopX = Xpoint + (this.lists.size() - 1) * Xscale
								+ YlableX;
						if (Ylabel[i].startsWith("90")
								|| Ylabel[i].startsWith("60")) {
							path1.moveTo(startX, startY);
							path1.lineTo(stopX, startY);
							canvas.drawPath(path1, p1);
						}
					}
				}
			}
		}
	}

	// 鐢绘í绾佃酱
	private void drawXLine(Canvas canvas, Paint p) {
		p.setColor(Color.BLACK);
		canvas.drawLine(Xpoint + YlableX, Ypoint, this.Margin + YlableX,
				this.Margin, p);
		// canvas.drawLine(Xpoint, this.Margin, Xpoint - Xpoint / 3, this.Margin
		// + this.Margin / 3, p);
		// canvas.drawLine(Xpoint, this.Margin, Xpoint + Xpoint / 3, this.Margin
		// + this.Margin / 3, p);
	}

	private void drawYLine(Canvas canvas, Paint p) {
		p.setColor(Color.BLACK);
		canvas.drawLine(Xpoint + YlableX, Ypoint, this.getWidth() - this.Margin
				+ YlableX, Ypoint, p);
		// canvas.drawLine(this.getWidth() - this.Margin, Ypoint,
		// this.getWidth()
		// - this.Margin - this.Margin / 3, Ypoint - this.Margin / 3, p);
		// canvas.drawLine(this.getWidth() - this.Margin, Ypoint,
		// this.getWidth()
		// - this.Margin - this.Margin / 3, Ypoint + this.Margin / 3, p);
	}

	// 鐢绘暟鎹�
	private void drawData(Canvas canvas) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.BLACK);
		p.setTextSize(this.Margin / 2);
		if (!isMonth) {
			for (int i = 0; i <= lists.size() - 1; i++) {
				System.out.println("i = " + i + "   ");
				int startX = Xpoint + i * Xscale + YlableX;
				p.setColor(Color.BLACK);
				canvas.drawText(xLine[i], startX - this.Margin / 2,
						this.getHeight() - this.Margin / 4, p);
			}

		} else {
			for (int i = 0; i <= lists.size() - 1; i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				p.setColor(Color.BLACK);
				if (lists.get(i).isShowYline()) {
					String date = this.lists.get(i).getPhy_date();
					String show = date.substring(date.indexOf("-") + 1);
					canvas.drawText(show, startX - this.Margin / 2,
							this.getHeight() - this.Margin / 4, p);
				}
			}

		}
		// 绾靛悜绾�
		if (!isMonth) {
			for (int i = 1; i <= lists.size() - 1; i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				p.setStrokeWidth(2);
				p.setColor(0xffc3c4dc);
				int point = (int) Double.parseDouble(lists.get(i - 1)
						.getValue());

				int point1 = (int) Double.parseDouble(lists.get(i).getValue());
				canvas.drawLine(Xpoint + (i - 1) * Xscale + YlableX,
						calY(point), startX, calY(point1), p);
			}
		} else {
			for (int i = 1; i <= lists.size() - 1; i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				p.setStrokeWidth(2);
				p.setColor(0xffc3c4dc);
				int point = (int) Double.parseDouble(lists.get(i - 1)
						.getValue());

				int point1 = (int) Double.parseDouble(lists.get(i).getValue());
				canvas.drawLine(Xpoint + (i - 1) * Xscale + YlableX,
						calY(point), startX, calY(point1), p);
			}
		}

		//
		if (!isMonth) {
			for (int i = 0; i <= lists.size() - 1; i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				p.setColor(Color.BLACK);
				p.setColor(0xff000080);
				int point1 = (int) Double.parseDouble(lists.get(i).getValue());
				canvas.drawCircle(startX, calY(point1), 6, p);

			}
		} else {
			for (int i = 0; i <= lists.size() - 1; i++) {
				int startX = Xpoint + i * Xscale + YlableX;
				p.setColor(Color.BLACK);
				p.setColor(0xff000080);
				int point1 = (int) Double.parseDouble(lists.get(i).getValue());
				canvas.drawCircle(startX, calY(point1), 6, p);

			}
		}

	}

	/**
	 * 
	 * @param y
	 * @return
	 */
	private int calY(int y) {
		int y0 = 0;
		int y1 = 0;
		if (Ylabel[0].endsWith("%")) {
			Ylabel[0] = Ylabel[0].substring(0, Ylabel[0].indexOf("%"));
		}
		if (Ylabel[1].endsWith("%")) {
			Ylabel[1] = Ylabel[1].substring(0, Ylabel[1].indexOf("%"));
		}
		try {
			y0 = Integer.parseInt(Ylabel[0]);
			y1 = Integer.parseInt(Ylabel[1]);
		} catch (Exception e) {
			Log.i("zzzz", "string changed is err");
			return 0;
		}
		try {
			return Ypoint - ((y - y0) * Yscale / (y1 - y0));
		} catch (Exception e) {
			// Log.i("zzzz", "return is err");
			return 0;
		}
	}

	BindWardBean bean;

	public void setBean(BindWardBean bean) {
		this.bean = bean;
	}

}
