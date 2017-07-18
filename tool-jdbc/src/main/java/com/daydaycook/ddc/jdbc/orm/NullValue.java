package com.daydaycook.ddc.jdbc.orm;

/**
 * NullValue<br>
 * 当需要设定的参数为空时需要使用这个对象
 * 
 * @author guannan.shang
 * @date 2015-1-6 15:20:12
 * @version 1.0
 */
public class NullValue {

	private int type;

	public NullValue(int type) {
	}

	public int getType() {
		return type;
	}
}
