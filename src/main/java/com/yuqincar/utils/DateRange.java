package com.yuqincar.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * <code>DateRange</code>是在ValueRange下针对<code>ValueRange</code>的一个封装。
 */
public class DateRange extends ValueRange {

	private static final Date MIN_DATE = str2Date("1800-01-01");
	private static final Date MAX_DATE = str2Date("3000-01-01");

	public DateRange(Date low, Date high) {
		this(low, high, true, true);
	}

	public DateRange(Date low, Date high, boolean clearable) {
		this(low, high, clearable, true);
	}

	public DateRange(String str) {
		super(new Date(), new Date());
		String s[]=str.split("-DR-");
		setLowValue(clearTime(DateUtils.getYMD(s[0]))); 
		setHighValue(clearTime(DateUtils.getYMD(s[1]))); 
	}

	public DateRange(Date low, Date high, boolean clearable, boolean maxable) {
		super(low == null ? clearTime(MIN_DATE) : clearable ? clearTime(low)
				: low, high == null ? setMaxTime(MAX_DATE)
				: maxable ? setMaxTime(high) : high);
	}

	private static Date clearTime(Date date) {
		return DateUtils.getMinDate(date);
	}

	private static Date setMaxTime(Date date) {
		return DateUtils.getMaxDate(date);
	}

	public Date getLowDate() {
		return (Date) getLowValue();
	}

	public Date getHighDate() {
		return (Date) getHighValue();
	}

	public boolean isCover(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(getLowDate());
		long lowDateLong = calendar.getTimeInMillis();

		calendar.setTime(date);
		long dateLong = calendar.getTimeInMillis();

		calendar.setTime(getHighDate());
		long highDateLong = calendar.getTimeInMillis();

		return lowDateLong <= dateLong && dateLong <= highDateLong;
	}

	public String toString() {
		return DateUtils.getYMDString(getLowDate()) + "-DR-"
				+ DateUtils.getYMDString(getHighDate());
	}

	public String toUserString(){
		return DateUtils.getYMDString(getLowDate()) + " 到 "
				+ DateUtils.getYMDString(getHighDate());
	}
}
