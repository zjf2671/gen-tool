package com.daydaycook.ddc.core.exception;

/**
 * 
 * @author harry.zhang
 * @since 1.0
 * @version 1.0
 */
public class IllegalParameterException extends NoStackException {
	private static final long serialVersionUID = 6016149363552889511L;

	public IllegalParameterException(String message) {
		super(-1, message);
	}

	public IllegalParameterException(int code, String message) {
		super(code, message);
	}
}
