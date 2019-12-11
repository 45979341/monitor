package com.zhuzhou.form.config;

import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Author chenzeting
 * @Date 2019-07-24
 * @Description:
 * @see ConfigPhase
 **/
@Data
public class ConfigPhaseScoreEditForm implements Form<ConfigPhase> {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 分数
     */
    @Min(value = 0, message = "分数最少定义0分")
    @Max(value = 100, message = "分数最高定义100分")
    private Integer score;
}
