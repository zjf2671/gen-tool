package com.daydaycook.ddc.core.web;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {
	private static final long serialVersionUID = -1902455525299606460L;
	private int code = 0;
	private String error;
	private String success;
	private T data;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
