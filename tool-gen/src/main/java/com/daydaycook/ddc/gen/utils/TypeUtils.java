package com.daydaycook.ddc.gen.utils;

import java.math.BigDecimal;
import java.util.Date;

public class TypeUtils {

	public static Class<?> getJavaType(String dataType) {
		if ("bigint".equals(dataType))
			return Long.class;
		if ("datetime".equals(dataType) || "timestamp".equals(dataType) || "time".equals(dataType)
				|| "date".equals(dataType))
			return Date.class;
		if ("int".equals(dataType) || "tinyint".equals(dataType) || "smallint".equals(dataType))
			return Integer.class;
		if ("decimal".equals(dataType) || "double".equals(dataType) || "float".equals(dataType))
			return BigDecimal.class;
		if ("longblob".equals(dataType) || "blob".equals(dataType))
			return Object.class;
		return String.class;
	}
}
