package com.zhuzhou.page.sys;

import com.zhuzhou.entity.sys.SysRole;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.page.PageForm;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysRole
 **/
@Data
public class RolePageForm extends PageForm<SysRole> {

    /**
     * 角色名称
     */
    @Condition(operator = Operator.LIKE)
    private String roleName;

    /**
     * 创建时间
     */
    @Order
    private Timestamp createTime;
}
