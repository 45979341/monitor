package com.zhuzhou.es.monitor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author wangxiaokuan
 * @Date 2019-12-02
 * @Description: 报警信息
 **/
@Data
@Document(indexName = "alarm_info",type = "doc")
public class MonitorAlarmInfo {

    /**
     * 文件id
     */
    @Id
    private Integer id;
    private String deviceId;
    private String trainCode;
    private String manufactor;
    private String ccStr;
    private Integer alarmType;
    private Integer alarmEvent;
    private Integer workCondition;
    private Integer alarmChn;
    private Integer driverId;
    private Integer viceDriverId;
    private Date startTime;
    private Date endTime;
    private Date alarmTime;
    private Integer analyzeStatus;
    private Integer auditStatus;
    private String auditOpinion;
    private Integer status;
    private Integer isDelete;
    private String remark;
    private BigDecimal gpsLng;
    private BigDecimal gpsTidu;
}
