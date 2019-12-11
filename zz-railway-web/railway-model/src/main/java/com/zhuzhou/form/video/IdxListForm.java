package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-04-06
 * @Description:
 * @see Idx
 **/
@Data
public class IdxListForm implements Form<Idx> {

    @NotEmpty(message = "id不能为空")
    @Condition
    private String id;
}
