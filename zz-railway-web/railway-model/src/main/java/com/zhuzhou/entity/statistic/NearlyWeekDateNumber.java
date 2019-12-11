package com.zhuzhou.entity.statistic;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiechonghu
 * @date 2019/8/9 13:52
 * @description: 首页统计接收实体：近一周出乘数量，近一周项点汇总，
 *                近一周违章项点汇总，近一周违章手势项点汇总
 */
@Data
@Accessors(chain = true)
public class NearlyWeekDateNumber {

    /**
     * 时间
     */
    private String driverDate;

    /**
     * 数量
     */
    private Long num;
}
