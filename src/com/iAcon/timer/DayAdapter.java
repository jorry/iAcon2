package com.iAcon.timer;

import java.util.Date;

import android.text.format.DateFormat;
import android.util.Log;

public class DayAdapter {
	/** 日历当前时间--年 */
	private int mYear = 0;
	/** 日历当前时间--月 */
	private int mMonth = 0;
	/** 日历当前时间--天 */
	private int mDay = 0;
	/** 当天是本周的第几天（1～7） */
	private int mDayOfWeek = 0;

	/** 非闰年的每个月 */
	private int[] mNonLeapYear = new int[] { 31, 28, 31, 30, 31, 30, 31, 31,
			30, 31, 30, 31 };
	/** 闰年的每个月 */
	private int[] mLeapYear = new int[] { 31, 29, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	public DayAdapter() {
		setToCurrent();
	}

	/**
	 * 设置日历为当天
	 */
	private void setToCurrent() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		mYear = c.get(java.util.Calendar.YEAR);
		mMonth = c.get(java.util.Calendar.MONTH);
		mDay = c.get(java.util.Calendar.DATE);
		mDayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
		mMonthChanged = true;
	}

	public long getDateLongTime() {
		Log.i("bhb", "getDateLongTime() = " + mYear + mMonth + mDay);

		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(mYear, mMonth, mDay);
		return c.getTimeInMillis();
	}

	public String getLunarDayString() {
		return new Lunar(new Date(mYear - 1900, mMonth, mDay))
				.getLunarDayString();
	}

	public String getWeek() {
		switch (mDayOfWeek) {
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		}
		return null;
	}

	public String getSolar() {
		return new StringBuilder().append(
				DateFormat
						.format("MM-dd", new Date(mYear - 1900, mMonth, mDay)))
				.toString();
	}

	private int[] getMonthes(int year) {
		int[] monthes = mNonLeapYear;
		if (isLeapYear(year)) {
			monthes = mLeapYear;
		}
		return monthes;
	}

	public boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		}
		return false;
	}

	private boolean mMonthChanged = false;

	public boolean isMonthChanged() {
		return mMonthChanged;
	}
	
	
	private boolean mWeekChanged = false;

	public boolean isWeekChanged() {
		return mWeekChanged;
	}

	

	public int getDay() {
		return mDay;
	}

	public void goToPreDay() {
		mMonthChanged = false;
		mDay--;
		if (mDay < 1) {
			mMonthChanged = true;
			mMonth--;
			if (mMonth < 0) {
				mMonth = 11;
				mYear--;
			}
			mDay = getMonthes(mYear)[mMonth];
		}

		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(mYear, mMonth, mDay);

		mDayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
	}

	public void goToNxtDay() {
		mMonthChanged = false;
		mDay++;
		if (mDay > getMonthes(mYear)[mMonth]) {
			mMonthChanged = true;
			mMonth++;
			if (mMonth > 11) {
				mMonth = 0;
				mYear++;
			}
			mDay = 1;
		}
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(mYear, mMonth, mDay);
		mDayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
	}

	public long getFirstDayTimeInMs() {
		Log.i("ishang", mYear + "  " + mMonth);
		return new Date(mYear - 1900, mMonth, 1, 0, 0, 0).getTime();
	}

	public long getLastDayTimeInMs() {
		int day = mNonLeapYear[mMonth];
		if (isLeapYear(mDay)) {
			day = mLeapYear[mMonth];
		}
		return new Date(mYear - 1900, mMonth, day, 23, 59, 59).getTime();
	}
}
