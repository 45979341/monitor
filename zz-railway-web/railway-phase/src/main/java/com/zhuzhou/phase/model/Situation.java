package com.zhuzhou.phase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author xiechonghu
 * @date 2019/11/8 11:05
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Situation {
    /**
     * 状态名称
     */
    private String situation;
    /**
     * 开始索引
     */
    private Integer start;
    /**
     * 结束索引
     */
    private Integer end;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    @Override
    public String toString() {
        return situation
                + "\n" + "开始索引：" + start
                + "\n" + "结束索引：" + end
                + "\n" + "开始时间：" + startTime
                + "\n" + "结束时间：" + endTime
                +"\n-------------------------------------";
    }
}
