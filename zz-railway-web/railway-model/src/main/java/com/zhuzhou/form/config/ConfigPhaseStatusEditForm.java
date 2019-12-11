package com.zhuzhou.form.config;

import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author chenzeting
 * @Date 2019-05-20
 * @Description:
 * @see ConfigPhase
 **/
@Data
public class ConfigPhaseStatusEditForm implements Form<ConfigPhase> {

    @Condition
    @NotEmpty(message = "id不能为空")
    private String id;

    @Condition
    @ValueSet(value = {"0", "1"}, message = "请输入状态")
    private Integer status;
}
