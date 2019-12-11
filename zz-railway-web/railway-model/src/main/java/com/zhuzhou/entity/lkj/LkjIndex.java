package com.zhuzhou.entity.lkj;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenzeting
 * @since 2019-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("lkj_index")
public class LkjIndex implements Serializable {
    /**
     * 序号
     */
    private String id;

    /**
     * 开车日期
     */
    private String driverDate;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 车型
     */
    private String trainType;

    /**
     * 机车号
     */
    private String motorNum;

    /**
     * 本补
     */
    private String keHuo;

    /**
     * 走行
     */
    private Integer walk;

    /**
     * 长度
     */
    private BigDecimal length;

    /**
     * 数据交路
     */
    private Integer roadNum;

    /**
     * 司机
     */
    private String driverNum;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 副司机
     */
    private String assisDriverNum;

    /**
     * 副司机名
     */
    private String assisDriverName;

    /**
     * 起点站
     */
    private String originStation;

    /**
     * 终点站
     */
    private String terminus;

    /**
     * 分析日期
     */
    private Date analysisDate;

    /**
     * 分析人
     */
    private String analysisName;

    /**
     * 分数
     */
    @TableField(exist = false)
    private long score = 100;

    /**
     * 审核人
     */
    private String examineName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件最后修改时间
     */
    private Timestamp fileModifyTime;

    /**
     * lkj文件大小
     */
    private BigDecimal size;

    /**
     * api调用状态
     */
    private Integer apiStatus;

    /**
     * lkj分析状态
     */
    private Integer lkjStatus;

    /**
     * lkj文件名
     */
    private String fileName;

    /**
     * 是否有视频
     */
    @TableField(exist = false)
    private int isVideo;

    /**
     * 项点解析状态
     */
    @TableField(exist = false)
    private Integer phaseStatus;
}
