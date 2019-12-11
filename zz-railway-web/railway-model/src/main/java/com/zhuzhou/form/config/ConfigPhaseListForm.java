package com.zhuzhou.form.config;

import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-07-24
 * @Description:
 * @see ConfigPhase
 **/
@Data
public class ConfigPhaseListForm implements Form<ConfigPhase> {

    /**
     * 类型标识（LKJ, IDX, H）
     */
    @Condition(operator = Operator.IN)
    private String type;

    /**
     * 项点名称
     */
    @Condition(operator = Operator.LIKE)
    private String name;
}
