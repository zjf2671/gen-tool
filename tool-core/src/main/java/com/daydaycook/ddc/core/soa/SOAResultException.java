package com.daydaycook.ddc.core.soa;

import com.daydaycook.ddc.core.exception.NoStackException;

public class SOAResultException extends NoStackException {
	private static final long serialVersionUID = -1799431867033354737L;
	private static int ERROR_CODE = -10;
	static {

	}

	public SOAResultException(int code, String message){
		super(code, message);
	}

	public SOAResultException(String message){
		super(ERROR_CODE, message);
	}
}
