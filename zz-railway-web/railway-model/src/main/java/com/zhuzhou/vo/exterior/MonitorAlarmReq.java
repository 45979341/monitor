package com.zhuzhou.vo.exterior;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-18
 * @Description:
 **/
@Data
public class MonitorAlarmReq implements Serializable {

    private static final long serialVersionUID = 4277938865052219991L;

    private String deviceId;
    private Integer itemType;
    private Integer workCondition;
    private Date startTime;
    private Date endTime;
    private Integer alarmChn;
    private Integer analyzeStatus = 1;
    private List<String> alarmFilePaths;
    private RealtimeReq rt;
}
