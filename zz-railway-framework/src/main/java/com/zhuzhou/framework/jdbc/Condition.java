package com.zhuzhou.framework.jdbc;

import java.lang.annotation.*;


/**
 * @author chenzeting
 * 查询条件
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Condition {

    /**
     * 查询条件对应的属性字段
     * @return
     */
    String property() default "";

    /**
     * 条件比较方式
     * @return
     */
    Operator operator() default Operator.EQ;
}
