package com.daydaycook.ddc.utils.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateFormatPool {
	public static final Map<String, ThreadLocal<DateFormat>> DATE_FORMAT_POOL = new HashMap<String, ThreadLocal<DateFormat>>();

	static DateFormat getDateFormat(String format) {
		ThreadLocal<DateFormat> threadLocal = DATE_FORMAT_POOL.get(format);
		if (threadLocal == null)
			threadLocal = initThreadLocal(format);
		return threadLocal.get();
	}

	private static synchronized ThreadLocal<DateFormat> initThreadLocal(final String format) {
		ThreadLocal<DateFormat> threadLocal = DATE_FORMAT_POOL.get(format);
		if (threadLocal == null) {
			threadLocal = new ThreadLocal<DateFormat>() {
				@Override
				protected DateFormat initialValue() {
					return new SimpleDateFormat(format);
				}
			};
			DATE_FORMAT_POOL.put(format, threadLocal);
		}
		return threadLocal;
	}
}
