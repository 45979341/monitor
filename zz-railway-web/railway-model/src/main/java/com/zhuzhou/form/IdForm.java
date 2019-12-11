package com.zhuzhou.form;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.Phone;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-01-03
 * @Description:
 **/
@Data
public class IdForm implements Form {

    @NotEmpty(message = "id不能为空")
    private String id;
}
