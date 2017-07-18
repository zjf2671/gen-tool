package com.daydaycook.ddc.core.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseDAO<T> {
	public T findById(@Param("po") T po);

	public List<T> findOfPage(@Param("po") T po, @Param("page") Pagination page);

	public int countOfPage(@Param("po") T po);

	public List<T> findAll(@Param("po") T po);
	
	public int insert(T po);

	public int update(T po);

	public int delete(T po);
}
