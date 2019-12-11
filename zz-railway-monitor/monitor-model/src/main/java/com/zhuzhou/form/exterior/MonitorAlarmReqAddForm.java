package com.zhuzhou.form.exterior;

import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.entity.monitor.MonitorRealtimeInfo;
import com.zhuzhou.framework.form.Form;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-17
 * @Description:
 * @see MonitorAlarmInfo
 * @see MonitorRealtimeInfo
 **/
@Data
public class MonitorAlarmReqAddForm implements Form {
    private String deviceId;
    private Integer itemType;
    private Integer workCondition;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer alarmChn;
    private Integer analyzeStatus = 1;
    private List<String> alarmFilePaths;
    private MonitorRealtimeInfo rt;
}
