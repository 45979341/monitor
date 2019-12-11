package com.zhuzhou.entity.statistic;

import lombok.Data;

/**
 * @author xiechonghu
 * @date 2019/8/12 11:10
 * @description: 出乘统计
 */
@Data
public class RailStatistic {
    /**
     * id 用来查找违章扣分表
     */
    private String id;

    /**
     * 开车日期
     */
    private String driverDate;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 车型
     */
    private String trainType;

    /**
     * 起点站
     */
    private String originStation;

    /**
     * 终点站
     */
    private String terminus;

    /**
     * 车号
     */
    private String motorNum;

    /**
     * 司机号
     */
    private String driverNum;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 副司机号
     */
    private String assisDriverNum;

    /**
     * 副司机名
     */
    private String assisDriverName;

    /**
     * 项点统计
     */
    private Long phaseNum;

    /**
     * 违章总数
     */
    private Long illegalNum;
}
