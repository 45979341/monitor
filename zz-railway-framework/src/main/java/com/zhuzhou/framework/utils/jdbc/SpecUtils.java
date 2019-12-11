package com.zhuzhou.framework.utils.jdbc;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.framework.utils.stomp.StringUtils;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @Author: chenzeting
 * Date:     2019/2/12
 * Description:
 */
public class SpecUtils {

    /**
     * 构建复合查询条件
     * @param form
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> QueryWrapper<T> condition(Form<T> form) {
        Field[] fields = form.getClass().getDeclaredFields();
        QueryWrapper<T> qw = null;
        Condition condition;
        String property;
        Field upperField = null;
        Field lowerField = null;
        for (Field field : fields) {
            condition = field.getAnnotation(Condition.class);
            if(condition == null){
                continue;
            }
            Object value = ReflectionUtils.getFieldValue(form, field.getName());
            if(value == null){
                continue;
            }
            if (qw == null) {
                qw = new QueryWrapper<>();
            }
            String name = StringUtils.isEmpty(condition.property()) ? field.getName() : condition.property();
            property = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(name);
            switch (condition.operator()){
                case EQ:
                    if (!"".equals(value)) {
                        qw.eq(property, value);
                    }
                    break;
                case GT:
                    qw.gt(property, value);
                    break;
                case GE:
                    qw.ge(property, value);
                    break;
                case LE:
                    qw.le(property, value);
                    break;
                case LT:
                    qw.lt(property, value);
                    break;
                case IN:
                    if(value instanceof Collection){
                        qw.in(property, (Collection) value);
                    }else{
                        qw.in(property, value);
                    }
                    break;
                case NOT_IN:
                    if(value instanceof Collection){
                        qw.notIn(property, (Collection) value);
                    }else{
                        qw.notIn(property, value);
                    }
                    break;
                case LIKE:
                    if ("".equals(value)) {
                        break;
                    }
                    qw.like(property, value);
                    break;
                case LIKE_LEFT:
                    qw.likeLeft(property, value);
                    break;
                case LIKE_RIGHT:
                    qw.likeRight(property, value);
                    break;
                case NE:
                    qw.ne(property,value);
                    break;
                case BETWEEN_LOWER:
                    lowerField = field;
                    break;
                case BETWEEN_UPPER:
                    upperField = field;
                    break;
                case EXISTS:
                    qw.exists((String)value);
                    break;
                case NOT_EXISTS:
                    qw.notExists((String)value);
                    break;
            }
        }
        if(upperField != null && lowerField != null){
            String upperProperty = getProperty(upperField);
            Object upper = ReflectionUtils.getFieldValue(form, upperField.getName());
            String lowerProperty = getProperty(lowerField);
            Object lower = ReflectionUtils.getFieldValue(form, lowerField.getName());
            if (upperProperty.equals(lowerProperty)) {
                if (upper != null && lower != null) {
                    qw.between(upperProperty, lower, upper);
                } else if (upper != null) {
                    qw.lt(upperProperty, upper);
                } else if (lower != null) {
                    qw.gt(lowerProperty, lower);
                }
            } else {
                if (upper != null) {
                    qw.lt(upperProperty, upper);
                } else if (lower != null) {
                    qw.gt(lowerProperty, lower);
                }
            }
        } else if (upperField != null) {
            property = getProperty(upperField);
            Object upper = ReflectionUtils.getFieldValue(form, upperField.getName());
            if (upper != null) {
                qw.lt(property, upper);
            }
        } else if (lowerField != null) {
            property = getProperty(lowerField);
            Object lower = ReflectionUtils.getFieldValue(form, lowerField.getName());
            if (lower != null) {
                qw.gt(property, lower);
            }
        }
        return qw;
    }

    /**
     * 获取字段属性
     * @param field
     * @return
     */
    private static String getProperty (Field field) {
        Condition condition = field.getAnnotation(Condition.class);
        String name = StringUtils.isEmpty(condition.property()) ? field.getName() : condition.property();
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(name);
    }

    /**
     * 构建分页查询条件
     * @param <T>
     * @param form
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Page pageable(Form<T> form) {
        Field[] fields = form.getClass().getDeclaredFields();
        Order order;
        Page page = (Page)form;
        for (Field field : fields) {
            order = field.getAnnotation(Order.class);
            if (order == null) {
                continue;
            }
            String property = StringUtils.isEmpty(order.property()) ? field.getName() : order.property();
            property = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(property);
            Sort.Direction direction = order.direction();
            if (direction.isAscending()) {
                page.setAsc(property);
            } else {
                page.setDesc(property);
            }
        }
        return page;
    }

    /**
     * 构建分页查询条件
     * @param qw
     * @param <T>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void pageable(QueryWrapper<T> qw, Form<T> form) {
        Field[] fields = form.getClass().getDeclaredFields();
        Order order;
        for (Field field : fields) {
            order = field.getAnnotation(Order.class);
            if (order == null) {
                continue;
            }
            String property = StringUtils.isEmpty(order.property()) ? field.getName() : order.property();
            property = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(property);
            Sort.Direction direction = order.direction();
            if (direction.isAscending()) {
                qw.orderByAsc(property);
            } else {
                qw.orderByDesc(property);
            }
        }
    }
}
