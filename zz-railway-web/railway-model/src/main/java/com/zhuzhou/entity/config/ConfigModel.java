package com.zhuzhou.entity.config;

import java.sql.Timestamp;

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
 * @since 2019-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_model")
public class ConfigModel implements Serializable {

    private Integer id;

    private String motorSymbol;

    private String motorName;

    private String remark;

    private String motorType;

    private Timestamp createTime;


}
