package com.yuqincar.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

	private static final SimpleDateFormat YMD = new SimpleDateFormat(
			"yyyy-MM-dd");

    private static final SimpleDateFormat YMD_ = new SimpleDateFormat("yyyy/MM/dd");

	private static final SimpleDateFormat ChineseYMD = new SimpleDateFormat(
			"yyyy 年 MM 月 dd 日");

	private static final SimpleDateFormat ChineseYM = new SimpleDateFormat(
			"yyyy 年 MM 月");

	private static final SimpleDateFormat MD = new SimpleDateFormat("MM-dd");

	private static final SimpleDateFormat YMDHMS = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat YMDHM = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	private static final SimpleDateFormat SNDate = new SimpleDateFormat(
			"yyMMdd");

	private static final SimpleDateFormat YYYY = new SimpleDateFormat("yyyy");

	private static final SimpleDateFormat HMS = new SimpleDateFormat("HH:mm:ss");

	private static final SimpleDateFormat HM = new SimpleDateFormat("HH:mm");

	/**
	 * 将形如"yyyy-MM-dd"的字符串转换为Date
	 * @param s
	 * @return
	 */
	public static Date getYMD(String s) {
		try {
			return YMD.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 将形如"yyyy/MM/dd"的字符串转换为Date
     * @param s
     * @return
     */
    public static Date getYMD2(String s) {
        try {
            return YMD_.parse(s);
        } catch (Exception e) {
			e.printStackTrace();
            return null;
        }
    }

	/**
	 * 将Date转换为形如"yyyy-MM-dd"的字符串
	 * @param date
	 * @return
	 */
	public static String getYMDString(Date date) {
		if (date != null)
			return YMD.format(date);
		else
			return "";
	}

    /**
     * 将Date转换为形如"yyyy/MM/dd"的字符串
     * @param date
     * @return
     */
    public static String getYMDString2(Date date) {
        if (date != null)
            return YMD_.format(date);
        else
            return "";
    }

	/**
	 * 获取Date中的年份字符串"yyyy"
	 * @param date
	 * @return
	 */
	public static String getYearString(Date date) {
		if (date != null)
			return YYYY.format(date);
		else
			return "";
	}

	/**
	 * 将形如"yyyy"的字符串转换为Date
	 * @param year
	 * @return
	 */
	public static Date getYearDate(String year) {
		try {
			return YYYY.parse(year);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将Date转换为形如"yyyy 年 MM 月 dd 日"的字符串
	 * @param date
	 * @return
	 */
	public static String getChineseYMDString(Date date) {
		if (date != null)
			return ChineseYMD.format(date);
		else
			return "";
	}
	
	/**
	 * 将形如"yyyy 年 MM 月 dd 日"的字符串转换为Date
	 * @param date
	 * @return
	 */
	public static String getChineseYMString(Date date) {
		if (date != null)
			return ChineseYM.format(date);
		else
			return "";
	}

	/**
	 * 将Date转换为形如"MM-dd"的字符串
	 * @param date
	 * @return
	 */
	public static String getMDString(Date date) {
		if (date != null)
			return MD.format(date);
		else
			return "";
	}

	/**
	 * 将形如"yyyy-MM-dd HH:mm:ss"的字符串转换为Date
	 * @param s
	 * @return
	 */
	public static Date getYMDHMS(String s) {
		try {
			return YMDHMS.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将形如"yyyy-MM-dd HH:mm"的字符串转换为Date
	 * @param s
	 * @return
	 */
	public static Date getYMDHM(String s) {
		try {
			return YMDHM.parse(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将Date转换为形如"yyyy-MM-dd HH:mm:ss"的字符串
	 * @param date
	 * @return
	 */
	public static String getYMDHMSString(Date date) {
		if (date != null)
			return YMDHMS.format(date);
		else
			return "";
	}

	/**
	 * 将Date转换为形如"yyyy-MM-dd HH:mm"的字符串
	 * @param date
	 * @return
	 */
	public static String getYMDHMString(Date date) {
		if (date != null)
			return YMDHM.format(date);
		else
			return "";
	}
	
	/**
	 * 将Date转换为形如"yyMMdd"的字符串
	 * @param date
	 * @return
	 */
	public static String getSNDateString(Date date) {
		if (date != null)
			return SNDate.format(date);
		else
			return "";
	}

	/**
	 * 将Date转换为形如"HH:mm:ss"的字符串
	 * @param date
	 * @return
	 */
	public static String getHMSString(Date date) {
		if (date != null)
			return HMS.format(date);
		else
			return "";
	}
	
	/**
	 * 将Date转换为形如"HH:mm"的字符串
	 * @param date
	 * @return
	 */
	public static String getHMString(Date date) {
		if (date != null)
			return HM.format(date);
		else
			return "";
	}

	/**
	 * 得到某年的日期范围,从1月1日0时0分0秒 到 12月31日23时59分59秒
	 * @param year
	 * @return
	 */
	public static ValueRange getYearValueRange(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
		Date start = calendar.getTime();
		calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		Date end = calendar.getTime();
		return new ValueRange(start, end);
	}

	/**
	 * 将Date的时、分、秒、毫秒都置零
	 * @param date
	 * @return
	 */
	public static Date getMinDate(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime();
		} else
			return null;
	}
	
	/**
	 * 将Date的时、分、秒、毫秒都置为当天结束前的状态
	 * @param date
	 * @return
	 */
	public static Date getMaxDate(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			// 不设定为999的原因是数据库返回的是下一天的开始
			calendar.set(Calendar.MILLISECOND, 998);
			return calendar.getTime();
		} else
			return null;
	}

	/**
	 * 得到Date所表示月的第一天Date
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getMinDate(calendar.getTime());
	}

	/**
	 * 得到Date所表示月的最后一天Date
	 * @param date
	 * @return
	 */
	public static Date getEndDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getMaxDate(calendar.getTime());
	}

	/**
	 * 得到某年某月第一天的Date
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDateOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getMinDate(calendar.getTime());
	}

	/**
	 * 得到某年某月最后一天的Date
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getEndDateOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getMaxDate(calendar.getTime());
	}

	/**
	 * 得到某年的第一天Date
	 * @param year
	 * @return
	 */
	public static Date getFirstDateOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getMinDate(calendar.getTime());
	}

	/**
	 * 得到某年的最后一天Date
	 * @param year
	 * @return
	 */
	public static Date getEndDateOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		return getMaxDate(calendar.getTime());
	}

	/**
	 * 得到某年12个月的Date列表，其中每月的Date设为第一天
	 * @param year
	 * @return
	 */
	public static List<DateRange> getMonthesDateRange(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		List<DateRange> list = new ArrayList<DateRange>();
		for (int i = 1; i <= 12; i++) {
			Date fromDate = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date toDate = calendar.getTime();
			list.add(new DateRange(fromDate, toDate));

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}
		return list;
	}
	
	/**
	 * 计算两个Date之间包含的天数，如果end比begin还要早，则返回负数。
	 * saturdayConsidered为真时，星期六应该算作一天；
	 * sundayConsidered为真时，星期天应该算作一天。
	 * @param begin
	 * @param end
	 * @param saturdayConsidered
	 * @param sundayConsidered
	 * @return
	 */
	public static int elapseDays(Date begin, Date end,
			boolean saturdayConsidered, boolean sundayConsidered) {
		int sign = 1;
		Date startDate = getMinDate(begin);
		Date finishDate = getMinDate(end);
		if (startDate.after(finishDate)) {
			sign = -1;
			startDate = getMinDate(end);
			finishDate = getMinDate(begin);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int n = 0;
		while (!calendar.getTime().after(finishDate)) {
			if ((calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar
					.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) // 如果不是星期六，也不是星期天，就计算一天
				n++;
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY // 如果是星期六，并且星期六应该算作一天，就计算一天
					&& saturdayConsidered)
				n++;
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY // 如果是星期天，并且星期天应该算作一天，就计算一天
					&& sundayConsidered)
				n++;

			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return sign * n;
	}

	/**
	 * 根据开始日期和跨越天数，计算结束日期，时分秒毫秒与开始日期相同。
	 * saturdayConsidered为真时，星期六应该算作一天；
	 * sundayConsidered为真时，星期天应该算作一天。
	 * @param begin
	 * @param duration
	 * @param saturdayConsidered
	 * @param sundayConsidered
	 * @return
	 */
	public static Date getEndDate(Date begin, int duration,
			boolean saturdayConsidered, boolean sundayConsidered) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(begin);
		int n = 0;
		while (n < duration) {
			if ((calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar
					.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) // 如果不是星期六，也不是星期天，就计算一天
				n++;
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY // 如果是星期六，并且星期六应该算作一天，就计算一天
					&& saturdayConsidered)
				n++;
			else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY // 如果是星期天，并且星期天应该算作一天，就计算一天
					&& sundayConsidered)
				n++;

			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 得到从现在开始偏移多少天的Date
	 * @param offsetDays
	 * @return
	 */
	public static Date getOffsetDateFromNow(int offsetDays) {
		return getOffsetDate(new Date(), offsetDays);
	}

	/**
	 * 得到从date开始偏移多少天的Date
	 * @param offsetDays
	 * @return
	 */
	public static Date getOffsetDate(Date date, int offsetDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, offsetDays);
		return calendar.getTime();
	}

	/**
	 * 得到某个月的时间跨度DateRange
	 * @param date
	 * @return
	 */
	public static DateRange getMonthDateRange(Date date) {
		Date lowDate, highDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		lowDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		highDate = calendar.getTime();
		return new DateRange(lowDate, highDate);
	}

	/**
	 * 得到dateRange跨度内的月份DateRange
	 * @param dateRange
	 * @return
	 */
	public static List<DateRange> getMonthDateRange(DateRange dateRange) {
		List<DateRange> dateRangeList = new ArrayList<DateRange>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateRange.getLowDate());

		Date pointDate = calendar.getTime();
		while (dateRange.isCover(pointDate)) {
			dateRangeList.add(getMonthDateRange(pointDate));
			calendar.add(Calendar.MONTH, 1);
			pointDate = calendar.getTime();
		}
		return dateRangeList;
	}

	/**
	 * 得到date所在年的DateRange
	 * @param date
	 * @return
	 */
	public static DateRange getYearDateRange(Date date) {
		Calendar cal = Calendar.getInstance();
		Date lowDate, highDate;
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		lowDate = cal.getTime();
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		highDate = cal.getTime();
		return new DateRange(lowDate, highDate, true, true);
	}
	
	/**
	 * 比较两个日期的年月日是否相等，即比较两个日期是否是在同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareYMD(Date date1, Date date2) {
		int year1,month1,day1;
		int year2,month2,day2;
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date1);
		year1=cal.get(Calendar.YEAR);
		month1=cal.get(Calendar.MONTH);
		day1=cal.get(Calendar.DAY_OF_MONTH);
		
		cal.setTime(date2);
		year2=cal.get(Calendar.YEAR);
		month2=cal.get(Calendar.MONTH);
		day2=cal.get(Calendar.DAY_OF_MONTH);
		
		return year1==year2 && month1==month2 && day1==day2;
	}

    /**
     * 比较日期大小
     * 
     * @param fromDate
     * @param endDate
     * @return
     */
    public static int compareDate(Date fromDate, Date endDate) {
        Calendar fromCalender = Calendar.getInstance();
        fromCalender.setTime(fromDate);
        Calendar toCalender = Calendar.getInstance();
        toCalender.setTime(endDate);
        return fromCalender.compareTo(toCalender);
    }
    
    /** 
     * 计算两个日期之间相差的天数 
     * @param date1 
     * @param date2 
     * @return 
     */  
    public static int daysBetween(Date date1,Date date2)  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date1);  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(date2);  
        long time2 = cal.getTimeInMillis();       
        long between_days=(time2-time1)/(1000*3600*24);  
          
       return Integer.parseInt(String.valueOf(between_days));         
    } 
        
    /**
     * 根据指定日期获取所在周的星期一和星期日
     */
   public static DateRange getWeek(Date date){
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(date);
		
		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);   //获得当前日期是一个星期的第几天
		if(1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
       }
		
		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       Date fromDate=cal.getTime();
       cal.add(Calendar.DATE, 6);         
       Date toDate=cal.getTime();
       return new DateRange(fromDate,toDate);
   }
   
   public static float elapseHours(Date begin, Date end){
	   long step=end.getTime()-begin.getTime();
	   return step/(1000*60*60.0f);
   }
}
