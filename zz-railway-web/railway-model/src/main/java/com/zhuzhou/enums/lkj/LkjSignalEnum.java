package com.zhuzhou.enums.lkj;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-04-02
 * @Description: lkj信号枚举
 **/
@AllArgsConstructor
@Getter
public enum LkjSignalEnum {

    NONE(""),
    GREEN("绿灯"),
    YELLOW("黄灯"),
    D_YELLOW("双黄"),
    D_YELLOW_FLASH("双黄闪"),
    RED_YELLOW("红黄"),
    RED("红灯"),
    WHITE("白灯"),
    GREEN_YELLOW("绿黄"),
    YELLOW2("黄2"),
    YELLOW2_FLASH("黄2闪");

    private String name;

    /**
     * 判断列表内是否存在name值
     * @param name
     * @param lkjSignalEnums
     * @return
     */
    public static boolean fromValue (String name, LkjSignalEnum... lkjSignalEnums) {
        for (LkjSignalEnum each : lkjSignalEnums) {
            String n = each.getName();
             if (n.equals(name)) {
                 return true;
             }
        }
        return false;
    }
}
