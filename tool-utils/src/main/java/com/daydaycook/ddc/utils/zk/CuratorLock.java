package com.daydaycook.ddc.utils.zk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daydaycook.ddc.utils.date.DateUtils;
import com.daydaycook.ddc.utils.str.Radix;

public class CuratorLock {
	private static Logger logger = LoggerFactory.getLogger(CuratorLock.class);
	private static final String LOCK_PATH = "/server_no_lock/";
	private static final String LOG_PATH = "/server_no_log/";
	private static final Radix RADIX = new Radix("ABCDFGHJKMNPQRTUVWXY");
	private static final int MAX_CNT = 500;
	private static final int TIME_OUT = 800;
	private static CuratorFramework CLIENT = null;
	private static String ZK_URL = null;

	private static CuratorFramework getClient() {
		if (CLIENT == null) {
			InputStream is = null;
			try {
				is = new FileInputStream(getConfigPath());
				Properties properties = new Properties();
				properties.load(is);
				ZK_URL = properties.getProperty("cluster1.serverList");
			} catch (IOException e1) {
				logger.error("", e1);
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			CLIENT = CuratorFrameworkFactory.newClient(ZK_URL, 5000, 5000, retryPolicy);
			CLIENT.start();
		}
		return CLIENT;
	}

	public static void saveServer(String path, String ip) {
		try {
			String path2 = LOG_PATH + path + "/" + ip;
			String time = DateUtils.getDefaultDateTimeNow();
			if (CLIENT.checkExists().forPath(path2) == null) {
				CLIENT.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path2,
						time.getBytes("UTF-8"));
			} else {
				CLIENT.setData().forPath(path2, time.getBytes("UTF-8"));
			}
			path2 = LOG_PATH + path;
			CLIENT.setData().forPath(path2, ip.getBytes("UTF-8"));
		} catch (Throwable e) {
			logger.error("", e);
		}
	}

	public static String getLock() {
		try {
			int i = getLock(getClient());
			return parse(i);
		} catch (Throwable e) {
			logger.error("", e);
		}
		return null;
	}

	private static String parse(int i) {
		if (i < 10)
			return "0" + i;
		if (i < 100)
			return "" + i;
		if (i >= 100 && i < MAX_CNT) {
			i = i - 100;
			if (i < 20)
				return "A" + RADIX.parse(i);
			return RADIX.parse(i);
		}
		return null;
	}

	private static int getLock(CuratorFramework client) throws Exception {
		int i = -1;
		while (++i < MAX_CNT) {
			String path = LOCK_PATH + i;
			InterProcessMutex lock = new InterProcessMutex(client, path);
			if (lock.acquire(TIME_OUT, TimeUnit.MILLISECONDS)) {
				return i;
			} else {
				logger.debug(i + "被占用");
			}
		}
		return -1;
	}

	private static String getConfigPath() {
		String path = System.getProperty("global.config.path");
		StringBuilder s = new StringBuilder(path);
		if (!path.endsWith(File.separator))
			s.append(File.separator);
		s.append("osoa").append(File.separator).append("zookeeper-cluster.properties");
		return s.toString();
	}
}
