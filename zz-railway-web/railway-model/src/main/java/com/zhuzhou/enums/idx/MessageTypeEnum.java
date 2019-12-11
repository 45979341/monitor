package com.zhuzhou.enums.idx;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-04-05
 * @Description: 报文类型枚举
 **/
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    TYPE_F3("F3"),
    TYPE_07("07");

    private String type;
}
