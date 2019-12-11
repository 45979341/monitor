package com.zhuzhou.form.lkj;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-05-16
 * @Description:
 * @see ItemRecord
 **/
@Data
public class ItemRecordItemIdForm implements Form<ItemRecord> {

    /**
     * 关联lkj_index的id
     */
    @NotEmpty(message = "id不能为空")
    private String lkjId;

    /**
     * 项点编号
     */
    @NotEmpty(message = "项点编号不能为空")
    private Integer itemId;
}
