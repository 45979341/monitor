package com.zhuzhou.form.video;

import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.framework.validate.annotation.NotEmpty;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-04-06
 * @Description:
 * @see IdxAnalysisLog
 **/
@Data
public class IdxAnalysisLogDetailForm implements Form<IdxAnalysisLog> {

    /**
     * 关联idx_index的id
     */
    @Condition
    @NotEmpty(message = "record_id不能为空")
    private String recordId;

    /**
     * 项点id
     */
    @Condition
    @NotEmpty(message = "项点id不能为空")
    private String phaseId;
}
