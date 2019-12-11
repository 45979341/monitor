package com.zhuzhou.phase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author chenzeting
 * @Date 2019-10-14
 * @Description: LKJ事件名称定义标签
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventName {
    /**
     * 事件名称
     * @return
     */
    String value();
}
