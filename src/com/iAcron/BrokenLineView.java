package com.iAcron;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.platform.comapi.map.t;
import com.iAcon.database.model.BindWardBean;
import com.iAcon.response.fetchWardHistory;
import com.iAcon.response.bean.HistoryBean;
import com.iAcon.timer.CalendarView;
import com.iAcon.timer.Day;
import com.iAcon.timer.MonthAdapter;
import com.iAcon.timer.WeekAdapter;
import com.iAcron.R;
import com.iAcron.brokenline.CharInfo;
import com.iAcron.brokenline.ChartView;
import com.iAcron.brokenline.LineChart;
import com.iAcron.brokenline.LineInfo;
import com.iAcron.fragment.CommonFragment;
import com.iAcron.net.http.HttpCallBack;
import com.iAcron.net.http.RequstClient;
import com.iAcron.util.Common;
import com.iAcron.util.Util;
import com.iAcron.view.MyBrokenline;
import com.iAcron.view.MyToast;
import com.iAcron.view.ProgressLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BrokenLineView extends BaseActivity implements OnClickListener {

	private TextView dayTv, weekNameTv;

	private Button weekBt, mouthBt;

	CalendarView calender;
	private int showType;
	Day[] days;
	long todayTime; // 当前时间
	private BindWardBean bean;

	MyBrokenline lineChar;

	private int weekSo;
	private Map<String, String> monthMap = new HashMap<String, String>();
	private TextView title;
	ProgressLayout prolayout;
	ProgressBar proBar;
	ImageView proimg;
	private TextView proTv;

	public void setPro(String data) {
		try {
			if (data == null) {
				return;
			}

			int dateIn = Integer.parseInt(data);
			if (bean.getType().equals(CommonFragment.JIUHU)) {
				title.setText("瘫倒");

				int proIndex =0;
				if(dateIn<25){
					proIndex = 0;
				}else if(dateIn>=25&&dateIn<50){
					proIndex = 1;
				}else if(dateIn>=50&&dateIn<75){
					proIndex = 2;
				}else{
					proIndex = 3;
				}
				prolayout.setRes(CommonFragment.res).setKedu(
						new String[] { "25%", "50%", "75%" }).index = proIndex;

				proBar.setVisibility(View.GONE);
				proimg.setImageResource(R.drawable.u111);
				proTv.setText(dateIn+"次");
				prolayout.invalidate();
			} else if (bean.getType().equals(CommonFragment.XINLUE)) {
				int index = Util.myLayout(fetch.heart_rate_high,
						fetch.heart_rate_low, dateIn, prolayout.getWidth());
				
				int proIndex =0;
				if(dateIn<60){
					proIndex = 0;
				}else if(dateIn>=60&&dateIn<90){
					proIndex = 1;
				}else if(dateIn>=90&&dateIn<120){
					proIndex = 2;
				}else{
					proIndex = 3;
				}
				
				
				prolayout.setRes(CommonFragment.res1).setKedu(
						new String[] { "60", "90", "120" }).index = proIndex;
				prolayout.invalidate();
				proBar.setVisibility(View.GONE);
				proimg.setImageResource(R.drawable.u107);
				title.setText("心率");
				proTv.setText(data+"次");

			} else if (bean.getType().equals(CommonFragment.XUEYANG)) {
//				int index = Util.getProgressBarFour(fetch.blood_oxygen_height,
//						fetch.blood_oxygen_low, dateIn);
				int proIndex;
				
				if(dateIn<80){
					proIndex= 0;
				}else if(dateIn>=80&&dateIn<90){
					proIndex=1;
				}else if(dateIn>=90&&dateIn<93){
					proIndex = 2;
				}else{
					proIndex = 3;
				}
				
				prolayout.setRes(CommonFragment.res1).setKedu(
						new String[] { "80%", "90%", "93%" }).index = proIndex;
				prolayout.invalidate();
				proimg.setImageResource(R.drawable.u250);
				proBar.setVisibility(View.GONE);
				title.setText("血氧");
				proTv.setText(dateIn+"%");

			} else if (bean.getType().equals(CommonFragment.BUFA)) {
				prolayout.setVisibility(View.GONE);
				proimg.setImageResource(R.drawable.u103);
				title.setText("运动步数");
				int pro = Util.getProgressBarFour(fetch.plan_step_num, 0,
						dateIn);
				proBar.setProgress(pro);
				proTv.setText(data+"步");

			} else if (bean.getType().equals(CommonFragment.TIME)) {
				prolayout.setVisibility(View.GONE);
				proimg.setImageResource(R.drawable.u248);
				title.setText("睡眠");
				int pro = Util.getProgressBarFour(fetch.sleep_time_long, 0,
						dateIn);
				proBar.setProgress(pro);
				proTv.setText(data+"h");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brokenpage);
		findViewById(R.id.brokenpageBt).setOnClickListener(this);
		title = (TextView) findViewById(R.id.topbar_title);
		proimg = (ImageView) findViewById(R.id.img);
		prolayout = (ProgressLayout) findViewById(R.id.prolayout);
		proBar = (ProgressBar) findViewById(R.id.progressBar);
		this.proTv = (TextView) findViewById(R.id.brokenbartv);

		bean = (BindWardBean) getIntent().getSerializableExtra("obj");

		lineChar = (MyBrokenline) findViewById(R.id.lineChart);
		lineChar.setBean(bean);
		a();

		dayTv = (TextView) findViewById(R.id.broken_time);
		weekNameTv = (TextView) findViewById(R.id.xingqi);
		calender = new CalendarView();
		weekSo = getWeekOfYear();
		try {
			todayTime = getNextDayMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		weekBt = (Button) findViewById(R.id.week);
		weekBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeButtonState(arg0);
			}
		});

		mouthBt = (Button) findViewById(R.id.month);
		mouthBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeButtonState(arg0);
			}
		});

		mouthBt.performClick();

		findViewById(R.id.menuBtn).setOnClickListener(this);
		findViewById(R.id.brokenpageBt).setOnClickListener(this);

		findViewById(R.id.id_calendar_proday).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						proDay();
					}
				});

		findViewById(R.id.id_calendar_nxtday).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						toNextDay();
					}
				});

		dayTv.setText(calender.getYear() + "-" + calender.getDayTitle());
		weekNameTv.setText(calender.getDayAdapter().getWeek());
		days = calender.getDays();
	}

	public final static int SHOWweek = 1;
	public final static int SHOWMONTH = 2;

	@SuppressLint("ResourceAsColor")
	private void changeButtonState(View view) {
		long startTime = 0, endTime = 0;
		a();
		if (view == weekBt) {
			showType = 1;
			weekBt.setBackgroundResource(R.drawable.broken_select);
			weekBt.setTextColor(R.color.sky_blue);
			mouthBt.setBackgroundResource(R.drawable.broken_no);
			mouthBt.setTextColor(R.color.sky_blue);
			goToWeek();
		} else if (view == mouthBt) {
			startTime = calender.getDayAdapter().getFirstDayTimeInMs();
			endTime = calender.getDayAdapter().getLastDayTimeInMs();
			showType = 2;
			weekBt.setBackgroundResource(R.drawable.broken_no);
			weekBt.setTextColor(R.color.sky_blue);
			mouthBt.setBackgroundResource(R.drawable.broken_select);
			mouthBt.setTextColor(R.color.sky_blue);
			request(startTime, endTime);
		}

	}

	public void goToWeek() {
		Calendar c = Calendar.getInstance();
		c.set(calender.getYear(), calender.getMonthAdapter().getMonth(),
				calender.getDayAdapter().getDay(), 00, 01);

		Date date = new Date();

		date.setTime(c.getTimeInMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Date datefrist = calender.getFirstDayOfWeek(date);
		String firstS = df.format(datefrist);

		Date dateEnd = calender.getLastDayOfWeek(date);
		String endS = df.format(dateEnd);

		System.out.println("开始第一天+" + firstS + "   最后一天");
		request(datefrist.getTime(), dateEnd.getTime());
	}

	public void toNextDay() {
		try {
			long dayTime = getNextDayMillis();
			if (dayTime > todayTime) {
				return;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		calender.goToNxtDay();
		dayTv.setText(calender.getYear() + "-" + calender.getDayTitle());

		showLinePro();

		weekNameTv.setText(calender.getDayAdapter().getWeek());
		if (showType == 1) {
			int tempWeek = getWeekOfYear();
			System.out.println(weekSo + " toNextDay " + tempWeek);
			if (weekSo != tempWeek) {
				weekSo = tempWeek;
				goToWeek();
			}
		} else {

			if (calender.getDayAdapter().isMonthChanged()) {
				calender.getMonthAdapter().goToNxtMonth();
				request(calender.getDayAdapter().getFirstDayTimeInMs(),
						calender.getDayAdapter().getLastDayTimeInMs());
			} else {
				// int day = calender.getDayAdapter().getDay();
				// if (map.get(day) != null) {
				// arrayATO.addAll(map.get(day));
				// }
			}
		}
	}

	/**
	 * 上一天 先判断是月视图还是周视图； 如果是月视图，判断月有没有改变，如果改变，重新请求网络。
	 * 如果是周视图，先获取当前日期是全年的第几周，如果周改变了，重新请求网络。 否则在map里面拿数据
	 */
	public void proDay() {
		calender.goToPreDay();

		dayTv.setText(calender.getYear() + "-" + calender.getDayTitle());

		showLinePro();
		weekNameTv.setText(calender.getDayAdapter().getWeek());
		if (showType == 1) {
			int tempWeek = getWeekOfYear();
			System.out.println(weekSo + " toNextDay " + tempWeek);

			if (weekSo != tempWeek) {
				weekSo = tempWeek;
				goToWeek();
			}

		} else {
			if (calender.getDayAdapter().isMonthChanged()) {
				calender.getMonthAdapter().goToPreMonth();
				request(calender.getDayAdapter().getFirstDayTimeInMs(),
						calender.getDayAdapter().getLastDayTimeInMs());
			} else {
				// int day = calender.getDayAdapter().getDay();
				// if (map.get(day) != null) {
				// arrayATO.addAll(map.get(day));
				// }
			}
		}

	}

	/**
	 * 老外的时间是周一，所以要减一
	 * 
	 * @return
	 */
	public int getWeekOfYear() {

		Calendar ca = Calendar.getInstance();
		ca.set(calender.getYear(), calender.getMonthAdapter().getMonth(),
				calender.getDayAdapter().getDay() - 1, 00, 01);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ca.getTimeInMillis());
		return c.get(Calendar.WEEK_OF_YEAR);

	}

	public long getNextDayMillis() {
		Calendar ca = Calendar.getInstance();
		ca.set(calender.getYear(), calender.getMonthAdapter().getMonth(),
				calender.getDayAdapter().getDay() + 1, 00, 01);
		return ca.getTimeInMillis();
	}

	public void a() {
		String[] data = null;
		if (bean.getType().equals(CommonFragment.BUFA)) { // 步数
			data = new String[13];
			for (int i = 0; i < data.length; i++) {
				data[i] = "" + i * 1000;
			}
		} else if (bean.getType().equals(CommonFragment.TIME)) { // 随眠时间
			data = new String[13];
			for (int i = 0; i < data.length; i++) {
				data[i] = "" + i;
			}
		} else if (bean.getType().equals(CommonFragment.XINLUE)) { // 心率
			data = new String[13];
			for (int i = 0; i < data.length; i++) {
				data[i] = "" + i * 10;
			}
		} else if (bean.getType().equals(CommonFragment.XUEYANG)) { // 血氧
			data = new String[11];
			for (int i = 0; i < data.length; i++) {
				data[i] = i * 10 + "%";
			}
		} else if (bean.getType().equals(CommonFragment.JIUHU)) { // 跌倒
			data = new String[11];
			for (int i = 0; i < data.length; i++) {
				data[i] = i * 10 + "%";
			}
		}
		lineChar.setYlayout(data);
	}

	fetchWardHistory fetch;

	/**
	 * ward_id 必须 Int 被监护人ID phy_type 必须 String 生理数据类型，可选值如下所示。 运动步数：step_num
	 * 睡眠时间：sleep_time 心率： heart_rate 血氧： blood_oxygen 卡路里： calorie
	 * 瘫倒次数：slump_num from_date 必须 String 提取数据的起始日期。格式为：yyyy-MM-dd to_date 必须
	 * String 提取数据的截止日期。格式为：yyyy-MM-dd token 必须 String user_id 必须 Int
	 * 
	 * 请求网络
	 * 
	 * @param firstDayTimeInMs
	 * @param lastDayTimeInMs
	 */
	private void request(long firstDayTimeInMs, long lastDayTimeInMs) {
		startWaitDialog("提示","网络请求",false);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(firstDayTimeInMs);
		String from_date = df.format(ca.getTime());

		ca.setTimeInMillis(lastDayTimeInMs);
		String end_date = df.format(ca.getTime());

		String url = "http://112.74.95.154:8080/iAcron/fetchWardHistoryPhy";
		Common com = new Common();
		com.setKV("ward_id", "" + bean.getWard_id());
		com.setKV("from_date", "" + from_date);
		com.setKV("to_date", "" + end_date);
		com.setKV("phy_type", bean.getType());

		RequstClient client = new RequstClient(this, new HttpCallBack() {

			@Override
			public void onSuccess(String data) {
				System.out.println("data" + data);

				finishWaitDialog();
				fetchWardHistory fetch = new fetchWardHistory(
						BrokenLineView.this);
				fetch.parse(data);
				if (fetch.result == 1) {
					monthMap.clear();
					BrokenLineView.this.fetch = fetch;
					showChar(fetch.lists);
					showLinePro();
				} else {
					MyToast.showToast(BrokenLineView.this, fetch.result_msg);
				}
			}

			@Override
			public void onAppError(int error, Throwable message) {
				finishWaitDialog();
			}

			@Override
			public void onFailure(int error, Throwable message) {
				finishWaitDialog();
			}

		});
		client.post(url, com.getParams());

	}

	private void showLinePro() {
		if (monthMap.size() <= 0) {
			for (HistoryBean b : fetch.lists) {
				monthMap.put(b.getPhy_date(), b.getValue());
			}
		}

		String data = monthMap.get(dayTv.getText().toString());
		System.out.println("时间....." + dayTv.getText().toString() + "value"
				+ data);

		setPro(data);

		// if (bean.getType().equals(CommonFragment.BUFA)) { // 步数
		// proTv.setText(data);
		// } else if (bean.getType().equals(CommonFragment.TIME)) { // 随眠时间
		// proTv.setText(data);
		// } else if (bean.getType().equals(CommonFragment.XINLUE)) { // 心率
		// proTv.setText(data + "/" + fetch.heart_rate_high);
		// } else if (bean.getType().equals(CommonFragment.XUEYANG)) { // 血氧
		// proTv.setText(data + "/" + fetch.blood_oxygen_height);
		// } else if (bean.getType().equals(CommonFragment.JIUHU)) { // 跌倒
		// proTv.setText(data + "/");
		// }

	}

	private void showChar(ArrayList<HistoryBean> lists) {

		if (showType == 1) { // 周视图
			int size = lists.size();
			for (int i = 0; i < size; i++) { // 全部竖线可见
				lists.get(i).setShowYline(true);
			}
			lineChar.setMonth(false);

		} else { // 月视图

			int size = lists.size();
			if (calender.getDayAdapter().isMonthChanged()) { // 已经切换到其他月份
				for (int i = 0; i < size; i++) {
					if (i % 5 == 0) { // 每5天可见一次
						lists.get(i).setShowYline(true);
					}
				}
			} else { // 当前月
				for (int i = 0; i < size; i++) {
					if (i % 5 == 0) { // 每5天可见一次
						lists.get(i).setShowYline(true);
					}
					if (i == size - 1) {
						lists.get(i).setShowYline(true);
					}
				}

			}
			lineChar.setMonth(true);
		}
		lineChar.setData(lists);
	}

	/*
	 * X坐标
	 */
	public void setBrokenLine(String Xdata[], ArrayList<HistoryBean> lists) {

	}

	public long getDayTimeMillis(int year, int month, int day, int hourOfDay,
			int minute) throws Exception {
		// System.out.println("时间转换："+year+"-"+month+"-"+day);
		// Calendar create = Calendar.getInstance(); // ��ǰʱ��
		// create.set(year, month+1, day, hourOfDay, minute);
		// return create.getTimeInMillis();
		return calender.getDayAdapter().getDateLongTime();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menuBtn:
			finish();
			break;
		case R.id.brokenpageBt:
			startActivity(new Intent(this,RealTimeActivity.class));
			break;

		default:
			break;
		}
	}

}
