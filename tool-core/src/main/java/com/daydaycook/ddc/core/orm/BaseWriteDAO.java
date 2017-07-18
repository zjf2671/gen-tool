package com.daydaycook.ddc.core.orm;

public interface BaseWriteDAO<T> {
	int insert(T po);

	int update(T po);

	int delete(T po);
}
