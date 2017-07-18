package com.daydaycook.ddc.utils.str;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdCreatorWithChar extends IdCreator {
	private static Logger logger = LoggerFactory.getLogger(IdCreatorNoChar.class);

	private static final String up = "0123456789ABCDFGHJKMNPRSTUVWXY";
	private static final char[] chars = up.toCharArray();
	private static final int CHAR_NUM = chars.length;
	private int MAX_CNT = CHAR_NUM;
	private int length = 1;

	public IdCreatorWithChar(){
		this(4);
	}

	/**
	 * @param number
	 *            最后数字的个数
	 */
	public IdCreatorWithChar(int number){
		this.length = number;
		int max = 1;
		while (number-- > 0) {
			max *= CHAR_NUM;
		}
		this.MAX_CNT = max;
	}

	public synchronized String next() {
		long num = getNum();
		if (num != last) {
			last = num;
			cnt = 0;
		}
		if (cnt == MAX_CNT) {
			while (true) {
				try {
					logger.debug("id create too fast(over {}/s) ,sleep 20ms", MAX_CNT);
					Thread.sleep(20);
				} catch (Exception e) {
				}
				num = getNum();
				if (num != last) {
					last = num;
					cnt = 0;
					break;
				}
			}
		}
		StringBuilder s = new StringBuilder();
		s.append(num);
		String str = parse(cnt);
		int len = length - str.length();
		while (len-- > 0) {
			s.append("0");
		}
		s.append(str);
		cnt++;
		return s.toString();
	}

	public String parse(int num) {
		if (num == 0)
			return "0";
		StringBuilder s = new StringBuilder();
		append(s, num);
		return s.toString();
	}

	private void append(StringBuilder s, int num) {
		if (num == 0)
			return;
		s.insert(0, chars[Long.valueOf(num % CHAR_NUM).intValue()]);
		append(s, num / CHAR_NUM);
	}
}
