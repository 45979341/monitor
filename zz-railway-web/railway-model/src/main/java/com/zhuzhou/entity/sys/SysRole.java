package com.zhuzhou.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole implements Serializable {

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String menuId;

    /**
     * 服务器ip
     */
    private Integer serverIp;

    /**
     * 信息端口
     */
    private Integer infoPort;

    /**
     * 视频端口
     */
    private Integer videoPort;

    /**
     * 用户名
     */
    @JsonIgnore
    private String userName;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 角色状态:（0正常,1禁用）
     */
    private Integer status;

    /**
     * 创建者
     */
    @TableField(select = false)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(select = false)
    private Timestamp createTime;

    /**
     * 更新者
     */
    @TableField(select = false)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(select = false)
    private Timestamp updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * E服务器ip
     */
    private Integer eServerIp;

    /**
     * E信息端口
     */
    private Integer eInfoPort;

    /**
     * E视频端口
     */
    private Integer eVideoPort;

    /**
     * E用户名
     */
    private String eUserName;

    /**
     * E密码
     */
    private String ePassword;

    @TableField(exist = false)
    private String tempServerIp;

    @TableField(exist = false)
    private String tempEServerIp;
}
