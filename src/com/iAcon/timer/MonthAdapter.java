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


/**
 * 日历的单元控件适配器
 * 
 * @author chenhailin
 *
 */
public class MonthAdapter {
	/** 日历当前时间--年*/
	private int mYear = 0;
	/** 日历当前时间--月 */
	private int mMonth = 0;
	/** 日历当前时间--天 */
	private int mDay = 0;
	/** 当天是本周的第几天（1～7） */
	private int mDayOfWeek = 0;
	/** 索引 */
	private int index = 0;
	
	public int getIndex() {
		return index;
	}


	/** 非闰年的每个月 */
	private int[] mNonLeapYear = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/** 闰年的每个月 */
	private int[] mLeapYear = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/** 日历单元信息列表 */
	private Day[] mDays = new Day[42];
	
	private ArrayList<Day> mShowDays = new ArrayList<Day>(42);
	
	
	public ArrayList<Day> getShowDays() {
		return mShowDays;
	}
	
	public int getDaysInMonth() {
		if (isLeapYear(mYear)) {
			return mLeapYear[mMonth];
		}
		return mNonLeapYear[mMonth];
	}

	public MonthAdapter() {
		for(int i = 0; i < 42; i++) {
			mDays[i] = new Day();
		}
		// 设置日历为当天
		setToCurrent();
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

//	public View getView(int position, View convertView, ViewGroup parent) {
//		Day day = mShowDays.get(position);
//		DayView dayView = null;
//		if (convertView == null) {
//			dayView = new DayView(context);
//		} else {
//			dayView = (DayView)convertView;
//		}
//
//		if (position % 7 == 0 || position % 7 == 6) {
//			dayView.setWeekendColor();
//		} else {
//			dayView.setWorkdayColor();
//		}
//		dayView.setMonthFlag(false);
//		dayView.setTodayFlag(false);
//		dayView.setSelected(false);
//		dayView.setEventTypes(null);
//		if (day.mIsInMonth) {
//			dayView.setMonthFlag(true);
//			dayView.setDateInfo1(day.mDateInfo1);
//			dayView.setDateInfo2(day.mDateInfo2);
//			dayView.setBackgroundResource(R.drawable.calendar_cell);
//			
//			if (day.mIsToday) {
//				dayView.setTodayFlag(true);
//			} else {
//				dayView.setTodayFlag(false);
//			}
//			
//			if (day.mSelected) {
//				dayView.setSelected(true);
//			}
//			dayView.setEventTypes(day.mTypes);
//		} else {
//			dayView.setDateInfo1("");
//			dayView.setDateInfo2("");
//		}
//		dayView.setTag(day);
//		
//		return dayView;
//	}
	
	public boolean isCurrentMonth() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		int year = c.get(java.util.Calendar.YEAR);
		int month = c.get(java.util.Calendar.MONTH);
		
		return (mYear == year && mMonth == month);
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
		computeDays();
		index = getDayIndex(mDay);
	}

