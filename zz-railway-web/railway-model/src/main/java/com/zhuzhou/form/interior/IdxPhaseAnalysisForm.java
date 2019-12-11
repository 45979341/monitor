package com.zhuzhou.form.interior;

import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-05-16
 * @Description:
 * @see com.zhuzhou.entity.video.Idx
 **/
@Data
public class IdxPhaseAnalysisForm implements Form<Idx> {

    /**
     * index_id 的 id
     */
    @NotEmpty(message = "id不能为空")
    private String id;

    /**
     * 关联idx_index的id
     */
    @NotEmpty(message = "recordId不能为空")
    private String recordId;

    /**
     * 项点编号
     */
    private List<Idx> list;
}
