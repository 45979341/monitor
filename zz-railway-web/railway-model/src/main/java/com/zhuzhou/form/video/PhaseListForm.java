package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see com.zhuzhou.entity.video.Phase
 **/
@Data
public class PhaseListForm implements Form<Phase> {

    @NotEmpty(message = "id不能为空")
    @Condition
    private String recordId;

    @Condition
    private String code;

    @Order(direction = Sort.Direction.ASC)
    private Date startTime;
}
