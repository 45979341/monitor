package com.zhuzhou.form.sys.post;

import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysPost
 **/
@Data
public class PostAddForm implements Form {

    /**
     * 岗位编码
     */
    @NotEmpty(message = "请填写岗位编码")
    private String code;

    /**
     * 岗位名称
     */
    @NotEmpty(message = "请填写岗位名称")
    private String name;

    /**
     * 备注
     */
    private String remark;
}
