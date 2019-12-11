package com.zhuzhou.form.sys.role;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysRole
 **/
@Data
public class SysRoleAddForm implements Form {

    /**
     * 角色名称
     */
    @NotEmpty(message = "请填写角色名称")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotEmpty(message = "请设置角色权限")
    private String resIds;

    /**
     * 服务器ip
     */
    @NotEmpty(message = "请填写服务器ip")
    private String serverIp;

    /**
     * 信息端口
     */
    @NotEmpty(message = "请填写信息端口")
    private Integer infoPort;

    /**
     * 视频端口
     */
    @NotEmpty(message = "请填写视频端口")
    private Integer videoPort;

    /**
     * E用户名
     */
    @NotEmpty(message = "请填写E用户名")
    private String userName;

    /**
     * E密码
     */
    @NotEmpty(message = "请填写E密码")
    private String password;

    /**
     * 备注
     */
    private String remark;

    /**
     * E服务器ip
     */
    @NotEmpty(message = "请填写E服务器ip")
    private String eServerIp;

    /**
     * E信息端口
     */
    @NotEmpty(message = "请填写E信息端口")
    private Integer eInfoPort;

    /**
     * E视频端口
     */
    @NotEmpty(message = "请填写E视频端口")
    private Integer eVideoPort;

    /**
     * E用户名
     */
    @NotEmpty(message = "请填写E用户名")
    private String eUserName;

    /**
     * E密码
     */
    @NotEmpty(message = "请填写E密码")
    private String ePassword;
}
