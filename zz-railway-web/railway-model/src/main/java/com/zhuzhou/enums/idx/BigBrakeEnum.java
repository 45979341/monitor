package com.zhuzhou.enums.idx;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description: 大闸指令枚举
 **/
@AllArgsConstructor
@Getter
public enum BigBrakeEnum {
    BIG_0("0", "运转位"),
    BIG_1("1", "初制动"),
    BIG_2("2", "常用制动区"),
    BIG_3("3", "全制动"),
    BIG_4("4", "抑制位"),
    BIG_5("5", "重联位"),
    BIG_6("6", "紧急制动位"),
    INVALID ("0xFF", "无效");

    private String code;
    private String name;
}
