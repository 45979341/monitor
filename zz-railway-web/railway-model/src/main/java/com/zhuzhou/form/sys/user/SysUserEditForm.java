package com.zhuzhou.form.sys.user;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysUser
 **/
@Data
public class SysUserEditForm implements Form {

    /**
     * 用户id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 部门ID
     */
    @NotNull(message = "请选择部门")
    private String deptId;

    /**
     * 职务ID
     */
    @NotNull(message = "请选择职务")
    private String postId;

    /**
     * 专属角色IDS
     */
    @NotNull(message = "请选择角色")
    private String[] roleIds;

    /**
     * 工号
     */
    @NotNull(message = "请填写工号")
    private String jobNumber;

    /**
     * 姓名
     */
    @NotNull(message = "请填写姓名")
    private String userName;

    /**
     * 用户性别:0男,1女
     */
    @NotNull(message = "请选择性别")
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
     * 帐号状态:0正常,1禁用
     */
    @NotEmpty(message = "请选择用户状态")
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
