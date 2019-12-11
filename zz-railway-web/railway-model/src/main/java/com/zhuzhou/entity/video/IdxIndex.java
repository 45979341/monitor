package com.zhuzhou.entity.video;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("idx_index")
public class IdxIndex implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 关联lkj首页id
     */
    private String recordId;

    /**
     * idx路径
     */
    private String fileName;

    /**
     * 文件目录
     */
    private String dir;

    /**
     * http路径
     */
    private String url;

    /**
     * 机车号
     */
    private String cabNum;

    /**
     * 车次
     */
    private String trainNum;

    /**
     * 司机号
     */
    private String driverNum;

    /**
     * 司机名
     */
    private String driverName;

    /**
     * 文件数
     */
    private Integer fileNum;

    /**
     * 日期
     */
    private String date;

    /**
     * 文件大小（KB）
     */
    private Integer size;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 文件修改时间
     */
    private Timestamp fileModifyTime;

    /**
     * api调用状态
     */
    private Integer apiStatus;

    /**
     * 创建时间
     */
    private Timestamp createTime;


}
