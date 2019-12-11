package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see com.zhuzhou.entity.video.Mp4
 **/
@Data
public class Mp4ListForm implements Form<Mp4> {

    @NotEmpty(message = "id不能为空")
    @Condition
    private String recordId;

    @Order(direction = Sort.Direction.ASC)
    private String prefixDate;

    @Order(direction = Sort.Direction.ASC)
    private String suffixDate;
}
