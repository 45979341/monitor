package com.zhuzhou.form.sys.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysUser
 **/
@Data
public class SysUserLoginForm implements Form {
    /**
     * 登录账号
     */
    @NotEmpty(message = "用户或密码不能为空")
    private String loginName;
    /**
     * 密码
     */
    @NotEmpty(message = "用户或密码不能为空")
    private String password;
}
