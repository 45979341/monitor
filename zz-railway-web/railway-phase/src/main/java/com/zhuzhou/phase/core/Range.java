package com.zhuzhou.phase.core;

import java.lang.annotation.*;

/**
 * @author xiechonghu
 * @date 2019/11/12 17:06
 * @description:
 * 按区间判断项点
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Range {
    String value();
}
