package com.daydaycook.ddc.utils;

import com.daydaycook.ddc.utils.IdUtils;

public class TestIdUtils {

	public static void main(String[] args) {
		// testCost();
		// sleep(1000);
		testData();
	}

	public static void testData() {
		int i = 0;
		while (i++ < 20) {
			System.out.println(IdUtils.next("Y"));
			sleep(30000);
		}
		System.out.println("12345678901234567890");
	}

	public static void testCost() {
		System.out.println(IdUtils.next("X"));
		sleep(1000);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 800000; i++) {
			IdUtils.next("X");
		}
		System.out.println("cost:" + (System.currentTimeMillis() - start));
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (Exception e) {
		}
	}
}
