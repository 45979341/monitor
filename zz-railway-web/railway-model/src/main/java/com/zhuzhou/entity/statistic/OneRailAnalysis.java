package com.zhuzhou.entity.statistic;

import lombok.Data;

/**
 * @author xiechonghu
 * @date 2019/8/9 17:45
 * @description: 每一趟分析统计
 */
@Data
public class OneRailAnalysis {

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 违章项点编码
     */
    private Integer phaseId;

    /**
     * 项点名称
     */
    private String name;

    /**
     * 次数
     */
    private Long count;

    /**
     * 扣分
     */
    private Integer score;

    /*
     * 责任
     */

    /**
     * 总计
     */
    private Long totalPoints;
}
