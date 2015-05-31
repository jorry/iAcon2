package com.iAcon.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class WeekAdapter {
	/** ������ */
	
	/** ����ǰʱ��--��*/
	private int mYear = 0;
	/** ����ǰʱ��--�� */
	private int mMonth = 0;
	/** ����ǰʱ��--�� */
	private int mDay = 0;
	/** �����Ǳ��ܵĵڼ��죨1��7�� */
	private int mDayOfWeek = 0;
	
	private int mIndex;


	/** �������ÿ���� */
	private int[] mNonLeapYear = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/** �����ÿ���� */
	private int[] mLeapYear = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/** ����Ԫ��Ϣ�б� */
	private Day[] mDays = new Day[42];
	
	private int mWeekNumber = -1;
	
	public Day[] getDays() {
		return mDays;
	}
	
	private ArrayList<Day> mShowDays = new ArrayList<Day>(7);
	
	public ArrayList<Day> getShowDays() {
		return mShowDays;
	}
	
	public boolean isCurrentWeek() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		return (mYear == c.get(java.util.Calendar.YEAR) && mWeekNumber == getCurrentWeekNumber());
	}

	public WeekAdapter() {
		for(int i = 0; i < 42; i++) {
			mDays[i] = new Day();
		}
		// ��������Ϊ����
		setToCurrent();
		mWeekNumber = getCurrentWeekNumber();
	}

	public long getFirstDayInWeekInMs() {
		Day firstDay = getShowDays().get(0);

        return new Date(firstDay.mYear - 1900, firstDay.mMonth, firstDay.mDay, 0, 0, 0).getTime();
	}
	
	public long getLastDayInWeekInMs() {
		Day lastDay = getShowDays().get(6);

		return new Date(lastDay.mYear - 1900, lastDay.mMonth, lastDay.mDay, 23, 59, 59).getTime();
	}
	
	
	public int getCount() {
		return mShowDays.size();
	}

	public Object getItem(int position) {
		return mShowDays.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	
	/**
	 * ��������Ϊ����
	 */
	private void setToCurrent() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		mYear = c.get(java.util.Calendar.YEAR);
		mMonth = c.get(java.util.Calendar.MONTH);
		mDay = c.get(java.util.Calendar.DATE);
		mDayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
		computeDays();
		mIndex = getDayIndex(mDay) / 7;
		refreshWeekData();
		mWeekNumber = getCurrentWeekNumber();
	}

	/**
	 * ��������ʱ��
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void setCalendar(int year, int month, int day) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(year, month, day);
		mYear = c.get(java.util.Calendar.YEAR);
		mMonth = c.get(java.util.Calendar.MONTH);
		mDay = c.get(java.util.Calendar.DATE);
		mDayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
		computeDays();
	}

	
	/**
	 * ��ȡ���ӵ�����
	 * 
	 * @param day
	 * @return
	 */
	private int getDayIndex(int day) {
		for (int i = 0; i < mDays.length; i++) {
			if (mDays[i].mIsInMonth == true && mDays[i].mDay == day) {
				return i;
			}
		}
		return 0;
	}
	
	
	/**
	 * �жϵ�ǰ���Ƿ�������
	 * @return
	 */
	public boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * ��������
	 */
	private void computeDays() {
		int countDayInMonth = getMonthes(mYear)[mMonth];
		
		int dayOfWeek = mDayOfWeek;
		for (int i = mDay - 1; i >= 0; i--) {
			dayOfWeek--;
			if (dayOfWeek == 0) {
				dayOfWeek = 7;
			}
		}
		
		// ����
		int preMonth = mMonth - 1;
		int year = mYear;
		if (preMonth < 0) {
			preMonth = 11;
			year = year - 1;
		}
		
		int preMonthDay = getMonthes(year)[preMonth];
		
		
		for (int i = 0; i < dayOfWeek; i++) {
			mDays[i].mYear = year;
			mDays[i].mMonth = preMonth;
			mDays[i].mDay = preMonthDay - dayOfWeek + i+1;
			mDays[i].mDateInfo1 = String.valueOf(mDays[i].mDay);
			Lunar lunar = new Lunar(new Date(year - 1900, preMonth, mDays[i].mDay));
			mDays[i].mDateInfo2 = lunar.getLunarDayString();
			lunar = null;
			mDays[i].mFestivalFlag = false;
			mDays[i].mTimerFlag = false;
			mDays[i].mIsInMonth = false;
			mDays[i].mIsToday = false;
		}
		
		
		Calendar calendar = Calendar.getInstance();
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH);
		int curDay = calendar.get(Calendar.DATE);
		// ����
		for (int i = dayOfWeek; i < countDayInMonth + dayOfWeek; i++) {
			mDays[i].mYear = mYear;
			mDays[i].mMonth = mMonth;
			mDays[i].mDay = i - dayOfWeek+1;
			mDays[i].mDateInfo1 = String.valueOf(mDays[i].mDay);
			Lunar lunar = new Lunar(new Date(mYear - 1900, mMonth, mDays[i].mDay));
			mDays[i].mDateInfo2 = lunar.getLunarDayString();
			lunar = null;
			
			
			mDays[i].mFestivalFlag = false;
			mDays[i].mTimerFlag = false;
			mDays[i].mIsInMonth = true;
			
			if (mDays[i].mYear == curYear && mDays[i].mMonth == curMonth && mDays[i].mDay == curDay) {
				mDays[i].mIsToday = true;
			} else {
				mDays[i].mIsToday = false;
			}
			
			
		}
		// ����
		int nxtMonth = mMonth + 1;
		year = mYear;
		if (nxtMonth > 11) {
			nxtMonth = 0;
			year = year + 1;
		}
		// ����
		for (int i = countDayInMonth + dayOfWeek ; i < mDays.length; i++) {
			mDays[i].mYear = year;
			mDays[i].mMonth = nxtMonth;
			mDays[i].mDay = i - countDayInMonth - dayOfWeek+1;
			mDays[i].mDateInfo1 = String.valueOf(mDays[i].mDay);
			Lunar lunar = new Lunar(new Date(year - 1900, nxtMonth, mDays[i].mDay));
			mDays[i].mDateInfo2 = lunar.getLunarDayString();
			lunar = null;
			mDays[i].mFestivalFlag = false;
			mDays[i].mTimerFlag = false;
			mDays[i].mIsInMonth = false;
			mDays[i].mIsToday = false;
		}
	}
	
	
	/**
	 * ���ص���һ����
	 */
	private void goToPreMonth() {
		// ������һ����
		mMonth--;
		if (mMonth < 0) {
			mMonth = 11;
			mYear--;
		}
		if (isLeapYear(mYear)) {
			mDay = mLeapYear[mMonth];
		} else {
			mDay = mNonLeapYear[mMonth];
		}
		
		setCalendar(mYear, mMonth, mDay);
	}
	
	/**
	 * ���뵽��һ����
	 */
	private void goToNxtMonth() {
		// ������һ����
		mMonth++;
		if (mMonth > 11) {
			mMonth = 0;
			mYear++;
		}
	
		mDay = 1;
		
		setCalendar(mYear, mMonth, mDay);
	}
	
	public void goToPreWeek() {
		mIndex--;
		if (mIndex == -1) {
			goToPreMonth();
			mIndex = 4;
		}
		refreshWeekData();
	}

	public void goToNxtWeek() {
		mIndex++;
		if (mIndex == 6) {
			goToNxtMonth();
			mIndex = 1;
		}
		refreshWeekData();
	}
	
	private int[] getMonthes(int year) {
		int[] monthes = mNonLeapYear;
		if (isLeapYear(year)) {
			monthes = mLeapYear;
		}
		return monthes;
	}
	
	private void refreshWeekData() {
		mShowDays.clear();
		int begin = mIndex * 7;
		for (int i = begin; i < begin + 7; i++) {
			mShowDays.add(mDays[i]);
		}
	}
	
	
	public String getSolar() {
		Day day = mShowDays.get(mShowDays.size() - 1);
		return new StringBuilder().append(DateFormat.format("yyyy-MM", new Date(day.mYear - 1900, day.mMonth, day.mDay))).toString();
	}
	
	public int getCurrentWeekNumber() {
		Day day = mShowDays.get(mShowDays.size() - 1);
		int[] monthes = getMonthes(day.mYear);
		int count = 0;
		for (int i = 0; i < day.mMonth; i++) {
			count += monthes[i];
		}
		count += day.mDay;
	
		int index = count % 7 == 0 ? count / 7 : count / 7 + 1; 
		Log.i("ishang", day.mMonth+"   "+day.mDay);
		return index;
	}
	
	public String getWeek() {
		return "��" + getCurrentWeekNumber() + "��";
	}

}
