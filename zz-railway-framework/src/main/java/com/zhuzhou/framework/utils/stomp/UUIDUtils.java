package com.zhuzhou.framework.utils.stomp;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @Author:   chenzeting
 * Date:     2018/7/18
 * Description:
 */
public class UUIDUtils {

    public static String randomUUID () {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public static String randomAlphanumeric (int i) {
        return RandomStringUtils.randomAlphanumeric(i);
    }
}
