package com.daydaycook.ddc.utils.str;

import com.daydaycook.ddc.utils.str.CharRandom;

public class TestString {
	static String up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String dw = "abcdefghijklmnopqrstuvwxyz";
	static String nu = "0123456789";
	static String str = nu + dw + up + "_";
	static char[] chars = str.toCharArray();
	static final int len = chars.length;
	static int index = 0;

	public static void main(String[] args) {
		CharRandom charRandom = new CharRandom(str);
		String str = charRandom.get(32);
		System.out.println(str);
		int i = 200;
		while (i-- > 0) {
			System.out.println(index);
			System.out.println(index);
			System.out.println(index);
		}
	}

	public static void test() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					index++;
					if (index == len - 1)
						index = 0;
				}
			}
		}).start();
	}
}
