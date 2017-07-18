package com.daydaycook.ddc.utils.excel;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daydaycook.ddc.utils.excel.ExcelTemplate;

public class TestExcelByVo {
	public static void main(String[] args) {
		try {
			// 总结 10000条 数据
			// 没有js表达式，耗时3335 ms
			// 有js表达式，耗时8485 ms
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test() throws Exception {
		String template = getTemplate();
		String target = "C:/Users/guannan.shang/Desktop/tt1.xlsx";
		Map<String, Object> data = new HashMap<String, Object>();
		List<TestVo> list = getTestData();
		long st = System.currentTimeMillis();
		data.put("data", list);
		ExcelTemplate excel = ExcelTemplate.getInstance(template, data);
		excel.write(new FileOutputStream(target));
		long et = System.currentTimeMillis();
		System.out.println(et - st);
	}

	public static List<TestVo> getTestData() {
		int max = 10000;
		int i = 0;
		List<TestVo> list = new ArrayList<TestVo>(max);
		while (i++ < max) {
			TestVo vo = new TestVo();
			vo.setId(Long.valueOf(i));
			vo.setName("张三");
			vo.setAmount(getDecimal());
			vo.setPrice(Math.random() * 234483);
			vo.setStatus((i % 3) + 1);
			vo.setStartTime(new Date());
			vo.setEndTime(new Date());
			vo.setType(Long.valueOf(i % 2));
			vo.setType1("类型1");
			vo.setType2("类型2");
			list.add(vo);
		}
		return list;
	}

	public static BigDecimal getDecimal() {
		return new BigDecimal(Math.random() * 234483);
	}

	private static String getTemplate() {
		URL url = TestExcelByVo.class.getClassLoader().getResource("excel_test_template.xlsx");
		return url.getPath();
	}
}
