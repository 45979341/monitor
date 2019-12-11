package com.zhuzhou.form.ai;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
@Data
public class RecordIdForm implements Form {

    @NotEmpty(message = "recordId不能为空")
    private String recordId;
}
