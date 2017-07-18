package com.daydaycook.ddc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.daydaycook.ddc.jdbc.conn.JDBCManager;
import com.daydaycook.ddc.jdbc.orm.NullValue;
import com.daydaycook.ddc.jdbc.orm.Page;
import com.daydaycook.ddc.jdbc.orm.Result2Bean;
import com.daydaycook.ddc.jdbc.tx.Transactional;

/**
 * Jdbc
 * 
 * @author guannan.shang
 * @date 2014-12-25 18:35:50
 * @version 1.0
 */
public class Jdbc {
	private JDBCManager jdbcManager;
	private ThreadLocal<Transactional> transactional;

	public Jdbc(JDBCManager jdbcManager){
		this.jdbcManager = jdbcManager;
		transactional = new ThreadLocal<Transactional>();
	}

	/**
	 * 为当前线程开启一段事务开启一段事务<br>
	 * 已存在事务将会被挂起
	 * 
	 * @param dataSource
	 * @throws SQLException
	 */
	public void start() throws SQLException {
		Connection conn = jdbcManager.getConnection();
		conn.setAutoCommit(false);
		Transactional tran = new Transactional(conn);
		tran.setHang(transactional.get());
		transactional.set(tran);
	}

	public <T> Page<T> page(Class<T> type, String sql, int start, int size, Object... values) throws SQLException {
		int total = count(sql, values);
		if (total == 0) {
			List<T> rows = new ArrayList<T>(0);
			Page<T> page = new Page<T>(rows, total);
			return page;
		} else {
			sql = getPageSql(sql, start, size);
			List<T> rows = query(type, sql, values);
			Page<T> page = new Page<T>(rows, total);
			return page;
		}
	}

	private String getPageSql(String sql, int start, int size) {
		if (start == 0) {
			return "select * from (" + sql + ") where rownum <= " + size;
		}
		StringBuffer s = new StringBuffer();
		s.append("select * from (select rs_t.*,rownum as rownumber from (");
		s.append(sql);
		s.append(") rs_t where rownum <= ");
		s.append(start + size);
		s.append(") where rownumber > ").append(start);
		return s.toString();
	}

	public int count(String sql, Object... values) throws SQLException {
		sql = "select count(*) from(" + sql + ")";
		Integer count = queryOne(Integer.class, sql, values);
		if (count != null)
			return count;
		return 0;
	}

	public <T> T queryOne(Class<T> type, String sql, Object... values) throws SQLException {
		List<T> rows = query(type, sql, values);
		if (rows != null && rows.size() > 0)
			return rows.get(0);
		return null;
	}

	public <T> List<T> query(Class<T> type, String sql, Object... values) throws SQLException {
		Transactional tran = transactional.get();
		Connection conn = null;
		if (tran != null)
			conn = tran.getConnection();
		boolean noTransactional = conn == null;
		if (conn == null)
			conn = jdbcManager.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] == null)
					throw new SQLException("parameter is null at index " + i);
				setParameter(statement, i + 1, values[i]);
			}
		}
		ResultSet rs = statement.executeQuery();
		List<T> list = new LinkedList<T>();
		int cnt = 0;
		while (rs.next()) {
			Result2Bean<T> r2b = Result2Bean.getInstance(type);
			T t = r2b.parse(rs, cnt++);
			list.add(t);
		}
		statement.close();
		if (noTransactional)
			jdbcManager.release(conn);
		return list;
	}

	public int update(String sql, Object... values) throws SQLException {
		Transactional tran = transactional.get();
		Connection conn = null;
		if (tran != null)
			conn = tran.getConnection();
		boolean noTransactional = conn == null;
		if (conn == null)
			conn = jdbcManager.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++)
				setParameter(statement, i + 1, values[i]);
		}
		int i = statement.executeUpdate();
		statement.close();
		if (noTransactional)
			jdbcManager.release(conn);
		return i;
	}

	/**
	 * @param statement
	 * @param i
	 * @param object
	 * @throws SQLException
	 */
	private void setParameter(PreparedStatement statement, int i, Object value) throws SQLException {
		if (value == null)
			throw new IllegalArgumentException("Variable is null! index=" + i);
		if (value instanceof NullValue) {
			NullValue nullValue = (NullValue) value;
			statement.setNull(i, nullValue.getType());
		} else {
			statement.setObject(i, value);
		}
	}

	public int create(String sql, Object... values) throws SQLException {
		return update(sql, values);
	}

	public int delete(String sql, Object... values) throws SQLException {
		return update(sql, values);
	}

	public void rollback() throws SQLException {
		Transactional tran = transactional.get();
		transactional.set(tran.getHang());
		rollback(tran);
	}

	public void commit() throws SQLException {
		Transactional tran = transactional.get();
		transactional.set(tran.getHang());
		commit(tran);
	}

	public void rollback(Transactional tran) throws SQLException {
		Connection conn = tran.getConnection();
		SQLException sqlException = null;
		try {
			conn.rollback();
		} catch (SQLException e) {
			sqlException = e;
		}
		conn.setAutoCommit(true);
		jdbcManager.release(conn);
		if (sqlException != null)
			throw sqlException;
	}

	private void commit(Transactional tran) throws SQLException {
		Connection conn = tran.getConnection();
		SQLException sqlException = null;
		try {
			conn.commit();
		} catch (SQLException e) {
			sqlException = e;
		}
		conn.setAutoCommit(true);
		jdbcManager.release(conn);
		if (sqlException != null)
			throw sqlException;
	}
}
