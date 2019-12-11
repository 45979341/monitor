package com.zhuzhou.phase.utils;

import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.entity.config.ConfigModel;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.phase.LkjHead;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author xiechonghu
 * @date 2019/10/15 14:09
 */
public class HeadUtils {
    /**
     * 判断车次是否以5开头且长度为5位数。为单机
     * @param head
     * @return
     */
    public static boolean startWith5Length5(LkjHead head) {
        return head.getTrainNum().startsWith("5") && head.getTrainNum().length() == 5;
    }

    /**
     * 判断是否是货车
     * @param head
     * @return
     */
    public static boolean freight(LkjHead head) {
        return head.getTrainType().contains("货");
    }

    /**
     * 判断是否是补机
     */
    public static boolean bu(LkjHead head) {
        return head.getTrainType().contains("补");
    }

    /**
     * 判断是否是电力机车
     * @param head
     * @return
     */
    public static boolean electricLocomotive(LkjHead head) {
        ConfigModel motor = ConfigConst.modelMap.get(head.getMotorType());
        return "电力机车".equals(motor.getMotorType());
    }

    /**
     * 判断是否是内燃机车
     */
    public static boolean dieselLocomotive(LkjHead head) {
        ConfigModel motor = ConfigConst.modelMap.get(head.getMotorType());
        return "内燃机车".equals(motor.getMotorType());
    }

    /**
     * 判断机车名是否是指定名
     */
    public static boolean motorName(LkjHead head, String name) {
        ConfigModel motor = ConfigConst.modelMap.get(head.getMotorType());
        return motor.getMotorName().contains(name);
    }

    /**
     * 客车定压500
     * 货车定压600
     * 上下浮动10
     * @param head
     * @return
     */
    public static int constPressure(LkjHead head) {
        return (head.getTrainType().contains("客")) ? 600 : 500;
    }

    /**
     * 最大定压+40
     * 客车限压550
     * 货车限压650
     * @param head
     * @return
     */
    public static int limitPressure(LkjHead head) {
        return (head.getTrainType().contains("客")) ? 640 : 540;
    }

    /**
     * 列车管压累计减压量不能大于规定值
     * （600标压默认值为170KPA，500标压默认值为140KPA，浮动20KPA）
     */
    public static int maxDropPressure(LkjHead head) {
        return (head.getTrainType().contains("客")) ? 190 : 160;
    }

    /**
     * 货车限速
     * @param head
     * @return
     */
    public static int limitSpeed(LkjHead head) {
        // 总重大于10000 限速30
        return (head.getTotalWeight() > 10000) ? 30 : 15;
    }
}
