package com.daydaycook.ddc.utils;

import com.daydaycook.ddc.utils.zk.CuratorLock;

public class TestCuratorLock {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String path = CuratorLock.getLock();
		while (true) {
			System.out.println(path);
			Thread.sleep(5000);
		}
	}
}