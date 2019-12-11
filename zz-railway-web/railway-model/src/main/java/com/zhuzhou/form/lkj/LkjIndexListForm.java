package com.zhuzhou.form.lkj;

import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.page.PageForm;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-03-28
 * @Description:
 * @see com.zhuzhou.entity.lkj.LkjIndex
 **/
@Data
public class LkjIndexListForm extends PageForm<LkjIndex> {

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
     * 车次
     */
    @Condition(operator = Operator.LIKE)
    private String trainNum;

    /**
     * 车型
     */
    @Condition(operator = Operator.LIKE)
    private String trainType;

    /**
     * 机车号
     */
    @Condition(operator = Operator.LIKE)
    private String motorNum;

    /**
     * 开始时间
     */
    @Condition(operator = Operator.BETWEEN_LOWER, property = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @Condition(operator = Operator.BETWEEN_UPPER, property = "createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 是否有视频 1有视频 0无视频
     */
    private String video;

    /**
     * 相点解析状态(0: 未处理，1：已处理，2：视频缺失，3：正在处理)
     */
    private Integer phaseStatus;

    @Order
    private Timestamp createTime;
}
