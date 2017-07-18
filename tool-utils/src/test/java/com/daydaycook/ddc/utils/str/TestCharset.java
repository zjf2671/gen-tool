package com.daydaycook.ddc.utils.str;

import java.io.UnsupportedEncodingException;

public class TestCharset {

    public static String[] charsets = { "UTF-8", "GB2312", "GBK", "ISO8859-1" };

    public static void parse2EveryCharset(String str) {
        for (String sourceCharset : charsets) {
            for (String targetCharset : charsets) {
                if (!sourceCharset.equals(targetCharset)) parse(sourceCharset, targetCharset, str);
            }
        }
    }

    private static void parse(String sourceCharset, String targetCharset, String data) {
        try {
            StringBuilder log = new StringBuilder();
            log.append((sourceCharset+"          ").substring(0,10));
            log.append("转为  ");
            log.append((targetCharset+"          ").substring(0,10));
            System.out.print(log);
            System.out.println(new String(data.getBytes(sourceCharset), targetCharset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
