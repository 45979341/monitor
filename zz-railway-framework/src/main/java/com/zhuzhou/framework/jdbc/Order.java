package com.zhuzhou.framework.jdbc;


import org.springframework.data.domain.Sort;

import java.lang.annotation.*;

/**
 * @author chenzeting
 * 分页排序
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Order {

    /**
     * 排序字段
     * @return
     */
    String property() default "";

    /**
     * 排序方向
     * @return
     */
    Sort.Direction direction() default Sort.Direction.DESC;
}
