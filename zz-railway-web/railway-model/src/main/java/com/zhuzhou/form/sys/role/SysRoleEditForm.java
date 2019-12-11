package com.zhuzhou.form.sys.role;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysRole
 **/
@Data
public class SysRoleEditForm implements Form {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;
    /**
     * 角色名称
     */
    @NotNull(message = "请填写角色名称")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotNull(message = "请设置角色权限")
    private String[] resIds;

    /**
     * 服务器ip
     */
    @NotNull(message = "请填写服务器ip")
    private String serverIp;

    /**
     * 信息端口
     */
    @NotNull(message = "请填写信息端口")
    private Integer infoPort;

    /**
     * 视频端口
     */
    @NotNull(message = "请填写视频端口")
    private Integer videoPort;

    /**
     * E用户名
     */
    @NotNull(message = "请填写E用户名")
    private String userName;

    /**
     * E密码
     */
    @NotNull(message = "请填写E密码")
    private String password;

    /**
     * 角色状态:（0正常,1禁用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * E服务器ip
     */
    @NotNull(message = "请填写E服务器ip")
    private String eServerIp;

    /**
     * E信息端口
     */
    @NotNull(message = "请填写E信息端口")
    private Integer eInfoPort;

    /**
     * E视频端口
     */
    @NotNull(message = "请填写E视频端口")
    private Integer eVideoPort;

    /**
     * E用户名
     */
    @NotNull(message = "请填写E用户名")
    private String eUserName;

    /**
     * E密码
     */
    @NotNull(message = "请填写E密码")
    private String ePassword;
}
