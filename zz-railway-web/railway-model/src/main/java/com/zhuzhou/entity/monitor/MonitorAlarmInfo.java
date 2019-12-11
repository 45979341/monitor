package com.zhuzhou.entity.monitor;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报警信息表
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("monitor_alarm_info")
public class MonitorAlarmInfo implements Serializable {

    /**
     * 报警id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 设备标识
     */
    private String deviceId;

    /**
     * 车号编码
     */
    private String trainCode;

    /**
     * 厂家名称
     */
    private String manufactor;

    /**
     * 车次字串
     */
    private String ccStr;

    /**
     * 报警类型
     */
    private Integer alarmType;

    /**
     * 报警事件
     */
    private Integer alarmEvent;

    /**
     * 工况
     */
    private Integer workCondition;

    /**
     * 通道号
     */
    private Integer alarmChn;

    /**
     * 司机号
     */
    private Integer driverId;

    /**
     * 副司机号
     */
    private Integer viceDriverId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 报警时间
     */
    private Date alarmTime;

    /**
     * 经度
     */
    private BigDecimal gpsLng;

    /**
     * 纬度
     */
    private BigDecimal gpsTidu;

    /**
     * 识别状态 0:正常 1:异常
     */
    private Integer analyzeStatus;

    /**
     * 审核状态 0:未审核 1:审核通过 2:审核不通过
     */
    private Integer auditStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 状态:0正常,1禁用
     */
    private Integer status;

    /**
     * 是否删除:0正常,1删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 备注
     */
    private String remark;


}
