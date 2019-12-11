package com.zhuzhou.vo.lkj;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author chenzeting
 * @Date 2019-05-16
 * @Description:
 **/
@Data
public class ItemRecordVo implements Serializable {

    /**
     * 违章数量
     */
    private int count;

    /**
     * 标准数量
     */
    private int standCount;

    /**
     * 违章标准（0：违章，1：正常，2：记录）
     */
    private Integer illegalStand;
}
