package com.zhuzhou.form.monitor;

import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-06-19
 * @Description:
 **/
@Data
public class MonitorAlarmInfoListForm implements Form<MonitorAlarmInfo> {

    /**
     * 车号编码
     */
    @Condition
    private String trainCode;

    /**
     * 车次字串
     */
    @Condition
    private String ccStr;

    /**
     * 设备标识
     */
    @Condition(operator = Operator.IN)
    private String deviceId;

    /**
     * 司机号
     */
    @Condition
    private Integer driverId;

    /**
     * 报警时间
     */
    @Condition(operator = Operator.APPLY, property = "")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date alarmTime;
}
