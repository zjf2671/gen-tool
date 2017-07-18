package com.daydaycook.ddc.utils.str;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdCreatorNoChar extends IdCreator {
	private static Logger logger = LoggerFactory.getLogger(IdCreatorNoChar.class);

	private int MAX_CNT = 10;

	public IdCreatorNoChar(){
		this(4);
	}

	/**
	 * @param number
	 *            最后数字的个数
	 */
	public IdCreatorNoChar(int number){
		number = number > 8 ? 8 : number;
		int max = 1;
		while (number-- > 0) {
			max *= 10;
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
		num = num * MAX_CNT + cnt;
		cnt++;
		return String.valueOf(num);
	}
}
