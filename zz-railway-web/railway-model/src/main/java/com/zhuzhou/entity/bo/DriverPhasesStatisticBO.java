package com.zhuzhou.entity.bo;

import lombok.Data;

import java.util.Map;

/**
 * @author xiechonghu
 * @date 2019/8/15 14:31
 * @description:
 */
@Data
public class DriverPhasesStatisticBO {
    /**
     * 司机工号
     */
    private Integer jobNum;
    /**
     * 司机名
     */
    private String name;

    /**
     * 周月年
     */
    private String type;

    /**
     * 周 月 年 值
     */
    private String typeValue;

    /**
     * 项点编号和对应的违规次数
     */
    private Map<Integer, Integer> phases;
}
