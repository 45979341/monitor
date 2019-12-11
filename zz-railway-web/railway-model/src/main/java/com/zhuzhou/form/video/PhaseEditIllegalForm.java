package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see Phase
 **/
@Data
public class PhaseEditIllegalForm implements Form<Phase> {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 0：违章，1：正常
     */
    @NotEmpty(message = "请填写违章")
    @Min(0)
    @Max(1)
    private Integer illegal;
}
