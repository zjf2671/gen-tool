package com.daydaycook.ddc.utils;

import com.daydaycook.ddc.utils.str.Radix;

public class TestRadix {
	static Radix radix16 = new Radix("0123456789ABCDEF");
	static Radix radix62 = new Radix("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

	public static void main(String[] args) {
		// System.out.println(radix16.parse(288));
		// System.out.println(radix16.parse("__"));
		// System.out.println(radix16.parse(68186));
		// System.out.println(radix16.parse("DEF_"));
		System.out.println("-------- 测试 --------");
		System.out.println(Long.MAX_VALUE + " --> " + radix62.parse(Long.MAX_VALUE));
		System.out.println("10000000000 --> " + radix62.parse(10000000000L));
		System.out.println("-------- 过程 --------");
		String str = parse("33DC4DFB_87DE_44CB_8E6C_B04B6BCDD0C8");
		System.out.println("-------- 结果 --------");
		System.out.println("33DC4DFB_87DE_44CB_8E6C_B04B6BCDD0C8 : 36");
		System.out.println(str + " : " + str.length());
	}

	public static String parse(String str) {
		String[] ss = str.split("_");
		StringBuilder sb = new StringBuilder();
		for (String s : ss) {
			long l = radix16.parse(s);
			String s2 = radix62.parse(l);
			sb.append("_");
			sb.append(s2);
			System.out.println(s + " --> " + l + " --> " + s2);
		}
		return sb.substring(1).toString();
	}
}
