package com.zhuzhou.framework.jdbc;

/**
 * @author chenzeting
 * 操作符
 */
public enum Operator {

    /**
     * 相等
     */
    EQ ,
    /**
     * 大于
     */
    GT ,
    /**
     * 大于等于
     */
    GE ,
    /**
     * 小于
     */
    LT ,
    /**
     * 小于等于
     */
    LE,
    /**
     * 不等于
     */
    NE,
    /**
     * 通配
     */
    LIKE,
    /**
     * 左通配
     */
    LIKE_LEFT,
    /**
     * 右通配
     */
    LIKE_RIGHT,
    /**
     * 在集合之内
     */
    IN,
    /**
     * 不在集合之内
     */
    NOT_IN,
    /**
     * 范围下限
     */
    BETWEEN_LOWER,
    /**
     * 范围上限
     */
    BETWEEN_UPPER,
    /**
     * 存在
     */
    APPLY,
    /**
     * 存在
     */
    EXISTS,
    /**
     * 不存在
     */
    NOT_EXISTS
}
