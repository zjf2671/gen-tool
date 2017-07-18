package com.daydaycook.ddc.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daydaycook.ddc.utils.net.HostUtils;
import com.daydaycook.ddc.utils.str.IdCreator;
import com.daydaycook.ddc.utils.str.IdCreatorNoChar;
import com.daydaycook.ddc.utils.str.IdCreatorWithChar;
import com.daydaycook.ddc.utils.zk.CuratorLock;

public class IdUtils {
	private static Logger logger = LoggerFactory.getLogger(IdUtils.class);
	private static String server = "00";
	private static Map<String, IdCreator> creators = new HashMap<String, IdCreator>();
	static final String SERVER_NO_ENV = "SERVER_NO";
	private static boolean useChar = false;
	static {
		String serverNo = null;
		try {
			serverNo = CuratorLock.getLock();
		} catch (Throwable e) {
			logger.error("*********** 获取SERVER_NO失败 ***********", e);
		}
		logger.info("获取到服务器编号：" + serverNo);
		if (serverNo == null) {
			int i = 20;
			while (i-- > 0)
				logger.error("*********** 获取SERVER_NO失败 ***********");
		} else {
			server = serverNo;
			CuratorLock.saveServer(server, HostUtils.getHostIP());
		}
	}

	public static String next(String type) {
		return next(type, 4);
	}

	public static String next(String type, int length) {
		if (!hasCreator(type, length)) {
			initCreator(type, length);
		}
		StringBuilder s = new StringBuilder();
		s.append(type).append(server).append(creators.get(type + "_" + length).next());
		return s.toString();
	}

	private static boolean hasCreator(String type, int length) {
		return creators.containsKey(type + "_" + length);
	}

	private static synchronized void initCreator(String type, int length) {
		if (!hasCreator(type, length)) {
			if (useChar)
				creators.put(type + "_" + length, new IdCreatorWithChar(length));
			else
				creators.put(type + "_" + length, new IdCreatorNoChar(length));
		}
	}
}
