package com.daydaycook.ddc.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateUtils
 * 
 * @author guannan.shang
 * @date 2015-2-3 17:00:01
 * @version 1.0
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取 昨天的日期, 0时0分0秒
	 * 
	 * @date 2016-07-06 16:57:39
	 * @author wenpin.hao
	 * @return
	 */
	public static Date getYesterdayDate() {
		return getSomedayStart(-1);
	}

	/**
	 * 
	 * 获取指定偏移量的那天的日期, 0时0分0秒
	 * 
	 * @date 2016-07-08 16:57:39
	 * @author guannan.shang
	 * @param offset
	 *            指定的偏移量，-1代表昨天，1代表明天
	 * @return
	 */
	public static Date getSomedayStart(int offset) {
		Calendar calendar = getCalendar(null);
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		return toDayStart(calendar).getTime();
	}

	/**
	 * 获取今天的日期，今天的0时0分0秒
	 * 
	 * @author guannan.shang
	 * @return
	 */
	public static Date getTodayDate() {
		return toDayStart(getCalendar(null)).getTime();
	}

	/**
	 * 获取指定日期当天的开始 0时0分0秒0毫秒
	 * 
	 * @param date
	 * @author guannan.shang
	 * @return
	 */
	public static Date toDayStart(Date date) {
		return toDayStart(getCalendar(date)).getTime();
	}

	/**
	 * 根据date 获取日历对象
	 * 
	 * @param date
	 *            为null 时认为是当前时间
	 * @author guannan.shang
	 * @return
	 */
	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null)
			cal.setTime(date);
		return cal;
	}

	/**
	 * 将日历设置到当天的开始 0时0分0秒0毫秒
	 * 
	 * @param cal
	 * @author guannan.shang
	 * @return
	 */
	public static Calendar toDayStart(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @author guannan.shang
	 * @return date2-date1
	 */
	public static int betweenDays(Date date1, Date date2) {
		long l1 = toDayStart(date1).getTime();
		long l2 = toDayStart(date2).getTime();
		long between = (l2 - l1) / 86400000;
		return (int) between;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @author guannan.shang
	 * @return date2 - date1 的天数
	 */
	public static int betweenDays(String date1, String date2) throws ParseException {
		DateFormat df = DateFormatPool.getDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(date1);
		Date d2 = df.parse(date2);
		return betweenDays(d1, d2);
	}

	/**
	 * 获取默认格式的当前日期,年月日
	 * 
	 * @return
	 */
	public static String getDefaultDateNow() {
		return format(DATE_FORMAT, new Date());
	}

	/**
	 * 获取默认格式的当前时间,时分秒
	 * 
	 * @return
	 */
	public static String getDefaultTimeNow() {
		return format(TIME_FORMAT, new Date());
	}

	/**
	 * 获取默认格式的当前日期和时间,年月日 时分秒
	 * 
	 * @return
	 */
	public static String getDefaultDateTimeNow() {
		return format(DATE_TIME_FORMAT, new Date());
	}

	/**
	 * 获取默认格式的日期,年月日
	 * 
	 * @param date
	 * @return
	 */
	public static String getDefaultDate(Date date) {
		return format(DATE_FORMAT, date);
	}

	/**
	 * 获取默认格式的时间,时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String getDefaultTime(Date date) {
		return format(TIME_FORMAT, date);
	}

	/**
	 * 获取默认格式的日期时间,年月日 时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String getDefaultDateTime(Date date) {
		return format(DATE_TIME_FORMAT, date);
	}

	public static String format(String format, Date date) {
		DateFormat sdf = DateFormatPool.getDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 转化日期或者时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date) {
		try {
			String[] parsePatterns = { DATE_TIME_FORMAT, DATE_FORMAT };
			return parseDate(date, parsePatterns);
		} catch (ParseException e) {
			logger.error("转化日期错误[输入：" + date + "]", e);
		}
		return null;
	}
}
