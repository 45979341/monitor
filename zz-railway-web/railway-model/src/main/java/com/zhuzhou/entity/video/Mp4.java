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
import lombok.NoArgsConstructor;
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
@TableName("mp4")
public class Mp4 implements Serializable {

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
     * 机车号
     */
    private String cabNum;

    /**
     * 厂商名
     */
    private String factoryName;

    /**
     * 通道号
     */
    private String channelNum;

    /**
     * 通道名
     */
    private String channelName;

    /**
     * 日期项
     */
    private String prefixDate;

    /**
     * 时间项
     */
    private String suffixDate;

    /**
     * 文件大小（M）
     */
    private BigDecimal size;

    /**
     * 目录
     */
    @TableField(exist=false)
    private String dir;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}