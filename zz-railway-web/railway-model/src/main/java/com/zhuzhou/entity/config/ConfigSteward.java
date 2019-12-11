package com.zhuzhou.entity.config;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_steward")
public class ConfigSteward implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 铁路局
     */
    private String railway;

    /**
     * 机务段
     */
    private String locomotiveDepot;

    /**
     * 运用车间
     */
    private String workshop;

    /**
     * 工号
     */
    private Integer jobNum;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职名
     */
    private String pos;

    /**
     * 联系方式
     */
    private String tel;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
