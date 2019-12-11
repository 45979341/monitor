package com.zhuzhou.entity.statistic;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @author xiechonghu
 * @date 2019/8/9 10:02
 * @description: 近一周项点分析
 */
@Data
@Accessors(chain = true)
public class NearlyWeekGestureAnalysis {
    /**
     * 时间
     */
    private String driverDate;

    /**
     * 手势项点总数
     */
    private Long gesturePhase;

    /**
     * 缺失
     */
    private Long miss;

    /**
     * 正常
     */
    private Long normal;

    /**
     * 未处理
     */
    private Long undispose;

    /**
     * 违章
     */
    private Long illegal;
}
