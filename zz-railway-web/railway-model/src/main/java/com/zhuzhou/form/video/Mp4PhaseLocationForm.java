package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see Mp4
 **/
@Data
public class Mp4PhaseLocationForm implements Form<Mp4> {

    @NotEmpty(message = "id不能为空")
    @Condition
    private String recordId;

    @NotEmpty(message = "请输入时间")
    @Condition(operator = Operator.BETWEEN_LOWER, property = "suffix_date")
    private String start;

    @NotEmpty(message = "请输入时间")
    @Condition(operator = Operator.BETWEEN_UPPER, property = "suffix_date")
    private String end;
}
