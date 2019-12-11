package com.zhuzhou.enums.lkj;

import com.zhuzhou.framework.utils.stomp.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-04-02
 * @Description: lkj信号枚举
 **/
@AllArgsConstructor
@Getter
public enum SignalMachineEnum {

    IN("进站"),
    OUT("出站"),
    PASS("通过"),
    APPROACH("接近"),
    ADVANCE("预告");

    private String name;

    /**
     * 判断是否存在以上类型前缀信号机
     * @param signalMachine
     * @return
     */
    public static boolean fromValue(String signalMachine) {
        if (StringUtils.isEmpty(signalMachine)) {
            return false;
        }
        SignalMachineEnum[] values = SignalMachineEnum.values();
        for (SignalMachineEnum each : values) {
            if (signalMachine.startsWith(each.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据信号机获取前缀
     * @param signalMachine
     * @return
     */
    public static String startValue (String signalMachine) {
        if (StringUtils.isEmpty(signalMachine)) {
            return "";
        }
        SignalMachineEnum[] values = SignalMachineEnum.values();
        for (SignalMachineEnum each : values) {
            if (signalMachine.startsWith(each.getName())) {
                return each.getName();
            }
        }
        //其他类型默认为出站
        return "出站";
    }
}