package com.zhuzhou.enums.idx;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-03-27
 * @Description: 机车信号枚举
 **/
@AllArgsConstructor
@Getter
public enum CabSignalEnum {

    NONE("00","无灯"),
    GREEN("01","绿"),
    YELLOW("02","黄"),
    D_YELLOW("03","双黄"),
    RED_YELLOW("04","红黄"),
    RED("05","红"),
    WHITE("06","白"),
    GREEN_YELLOW("07","绿黄"),
    YELLOW2("08","黄2");

    private String code;
    private String name;
}
