package com.iAcon.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

public class CalendarView {

	private MonthAdapter monthAdapter;

	private WeekAdapter weekAdapter;

	private DayAdapter dayAdapter;

	public DayAdapter getDayAdapter() {
		return dayAdapter;
	}

	/** �ӿؼ��߶���Ϣ */
	protected static int childWidthMeasureSpec;
	protected static int childHeightMeasureSpec;

	public CalendarView() {
		init();
	}

	private void init() {
		monthAdapter = new MonthAdapter();
		weekAdapter = new WeekAdapter();
		dayAdapter = new DayAdapter();
	}

	public WeekAdapter getWeekAdapter() {
		return weekAdapter;
	}

	public MonthAdapter getMonthAdapter() {
		return monthAdapter;
	}

	public int getSpecifhDayIndex(int day) {
		return monthAdapter.getSpecifyDayIndex(day);
	}

	public boolean isRightCell(int day) {
		int index = monthAdapter.getSpecifyDayIndex(day) % 7;
		if (index > 3) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��������Ŀؼ���Ϣ
	 * 
	 * @param year
	 * @param month
	 *            (0~11)
	 */
	public void setCalendar(int year, int month) {
		monthAdapter.setCalendar(year, month);
	}

	public void goToPreMonth() {
		monthAdapter.goToPreMonth();
	}

	public void goToNxtMonth() {
		monthAdapter.goToNxtMonth();
	}

	public void goToPreWeek() {
		weekAdapter.goToPreWeek();
	}

	public void goToNxtWeek() {
		weekAdapter.goToNxtWeek();
	}

	public void goToPreDay() {
		dayAdapter.goToPreDay();
	}

	public void goToNxtDay() {
		dayAdapter.goToNxtDay();
	}

	/**
	 * ��ȡ����ĵ�ǰ���
	 * 
	 * @return
	 */
	public int getYear() {
		return monthAdapter.getYear();
	}

	/**
	 * ��ȡ����ĵ�ǰ�·�
	 * 
	 * @return
	 */
	public int getMonth() {
		return monthAdapter.getMonth();
	}

	/**
	 * ��ȡ����ĵ�ǰ��
	 * 
	 * @return
	 */
	public int getDay() {
		return monthAdapter.getDay();
	}

	/**
	 * ��ȡ����ĵ�ǰũ��
	 * 
	 * @return
	 */
	public String getLunar() {
		return monthAdapter.getLunar();
	}

	public String getSolar() {
		return monthAdapter.getSolar();
	}

	public String getMonthTitle() {
		return monthAdapter.getSolar();
	}

	public String getWeekTitle() {
		return weekAdapter.getSolar() + " " + weekAdapter.getWeek();
	}

	public String getDayTitle() {
		return dayAdapter.getSolar();
	}

	public long getFirstDayTimeInMs() {
		Day firstDay = monthAdapter.getDays()[monthAdapter
				.getFirstDayIndexInMonth()];

		Log.i("ishang",
				" +1"
						+ new Date(firstDay.mYear, firstDay.mMonth + 1,
								firstDay.mDay).getTime());

		return new Date(firstDay.mYear - 1900, firstDay.mMonth, firstDay.mDay,
				0, 0, 0).getTime();
	}

	public long getLastDayTimeInMs() {
		Day lastDay = monthAdapter.getDays()[monthAdapter
				.getLastDayIndexInMonth()];

		return new Date(lastDay.mYear - 1900, lastDay.mMonth, lastDay.mDay, 23,
				59, 59).getTime();

	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}
	
	
	public long getFirstDayInWeekInMs() {
		Day firstDay = weekAdapter.getShowDays().get(0);

		return new Date(firstDay.mYear - 1900, firstDay.mMonth, firstDay.mDay+1,
				0, 0, 0).getTime();
	}

	public long getLastDayInWeekInMs() {
		Day lastDay = weekAdapter.getShowDays().get(6);

		return new Date(lastDay.mYear - 1900, lastDay.mMonth, lastDay.mDay+1, 23,
				59, 59).getTime();
	}

	// public long getFirstDayTimeInS(){
	// ArrayList<Day> showDays = monthAdapter.getShowDays();
	// Day firstDay = showDays.get(0);
	// return new Date(firstDay.mYear, firstDay.mMonth,
	// firstDay.mDay,1,1).getTime();
	// }
	// public long getLastDayTimeInS(){
	// ArrayList<Day> showDays = monthAdapter.getShowDays();
	// Day firstDay = showDays.get(0);
	// return new Date(firstDay.mYear, firstDay.mMonth,
	// firstDay.mDay,59,59).getTime();
	// }
	/**
	 * ����ʱ������
	 * 
	 * @return
	 */
	public Day[] getDays() {
		return monthAdapter.getDays();
	}

	/**
	 * �趨������Ϣ
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setFestival(int year, int month, int day) {
		monthAdapter.setFestival(year, month, day);
	}

	/**
	 * �趨��ʱ��Ϣ
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setTimer(int year, int month, int day) {
		monthAdapter.setTimer(year, month, day);
	}

	/**
	 * ֪ͨ��Ϣ����ı䣬ˢ�����
	 */
	public void notifyDataSetChanged() {
	}

	public String getLunarDayString() {
		return dayAdapter.getLunarDayString();
	}

}
