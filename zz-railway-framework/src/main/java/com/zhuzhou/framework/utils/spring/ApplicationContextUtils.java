package com.zhuzhou.framework.utils.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author chenzeting
 * @since 2019-08-21
 */
public class ApplicationContextUtils {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        ApplicationContextUtils.context = context;
    }
}
