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
 * 用户信息表
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 职务ID
     */
    private String postId;

    /**
     * 专属角色IDS
     */
    private String roleIds;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 用户性别:0男,1女
     */
    private Integer sex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 入职日期
     */
    private String joinDay;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 盐值
     */
    @JsonIgnore
    private String salt;

    /**
     * 帐号状态:0正常,1禁用
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
}
