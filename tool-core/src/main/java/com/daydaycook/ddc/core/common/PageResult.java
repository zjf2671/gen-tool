package com.daydaycook.ddc.core.common;

import java.io.Serializable;
import java.util.List;

import com.daydaycook.ddc.core.orm.Pagination;

public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -5508893650738751862L;
	private int pageNo;
	private int pageSize;
	private int totalSize;
	private int totalPage;

	private List<T> list;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalPage() {
		this.totalPage = totalSize / pageSize + (totalSize % pageSize == 0 ? 0 : 1);
		return totalPage;
	}

	public void copy(PageResult<? extends Object> page) {
		this.setPageNo(page.getPageNo());
		this.setPageSize(page.getPageSize());
		this.setTotalSize(page.getTotalSize());
	}

	public void copy(Pagination page) {
		this.setPageNo(page.getPageNo());
		this.setPageSize(page.getPageSize());
	}
}
