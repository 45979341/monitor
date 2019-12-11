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
public class IdxAnalysisLogListForm implements Form<IdxAnalysisLog> {

    /**
     * 关联idx_index的id
     */
    @Condition
    @NotEmpty(message = "id不能为空")
    private String recordId;

    /**
     * 开始时间
     */
    @Condition(operator = Operator.BETWEEN_LOWER, property = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    /**
     * 结束时间
     */
    @Condition(operator = Operator.BETWEEN_UPPER, property = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    /**
     * 1：本用户，2：所有用户
     */
    @ValueSet({"1","2"})
    @Condition
    private String createBy;

    /**
     * 排序
     */
    @Order
    private Timestamp createTime;
}
