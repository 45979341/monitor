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
public class SysUserEditPasswordForm implements Form {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 密码
     */
    @NotNull(message = "请输入密码")
    @Length(min = 6, message = "密码长度不能小于6位")
    private String password;
}
