package com.zhuzhou.form.interior;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-05-16
 * @Description:
 * @see Lkj
 **/
@Data
public class LkjPhaseAnalysisForm implements Form<Lkj> {

    /**
     * 关联lkj_index的id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 项点编号
     */
    private List<Lkj> list;
}
