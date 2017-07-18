package com.daydaycook.ddc.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 * 
 * @author guannan.shang
 * 
 */
public class AES {
	private static final String AES = "AES";
	private static final int ENCRYPT = Cipher.ENCRYPT_MODE;
	private static final int DECRYPT = Cipher.DECRYPT_MODE;

	/**
	 * 加密/解密
	 * 
	 * @param src 要加密的数据
	 * @param key 密钥
	 * @param cryptMode 加密/解密
	 * @return
	 * @throws Exception
	 */
	public final static byte[] crypt(byte[] src, String key, int cryptMode)
			throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);// 设置密钥
		cipher.init(cryptMode, securekey);// 设置密钥和加密/解密形式
		return cipher.doFinal(src);
	}

	/**
	 * 二进制转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) {
		try {
			return new String(crypt(hex2byte(data.getBytes()), key,DECRYPT));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static String encrypt(String data, String key) {
		try {
			return byte2hex(crypt(data.getBytes(), key,ENCRYPT));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}