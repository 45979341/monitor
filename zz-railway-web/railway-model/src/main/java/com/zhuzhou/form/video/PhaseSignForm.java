package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see Phase
 **/
@Data
public class PhaseSignForm implements Form<Phase> {

    /**
     * id
     */
    @NotEmpty(message = "id不能为空")
    private String id;
    /**
     * 是否违章(0：违章，1：正常，2：视频缺失)
     */
    @ValueSet({"0","1","2"})
    private Integer illegal;

    /**
     * 分析内容
     */
    @NotNull(message = "请填写分析内容")
    private String content;

    /**
     * 存在问题
     */
    @NotNull(message = "请填写存在问题")
    private String question;

    /**
     * 分析意见
     */
    @NotNull(message = "请填写分析意见")
    private String opinion;
}
