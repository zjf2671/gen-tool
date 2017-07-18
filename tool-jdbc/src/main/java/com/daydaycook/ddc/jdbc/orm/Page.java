package com.daydaycook.ddc.jdbc.orm;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Page
 * 
 * @author guannan.shang
 * @date 2014-12-29 17:21:17
 * @version 1.0
 */
public class Page<E> {

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private List<E> rows;
	private Integer total;

	public Page() {
	}

	public Page(List<E> rows, Integer total) {
		this.rows = rows;
		this.total = total;
	}

	public List<E> getRows() {
		return rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String toJson(String... property) {
		StringBuffer json = new StringBuffer();
		json.append("{\"total\":");
		json.append(getTotal());
		json.append(",\"rows\":[");
		boolean first = true;
		for (Object obj : getRows()) {
			if (first)
				json.append("{");
			else
				json.append(",{");
			for (int i = 0; i < property.length; i++) {
				String key = property[i];
				if (i > 0)
					json.append(",");
				json.append("\"");
				json.append(key);
				json.append("\":\"");
				json.append(getValue(obj, key));
				json.append("\"");
			}
			json.append("}");
			first = false;
		}
		json.append("]}");
		return json.toString();
	}

	/**
	 * @param obj
	 * @param key
	 * @return
	 */
	private String getValue(Object obj, String key) {
		String getter = getGetter(key);
		try {
			Method method = obj.getClass().getMethod(getter);
			Object value = method.invoke(obj);
			if (value == null)
				return "";
			if (value instanceof Date)
				return new SimpleDateFormat(dateFormat).format((Date) value);
			return value.toString();
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * @param key
	 * @return
	 */
	private static String getGetter(String key) {
		char[] cs = key.toCharArray();
		cs[0] -= 32;
		String getter = String.valueOf(cs);
		return "get" + getter;
	}

	/**
	 * @param 设置日期类型的格式
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
