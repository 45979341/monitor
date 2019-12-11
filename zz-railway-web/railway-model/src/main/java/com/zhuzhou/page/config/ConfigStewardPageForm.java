package com.zhuzhou.page.config;

import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.page.PageForm;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-07-17
 * @Description:
 * @see com.zhuzhou.entity.config.ConfigSteward
 **/
@Data
public class ConfigStewardPageForm extends PageForm<ConfigSteward> {

    /**
     * 铁路局
     */
    @Condition(operator = Operator.LIKE)
    private String railway;

    /**
     * 机务段
     */
    @Condition(operator = Operator.LIKE)
    private String locomotiveDepot;

    /**
     * 运用车间
     */
    @Condition(operator = Operator.LIKE)
    private String workshop;

    /**
     * 工号
     */
    @Condition(operator = Operator.LIKE)
    private Integer jobNum;

    /**
     * 姓名
     */
    @Condition(operator = Operator.LIKE)
    private String name;
}
