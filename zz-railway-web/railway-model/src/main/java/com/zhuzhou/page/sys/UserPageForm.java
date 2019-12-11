package com.zhuzhou.page.sys;

import com.zhuzhou.entity.sys.SysUser;
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
 * @see com.zhuzhou.entity.sys.SysUser
 **/
@Data
public class UserPageForm extends PageForm<SysUser> {

    /**
     * 工号
     */
    @Condition(operator = Operator.LIKE)
    private String jobNumber;

    /**
     * 姓名
     */
    @Condition(operator = Operator.LIKE)
    private String userName;

    /**
     * 创建时间
     */
    @Order
    private Timestamp createTime;
}
