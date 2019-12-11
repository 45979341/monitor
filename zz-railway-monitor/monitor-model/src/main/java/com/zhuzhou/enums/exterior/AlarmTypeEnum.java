package com.zhuzhou.enums.exterior;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author chenzeting
 * @Date 2019-06-19
 * @Description: 手势信号枚举
 **/
@AllArgsConstructor
@Getter
public enum AlarmTypeEnum {

    PASS(1, 1, "手势识别", "通过信号"),
    READY_STOP(2, 1, "手势识别", "准备停车信号"),
    CX_STOP(3, 1, "手势识别", "侧线运行信号"),
    STOP(4, 1, "手势识别", "停车信号"),
    OUT_HEAD(5, 1, "手势识别", "窗外探头"),
    ONE_FAG(6, 2, "盹睡识别", "一级疲劳"),
    TWO_FAG(7, 2, "盹睡识别", "二级疲劳"),
    THREE_FAG(8, 2, "盹睡识别", "三级疲劳"),
    LEAVE_POST(9, 2, "盹睡识别", "驾驶姿势不正确（离岗）"),
    KEEP_OUT(10, 2, "盹睡识别", "遮挡"),
    SMOKING(11, 1, "手势识别", "抽烟"),
    PHONE(12, 1, "手势识别", "打手机"),
    GREEN_LIGHT(13, 3, "语音识别", "绿灯"),
    YELLOW_LIGHT(14, 3, "语音识别", "黄灯");

    public Integer id;
    public Integer typeId;
    public String AlarmTypeEnum;
    public String alarmEvent;

    public static AlarmTypeEnum getById(Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        for (AlarmTypeEnum enums : values()) {
            if (enums.id.equals(id)) {
                return enums;
            }
        }
        return null;
    }
}
