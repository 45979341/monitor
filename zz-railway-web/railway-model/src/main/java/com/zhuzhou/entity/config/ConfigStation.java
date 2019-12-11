package com.zhuzhou.entity.config;

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
 * @since 2019-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_station")
public class ConfigStation implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 交路号
     */
    private Integer roadNum;

    /**
     * 车站号
     */
    private Integer stationNum;

    /**
     * 车站名
     */
    private String stationName;

    /**
     * 线路号
     */
    private Integer lineNum;

    /**
     * Tmis编号
     */
    private Integer tmisNum;

    /**
     * 上行进站信号机编号
     */
    private Integer upEnter;

    /**
     * 上行出站信号机编号
     */
    private Integer upExit;

    /**
     * 下行进站信号机编号
     */
    private Integer downEnter;

    /**
     * 下行出站信号机编号
     */
    private Integer downExit;

    /**
     * G0
     */
    private Integer g0;

    /**
     * G1
     */
    private Integer g1;

    /**
     * G2
     */
    private Integer g2;

    /**
     * G3
     */
    private Integer g3;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
