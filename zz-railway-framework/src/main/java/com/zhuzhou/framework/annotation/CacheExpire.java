package com.zhuzhou.framework.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 自定义cache 缓存注解
 * 该注解声明为缓存失效时间
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(CacheExpires.class)
public @interface CacheExpire {

    /**
     * key 名称
     * @return
     */
    String cacheName();

    /**
     * expire time, default 60s
     */
    @AliasFor("expire")
    long value() default 60L;

    /**
     * expire time, default 60s
     */
    @AliasFor("value")
    long expire() default 60L;
}
