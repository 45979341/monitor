package com.zhuzhou.framework.utils.stomp;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

}
