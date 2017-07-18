package com.daydaycook.ddc.utils.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostUtils {
	static Logger logger = LoggerFactory.getLogger(HostUtils.class);

	public static String getHostIP() {
		try {
			if (isWindowsOS()) {
				InetAddress ip = InetAddress.getLocalHost();
				return ip.getHostAddress();
			} else {
				InetAddress ip = null;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
								&& ip.getHostAddress().indexOf(":") == -1) {
							return ip.getHostAddress();
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public static String getHostName() {
		try {
			if (isWindowsOS()) {
				InetAddress ip = InetAddress.getLocalHost();
				return ip.getHostAddress();
			} else {
				return getLocalNameFromException();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * 获得主机IP
	 *
	 * @return String
	 */
	private static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	private static String getLocalNameFromException() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			return ia.getHostName();
		} catch (UnknownHostException e) {
			logger.error("", e);
			String s = e.getMessage();
			return s.substring(0, s.indexOf(":"));
		}
	}
}
