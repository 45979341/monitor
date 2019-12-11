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
 * 项点分数表
 * </p>
 *
 * @author chenzeting
 * @since 2019-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_phase")
public class ConfigPhase implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 类型标识（LKJ, IDX, H）
     */
    private String type;

    /**
     * 项点名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 是否为必分析项点（0：否，1：是）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Timestamp createTime;


}
