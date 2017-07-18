package com.daydaycook.ddc.utils.str;

import java.util.Random;

public class CharRandom {
	static String up = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String dw = "abcdefghijklmnopqrstuvwxyz";
	static String nu = "0123456789";
	public final static int DEFAULT_LENGTH = 16;
	private Random random = new Random();
	private String str;
	private char[] chars;

	public CharRandom(){
		this.str = nu + dw + up;
		this.chars = str.toCharArray();
	}

	public CharRandom(String str){
		this.str = str;
		this.chars = str.toCharArray();
	}

	public String get() {
		return get(DEFAULT_LENGTH);
	}

	public String get(int length) {
		char[] cs = new char[length];
		for (int i = 0; i < length; i++) {
			cs[i] = chars[getIndex()];
		}
		return new String(cs);
	}

	private int getIndex() {
		Random random1 = new Random(random.nextInt());
		int i = random1.nextInt(10000);
		i = i % chars.length;
		if (i < 0)
			return i * -1;
		return i;
	}
}
