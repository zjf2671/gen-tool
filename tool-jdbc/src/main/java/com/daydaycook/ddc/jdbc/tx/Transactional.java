package com.daydaycook.ddc.jdbc.tx;

import java.sql.Connection;

/**
 * HangTransactional
 * 
 * @author guannan.shang
 * @date 2014-12-26 14:01:24
 * @version 1.0
 */
public class Transactional {
	private Connection connection;
	private Transactional hang;

	/**
	 * 
	 */
	public Transactional(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return Returns the hang.
	 */
	public Transactional getHang() {
		return hang;
	}

	/**
	 * @param hang
	 *            The hang to set.
	 */
	public void setHang(Transactional hang) {
		this.hang = hang;
	}

	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            The connection to set.
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