	/**
	 * 设置日历时间
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
		index = getDayIndex(mDay);
Log.i("calendar", "Calendar...getTime = "+c.getTimeInMillis());		
	}
	
	
	/**
	 * 设定节日信息
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setFestival(int year, int month, int day) {
		if (year == mYear && month == mMonth) {
			for (int i = 0; i < mDays.length; i++) {
				if (mDays[i].mDay == day) {
					mDays[i].mFestivalFlag = true;
					break;
				}
			}
		}
	}
	
	
	/**
	 * 设定定时信息
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setTimer(int year, int month, int day) {
		if (year == mYear && month == mMonth) {
			for (int i = 0; i < mDays.length; i++) {
				if (mDays[i].mIsInMonth && mDays[i].mDay == day) {
//					System.out.println("" + year + ":" + month + ":" + day);
					mDays[i].mTimerFlag = true;
					break;
				}
			}
		}
	}
	
	/**
	 * 返回时间数组
	 * 
	 * @return
	 */
	public Day[] getDays() {
		return mDays;
	}

	
	/**
	 * 获取日子的索引
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
	 * 返回本月第一天的索引
	 * 
	 * @return
	 */
	public int getFirstDayIndexInMonth() {
		for (int i = 0; i < mDays.length; i++) {
			if (mDays[i].mIsInMonth == true) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 返回本月最后一天的索引
	 * 
	 * @return
	 */
	public int getLastDayIndexInMonth() {
		for (int i = mDays.length - 1; i >= 0; i--) {
			if (mDays[i].mIsInMonth == true) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 判断当前年是否是闰年
	 * @return
	 */
	public boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 计算日期
	 */
	private void computeDays() {
		mShowDays.clear();
		
		int countDayInMonth = 0;
		if (isLeapYear(mYear)) {
			countDayInMonth = mLeapYear[mMonth];
		} else {
			countDayInMonth = mNonLeapYear[mMonth];
		}
		
		int dayOfWeek = mDayOfWeek;
		for (int i = mDay - 1; i >= 0; i--) {
			dayOfWeek--;
			if (dayOfWeek == 0) {
				dayOfWeek = 7;
			}
		}
		
		// 上月
		int preMonth = mMonth - 1;
		int year = mYear;
		if (preMonth < 0) {
			preMonth = 11;
			year = year - 1;
		}
		int preMonthDay = 0;
		if (isLeapYear(year)) {
			preMonthDay = mLeapYear[preMonth];
		} else {
			preMonthDay = mNonLeapYear[preMonth];
		}
		
		
		for (int i = 0; i < dayOfWeek; i++) {
			mDays[i].mYear = year;
			mDays[i].mMonth = preMonth;
			mDays[i].mDay = preMonthDay - dayOfWeek + i + 1;
			mDays[i].mDateInfo1 = String.valueOf(mDays[i].mDay);
			mDays[i].mFestivalFlag = false;
			mDays[i].mTimerFlag = false;
			mDays[i].mIsInMonth = false;
			mDays[i].mIsToday = false;
			mDays[i].mSelected = false;
			mDays[i].mTypes = null;
		}
		
		
		Calendar calendar = Calendar.getInstance();
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH);
		int curDay = calendar.get(Calendar.DATE);
		// 本月
		for (int i = dayOfWeek; i < countDayInMonth + dayOfWeek; i++) {
			mDays[i].mYear = mYear;
			mDays[i].mMonth = mMonth;
			mDays[i].mDay = i - dayOfWeek + 1;
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
			
			mDays[i].mSelected = false;
			mDays[i].mTypes = null;
		}
		// 下月
		int nxtMonth = mMonth + 1;
		year = mYear;
		if (nxtMonth > 11) {
			nxtMonth = 0;
			year = year + 1;
		}
		for (int i = countDayInMonth + dayOfWeek ; i < mDays.length; i++) {
			mDays[i].mYear = year;
			mDays[i].mMonth = nxtMonth;
			mDays[i].mDay = i - countDayInMonth - dayOfWeek + 1;
			mDays[i].mDateInfo1 = String.valueOf(mDays[i].mDay);
			mDays[i].mFestivalFlag = false;
			mDays[i].mTimerFlag = false;
			mDays[i].mIsInMonth = false;
			mDays[i].mIsToday = false;
			mDays[i].mSelected = false;
			mDays[i].mTypes = null;
		}
		int showIndex = 0;
		int emptyCount = dayOfWeek;
		if (dayOfWeek == 7) {
			showIndex = 7;
			emptyCount = 0;
		}
		
		int mod = (emptyCount + countDayInMonth) % 7;
		if (mod != 0) {
			emptyCount += 7 - mod;
		}
		
		for (int i = showIndex; i < showIndex + countDayInMonth + emptyCount; i++) {
			mShowDays.add(mDays[i]);
		}
	}
	
	
	
	
	/**
	 * 设置日历时间
	 * 
	 * @param year
	 * @param month(0~11)
	 */
	public void setCalendar(int year, int month) {
		if (mYear == year && mMonth == month) {
			return;
		}
		
		mYear = year;
		mMonth = month;
		mDay = 1;
		setCalendar(mYear, mMonth, mDay);
	}
	
	
	/**
	 * 返回到上一个月
	 */
	public void goToPreMonth() {
		// 返回上一个月
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
	 * 进入到下一个月
	 */
	public void goToNxtMonth() {
		// 进入下一个月
		mMonth++;
		if (mMonth > 11) {
			mMonth = 0;
			mYear++;
		}
	
		mDay = 1;
		
		setCalendar(mYear, mMonth, mDay);
	}
	
	/**
	 * 获取日历的当前年份
	 * 
	 * @return
	 */
	public int getYear() {
		return mYear;
	}
	
	/**
	 * 获取日历的当前月份
	 * 
	 * @return
	 */
	public int getMonth() {
		return mMonth;
	}
	
	/**
	 * 获取日历的当前天
	 * 
	 * @return
	 */
	public int getDay() {
		return mDay;
	}
	
	/**
	 * 获取日历的当前农历
	 * 
	 * @return
	 */
	public String getLunar() {
		return new Lunar(new Date(mYear - 1900, mMonth, mDay)).getLunarYearString();
	}
	
	public String getSolar() {
		return new StringBuilder().append(DateFormat.format("yyyy-MM", new Date(mYear - 1900, mMonth, mDay))).toString();
	}
	
	
	/**
	 * 通过索引检测并可能修改日期
	 * 
	 * @param index
	 */
	public boolean checkMayChangeDayByIndex(int index) {
		int firstDayIndex = getFirstDayIndexInMonth();
		if (index < firstDayIndex) {
			// 返回上一个月
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
			
			return true;	
		}
		int lastDayInde = getLastDayIndexInMonth();
		if (index > lastDayInde) {
			// 进入下一个月
			mMonth++;
			if (mMonth > 11) {
				mMonth = 0;
				mYear++;
			}
		
			mDay = 1;
		
			setCalendar(mYear, mMonth, mDay);
			return true;
		}
		
		return false;
	}

	
	/**
	 * 获取指定天数的下标
	 * 
	 * @param day
	 * @return
	 */
	public int getSpecifyDayIndex(int day) {
		for (int i = 0; i < mDays.length; i++) {
			Day d = mDays[i];
			if (d.mIsInMonth) {
				if (d.mDay == day) {
					return i;
				}
			}
		}
		return 0;
	}

}
