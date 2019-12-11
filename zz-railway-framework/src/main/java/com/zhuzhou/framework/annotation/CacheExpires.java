package com.zhuzhou.framework.annotation;

import java.lang.annotation.*;


/**
 * 自定义cache 缓存注解数组
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheExpires {

    CacheExpire[] value();
}
