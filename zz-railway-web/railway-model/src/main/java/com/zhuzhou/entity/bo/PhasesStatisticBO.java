package com.zhuzhou.entity.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author xiechonghu
 * @date 2019/8/15 10:04
 * @description:
 */
@Data
@Accessors(chain = true)
public class PhasesStatisticBO {
    /**
     * 指导队，车间，段，局名
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
