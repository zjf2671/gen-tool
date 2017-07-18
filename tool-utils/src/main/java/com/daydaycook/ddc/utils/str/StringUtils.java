package com.daydaycook.ddc.utils.str;

/**
 * StringUtils<br>
 * 继承了org.apache.commons.lang.StringUtils
 * 
 * @author guannan.shang
 * @date 2015-2-3 下午7:07:57
 * @version 1.0
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    /**
     * 转换为下划线命名法规则<br>
     * <code>String str = "userName";<br>
     * str = parse2Underscore(str);</code> <br>
     * 结果 str = "USER_NAME"
     * 
     * @param str
     * @return
     */
    public static String parse2Underscore(String str) {
        char[] chars = str.toCharArray();
        StringBuilder s = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 65 && chars[i] <= 90) {
                if (!isFirst)
                    s.append('_');
                s.append(chars[i]);
            } else if (chars[i] >= 97 && chars[i] <= 122) {
                s.append((char) (chars[i] - 32));
            } else {
                s.append(chars[i]);
            }
            isFirst = false;
        }
        return s.toString();
    }
    
    /**
     * 转换成骆驼命名法
     * 
     * @param str
     * @return
     */
    public static String parse2Camel(String str) {
        str = str.toLowerCase();
        char[] chars = str.toCharArray();
        char[] toChar = new char[chars.length];
        int j = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ('_' == c && i != chars.length - 1)
                c = upperCase(chars[++i]);
            toChar[j] = c;
            j++;
        }
        return new String(toChar).trim();
    }
    
    public static char lowerCase(char c) {
        if (c >= 65 && c <= 90)
            c += 32;
        return c;
    }
    
    public static char upperCase(char c) {
        if (c >= 97 && c <= 122)
            c -= 32;
        return c;
    }
    
    public static String firstCharUpperCase(String str) {
        char[] cs = str.toCharArray();
        cs[0] = upperCase(cs[0]);
        return String.valueOf(cs);
    }
    
    public static String firstCharLowerCase(String str) {
        char[] cs = str.toCharArray();
        cs[0] = lowerCase(cs[0]);
        return String.valueOf(cs);
    }
    
    /**
     * 将log中的参数注入
     * 
     * @param log 字符串包含参数格式为：{0}{1}{2}
     * @param param 参数按照顺序填入{0}{1}{2}
     * @return
     */
    public static String getLog(String log, Object... params) {
        if (params == null)
            return log;
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param != null) {
                log = log.replaceAll("\\{" + i + "\\}", param.toString());
                log = log.replaceAll("｛" + i + "｝", param.toString());
            }
        }
        return log;
    }
}
