package com.zhuzhou.form.video;

import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.page.PageForm;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see com.zhuzhou.entity.video.UploadRecord
 **/
@Data
public class IdxIndexListForm extends PageForm<IdxIndex> {

    /**
     * id
     */
    private String id;

    @Condition
    private String recordId;

    /**
     * 司机号
     */
    @Condition(operator = Operator.LIKE)
    private String driverNum;

    /**
     * 司机名
     */
    @Condition(operator = Operator.LIKE)
    private String driverName;

    /**
     * 机车号
     */
    @Condition(operator = Operator.LIKE)
    private String cabNum;

    /**
     * 车次
     */
    @Condition(operator = Operator.LIKE)
    private String trainNum;

    @Order
    private Timestamp createTime;
}
