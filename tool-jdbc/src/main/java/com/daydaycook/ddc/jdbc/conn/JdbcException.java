package com.daydaycook.ddc.jdbc.conn;

/**
 * JdbcException
 * 
 * @author guannan.shang
 * @date 2014-12-26 14:42:01
 * @version 1.0
 */
public class JdbcException extends RuntimeException {

	private static final long serialVersionUID = 980796948194344527L;

	public JdbcException(String message) {
		super(message);
	}

	public JdbcException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public JdbcException(Throwable throwable) {
		super(throwable);
	}
}
