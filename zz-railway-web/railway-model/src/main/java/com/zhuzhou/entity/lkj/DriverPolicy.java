package com.zhuzhou.entity.lkj;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("driver_policy")
public class DriverPolicy implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联lkj_index的id
     */
    private String lkjIndexId;

    /**
     * 车站名称
     */
    private String stationName;

    /**
     * 到达时间
     */
    private Timestamp arriveTime;

    /**
     * 通过时间
     */
    private Timestamp passTime;

    /**
     * 站内停车
     */
    private String innerStationTime;

    /**
     * 晚点
     */
    private Integer goodnight;

    /**
     * 区间
     */
    private String sectionTime;

    /**
     * 车辆数
     */
    private Integer carNum;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 交路号
     */
    private Integer roadNum;

    /**
     * 总重
     */
    private Integer weight;

    /**
     * 换长
     */
    private BigDecimal length;

    /**
     * 创建时间
     */
    private Timestamp createTime;


}
