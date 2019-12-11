package com.zhuzhou.page.sys;

import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.page.PageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiechonghu
 * @date 2019/8/28 11:36
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuPageForm extends PageForm<SysMenu> {

    /**
     * 菜单名称
     */
    @Condition(operator = Operator.LIKE)
    private String name;
    /**
     * 请求地址
     */
    @Condition(operator = Operator.LIKE)
    private String url;
    /**
     * 权限标识
     */
    @Condition(operator = Operator.LIKE)
    private String permission;
    /**
     * 类型: (1:菜单，2：按钮，3：接口)
     */
    @Condition(operator = Operator.EQ)
    private Integer type;
}
