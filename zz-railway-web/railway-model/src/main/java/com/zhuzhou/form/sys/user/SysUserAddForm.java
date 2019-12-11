package com.zhuzhou.form.sys.user;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysUser
 **/
@Data
public class SysUserAddForm implements Form {

    /**
     * 部门ID
     */
    @NotEmpty(message = "请选择部门")
    private String deptId;

    /**
     * 职务ID
     */
    @NotEmpty(message = "请选择职务")
    private String postId;

    /**
     * 专属角色IDS
     */
    @NotEmpty(message = "请选择角色")
    private String[] roleIds;

    /**
     * 工号
     */
    @NotEmpty(message = "请填写工号")
    private String jobNumber;

    /**
     * 登录账号
     */
    @NotEmpty(message = "请填写登录账号")
    private String loginName;

    /**
     * 姓名
     */
    @NotEmpty(message = "请填写姓名")
    private String userName;

    /**
     * 用户性别:0男,1女
     */
    @NotEmpty(message = "请选择性别")
    @ValueSet({"0", "1"})
    private Integer sex;

    /**
     * 生日
     */
    @NotNull(message = "请填写生日日期")
    private String birthday;

    /**
     * 入职日期
     */
    @NotNull(message = "请填写入职日期")
    private String joinDay;

    /**
     * 密码
     */
    @NotEmpty(message = "请输入密码")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;

    /**
     * 帐号状态:0正常,1禁用
     */
    @NotEmpty(message = "请选择用户状态")
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
