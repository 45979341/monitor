package com.zhuzhou.form.lkj;

import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see LkjIndex
 **/
@Data
public class LkjIndexProcessListForm implements Form<LkjIndex> {

    /**
     * 天数
     */
    @Min(0)
    private Integer count;

    @Order
    @Condition(operator = Operator.BETWEEN_LOWER)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
