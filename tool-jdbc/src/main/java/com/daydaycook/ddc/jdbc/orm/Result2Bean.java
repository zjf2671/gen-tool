package com.daydaycook.ddc.jdbc.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

/**
 * Result2Bean
 * 
 * @author guannan.shang
 * @date 2014-12-26 10:15:33
 * @version 1.0
 */
public class Result2Bean<T> {

	private static Map<Class<?>, Result2Bean<?>> map = new HashMap<Class<?>, Result2Bean<?>>();

	private String[] columns;
	private Class<T> clazz;
	private Map<String, Class<?>> types;
	private Map<String, Method> setters;

	@SuppressWarnings("unchecked")
	public synchronized static <T> Result2Bean<T> getInstance(Class<T> clazz) {
		Result2Bean<T> r2b = (Result2Bean<T>) map.get(clazz);
		if (r2b == null) {
			r2b = new Result2Bean<T>(clazz);
			r2b.initClass(clazz);
			map.put(clazz, r2b);
			return r2b;
		}
		Result2Bean<T> newResult2Bean = new Result2Bean<T>(clazz);
		newResult2Bean.types = r2b.types;
		newResult2Bean.setters = r2b.setters;
		return r2b;
	}

	@SuppressWarnings("unchecked")
	public T parse(ResultSet rs, int rowNum) throws SQLException {
		try {
			if (columns == null)
				initColumns(rs);
			String getter = getGetter(clazz);
			if (!"getObject".equals(getter) || clazz == Object.class) {
				Method get = ResultSet.class.getMethod(getter, int.class);
				Object value = get.invoke(rs, 1);
				return (T) value;
			}
			T t = clazz.newInstance();
			for (String column : columns)
				parseFiled(rs, t, column);
			return t;
		} catch (Exception e) {
			if (e instanceof SQLException)
				throw (SQLException) e;
			else
				debug("row to " + clazz.getSimpleName() + " error,rowNum =" + rowNum, e);
		}
		return null;
	}

	private void initColumns(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		columns = new String[count];
		for (int i = 0; i < count; i++) {
			columns[i] = rsmd.getColumnLabel(i + 1);
			if (columns[i] != null) // getColumnName返回的应该是大写的,但是再执行一下转大写总是更加保险,损失少量性能
				columns[i] = columns[i].toUpperCase();
		}
	}

	/**
	 * @param string
	 * @param e
	 */
	private void debug(String msg, Throwable e) {
		System.out.println(msg);
		System.out.println(e.getMessage());
	}

	private void parseFiled(ResultSet rs, T t, String column) {
		try {
			if (!types.containsKey(column))
				return;
			String getter = getGetter(types.get(column));
			Method get = ResultSet.class.getMethod(getter, String.class);
			Object value = get.invoke(rs, column);
			if (value != null)
				setters.get(column).invoke(t, value);
		} catch (Exception e) {
			debug("column name = " + column, e);
		}
	}

	private Result2Bean(Class<T> clazz){
		this.clazz = clazz;
		setters = new HashMap<String, Method>();
		types = new HashMap<String, Class<?>>();
	}

	public void initClass(Class<? extends Object> clazz) {
		if (isPrimitive(clazz))
			return;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String setterName = "set" + firstCharUpperCase(field.getName());
			try {
				String columnAlias = null;
				Column column = field.getAnnotation(Column.class);
				if (column != null)
					columnAlias = column.name();
				if (columnAlias == null || "".equals(columnAlias))
					columnAlias = parse2Underscore(field.getName());
				if (setters.containsKey(columnAlias))
					continue;// 父类同名属性不要覆盖子类
				Method setter = clazz.getMethod(setterName, field.getType());
				put(columnAlias, field.getName(), setter, field.getType());
			} catch (Exception e) {
			}
		}
		if (clazz.getSuperclass() != Object.class)
			initClass(clazz.getSuperclass());
	}

	/**
	 * @param columnAlias
	 * @param setter
	 * @param type
	 */
	private void put(String columnAlias, String fieldName, Method setter, Class<?> type) {

		columnAlias = columnAlias.toUpperCase();
		setters.put(columnAlias, setter);
		types.put(columnAlias, type);

		columnAlias = fieldName.toUpperCase();
		setters.put(columnAlias, setter);
		types.put(columnAlias, type);
	}

	/**
	 * 判断是否是Object类型，后者是单一对象(例如：Date Number)等
	 * 
	 * @param type
	 * @return
	 */
	private boolean isPrimitive(Class<? extends Object> type) {
		if (type == Object.class)
			return true;
		if (!"getObject".equals(getGetter(type)))
			return true;
		return false;
	}

	/**
	 * 根据数据类型获取resultset的get方法
	 * 
	 * @param type
	 * @return
	 */
	private static String getGetter(Class<?> type) {
		if ((type == long.class) || (type == Long.class)) {
			return "getLong";
		} else if ((type == int.class) || (type == Integer.class)) {
			return "getInt";
		} else if ((type == char.class) || (type == Character.class)) {
			return "getString";
		} else if ((type == short.class) || (type == Short.class)) {
			return "getShort";
		} else if ((type == double.class) || (type == Double.class)) {
			return "getDouble";
		} else if ((type == float.class) || (type == Float.class)) {
			return "getFloat";
		} else if ((type == boolean.class) || (type == Boolean.class)) {
			return "getBoolean";
		} else if (type == String.class) {
			return "getString";
		} else if (type == java.math.BigDecimal.class) {
			return "getBigDecimal";
		} else if (type == java.sql.Date.class) {
			return "getDate";
		} else if (type == java.sql.Timestamp.class || type == java.util.Date.class) {
			return "getTimestamp";
		}
		return "getObject";
	}

	public static String firstCharUpperCase(String str) {
		char[] cs = str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	public static String parse2Underscore(String str) {
		char[] chars = str.toCharArray();
		StringBuffer s = new StringBuffer();
		boolean isFirst = true;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] >= 65 && chars[i] <= 90) {
				if (!isFirst)
					s.append('_');
				s.append(chars[i]);
			} else if (chars[i] >= 97 && chars[i] <= 122) {
				s.append((char) (chars[i] - 32));
			} else {
				s.append(chars[i]);
			}
			isFirst = false;
		}
		return s.toString();
	}
}
