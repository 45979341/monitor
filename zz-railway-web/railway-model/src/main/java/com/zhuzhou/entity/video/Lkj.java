package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("lkj")
public class Lkj implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 关联upload_record记录id
     */
    private String recordId;

    /**
     * 序号
     */
    private Integer lkjNo;

    /**
     * 事件
     */
    private String eventItem;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date time;

    /**
     * 公里标
     */
    private Double kiloMeter;

    /**
     * 其它
     */
    private String other;

    /**
     * 距离
     */
    private Integer distance;

    /**
     * 信号机
     */
    private String signalMachine;

    /**
     * 信号
     */
    private String signals;

    /**
     * 速度
     */
    private Integer speed;

    /**
     * 限速
     */
    private String rateLimit;

    /**
     * 零位
     */
    private String zeroBit;

    /**
     * 牵引
     */
    private String transaction;

    /**
     * 前后
     */
    private String frontBehind;

    /**
     * 管压
     */
    private String pipePressure;

    /**
     * 缸压
     */
    private String cylinderPressure;

    /**
     * 转速电流
     */
    private String speedElectricity;

    /**
     * 均缸1
     */
    private String cylinder1;

    /**
     * 均缸2
     */
    private String cylinder2;
}
