package com.zhuzhou.enums.exterior;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-06-19
 * @Description: 审核状态枚举
 **/
@AllArgsConstructor
@Getter
public enum AuditStatusEnum {
    UN_AUDIT(0, "未审核"),
    PASS(1, "审核通过"),
    REFUSE(2, "审核不通过");

    public Integer id;
    public String desc;
}