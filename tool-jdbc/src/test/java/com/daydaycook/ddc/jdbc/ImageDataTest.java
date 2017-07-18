package com.daydaycook.ddc.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.daydaycook.ddc.jdbc.Jdbc;
import com.daydaycook.ddc.jdbc.JdbcFactory;
import com.daydaycook.ddc.utils.str.CharRandom;

public class ImageDataTest {
	static Jdbc jdbc = JdbcFactory.getJdbc("jdbc");

	private static String SQL = "insert into cms_image(path,name,img_server)values(?,?,?)";

	public static void main(String[] args) {
		CharRandom random = new CharRandom();
		for (String name : getName()) {
			try {
				String path = random.get(6) + "/" + random.get(15);
				jdbc.create(SQL, path, name, "qiniu");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<String> getName() {
		List<String> list = new ArrayList<String>();
		list.add("测试测试");
		list.add("测试1测试1");
		list.add("测试2测试2");
		list.add("测试3测试3");
		list.add("测试4测试4");

		list.add("测测试试");
		list.add("测测1试试1");
		list.add("测1测1试1试1");
		list.add("测1测111试1试1");
		list.add("测1测222试1试1");

		list.add("测1试2测3试4");
		list.add("测21试41测32试61");
		list.add("测1试1测2试1");
		list.add("测1试12测23试2");
		list.add("测123试345测试234");

		list.add("测1122测3344试1234试");
		list.add("测d2测1试2试1");
		list.add("测c1测d1试1试1");
		list.add("测a1测1d11试1试1");
		list.add("测b1测2d22试1试a1");

		list.add("测a1b1c2d2测e3f3e4f4试c1a2b3c4试");
		list.add("测d2d测1c试2b试1a");
		list.add("测c1b测d1试1试1");
		list.add("测a1c测b1da1d1试1");
		list.add("测b1测2d2k2试1试a1");
		return list;
	}
}
