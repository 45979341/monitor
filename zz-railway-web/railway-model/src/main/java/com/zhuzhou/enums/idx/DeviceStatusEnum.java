package com.zhuzhou.enums.idx;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-03-27
 * @Description: 装置状态枚举
 **/
@AllArgsConstructor
@Getter
public enum DeviceStatusEnum {

    DS_00("0-0", "监控非调车"),
    DS_01("0-1", "监控调车"),
    DS_10("1-0", "降级非调车"),
    DS_11("1-1", "降级调车");

    private String code;
    private String name;
}
