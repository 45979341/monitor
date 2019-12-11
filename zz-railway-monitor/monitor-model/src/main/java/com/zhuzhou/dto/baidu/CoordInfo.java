package com.zhuzhou.dto.baidu;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author chenzeting
 * @Date 2019-06-20
 * @Description: 百度接口映射经纬度
 **/
@Data
public class CoordInfo implements Serializable {

    private static final long serialVersionUID = -9160408366501726809L;
    /**
     * 经度
     */
    private BigDecimal x;
    /**
     * 纬度
     */
    private BigDecimal y;
}
