package com.daydaycook.ddc.jdbc;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.daydaycook.ddc.jdbc.Jdbc;
import com.daydaycook.ddc.jdbc.JdbcFactory;
import com.daydaycook.ddc.utils.date.DateUtils;

public class DataCreator {
	static Jdbc jdbc = JdbcFactory.getJdbc("jdbc");
	private static Random random = new Random();
	private static String[] remark1 = { "发工资", "交房租", "交水费", "交电费", "交保护费" };
	private static String[] remark2 = { "收会员费", "收诊断费", "收维修费" };

	private static String startDate = "2016-08-01";
	private static Date endDate = DateUtils.parseDate("2016-08-15 17:00:00");
	private static int seconds = 0;
	private static int step = 100;
	private static int page = 500;
	private static int pageCnt = 100;
	private static int totalCnt = 0;

	public static void main(String[] args) {
		try {
			create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void create() throws SQLException {
		String sql = "insert into income_exp_recoder(create_time,op_type,item_id, money_cost,remark,shop_id,update_time,transaction_status, create_userid, source_id)";
		sql = sql + "values(?,?,?,?,?,?,?,0,0,0)";
		int i = pageCnt;
		while (i-- > 0) {
			boolean flag = create(sql, page);
			System.out.println("创建了" + page + "条数据");
			if (flag)
				break;
		}
		System.out.println("共创建了" + totalCnt + "条数据");
	}

	public static boolean create(String sql, int cnt) throws SQLException {
		int i = cnt;
		jdbc.start();
		while (i-- > 0) {
			String remark = "";
			int s = random.nextInt(step);
			seconds += s;
			Date date = getRandomTime(startDate, seconds);
			if (date.after(endDate)) {
				jdbc.commit();
				return true;
			}
			int opType = random.nextInt(2);
			BigDecimal cost = new BigDecimal(random.nextInt(1000) + "." + random.nextInt(100));
			if (opType == 0) {
				remark = remark1[random.nextInt(4)];
			} else {
				remark = remark2[random.nextInt(3)];
			}
			int shopId = 200;
			shopId = random.nextInt(500);
			jdbc.create(sql, date, opType, random.nextInt(3), cost, remark, shopId, date);
			totalCnt++;
		}
		jdbc.commit();
		return false;
	}

	private static Date getRandomTime(String start, int seconds) {
		Date startDate = DateUtils.parseDate(start);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		DateUtils.toDayStart(cal).add(Calendar.SECOND, -1);
		long t = cal.getTimeInMillis();
		t = t + seconds * 1000;
		cal.setTimeInMillis(t);
		return cal.getTime();

	}
}
