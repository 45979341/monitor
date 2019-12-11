package com.zhuzhou.enums.exterior;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-06-19
 * @Description: 状态枚举
 **/
@AllArgsConstructor
@Getter
public enum StatusEnum {
    NORMAL(0, "正常"),
    DISABLE(1, "停用");

    public Integer id;
    public String value;
}