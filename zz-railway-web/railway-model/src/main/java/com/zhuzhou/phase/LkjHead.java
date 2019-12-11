package com.zhuzhou.phase;

import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-10-14
 * @Description: LKJ头信息
 **/
@Data
public class LkjHead {

    /**
     * 厂家标志，如：株洲所
     */
    @EventName("厂家标志")
    private String factoryTag;

    /**
     * 车次类型,如：货本
     */
    @EventName("车次类型")
    private String trainType;

    /**
     * 车次，如：33028; K1192
     */
    @EventName("车次")
    private String trainNum;

    /**
     * 运行径路，如：4
     */
    @EventName("运行径路")
    private String lineNum;

    /**
     * 数据交路号，如：4
     */
    @EventName("数据交路号")
    private String roadNum;

    /**
     * 车站号，如：4 -953
     */
    @EventName("车站号")
    private String stationNum;

    /**
     * 司机号1，如：5038226
     */
    @EventName("司机号1")
    private String driverNum;

    /**
     * 司机号2，如：5038529
     */
    @EventName("司机号2")
    private String assistDriverNum;

    /**
     * 总重，如：4567
     */
    @EventName("总重")
    private int totalWeight;

    /**
     * 辆数，如：54
     */
    @EventName("辆数")
    private int bought;

    /**
     * 计长，如：68.8
     */
    @EventName("计长")
    private float length;

    /**
     * 载重，如：3327
     */
    @EventName("载重")
    private int loadWeight;

    /**
     * 客车辆数，如：0
     */
    @EventName("客车辆数")
    private int customNum;

    /**
     * 重车辆数，如：59
     */
    @EventName("重车辆数")
    private int weightNum;

    /**
     * 空车辆数，如：4
     */
    @EventName("空车辆数")
    private int emptyNum;

    /**
     * 车速等级，如：0
     */
    @EventName("车速等级")
    private int speedLevel;

    /**
     * 机车号，如：0
     */
    @EventName("机车号")
    private int motorNum;

    /**
     * 机车型号修改，如：0
     */
    @EventName("机车型号修改")
    private int motorType;

    /**
     * 操作端，如：0
     */
    @EventName("操作端")
    private String option;
}
