package com.zhuzhou.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author wangxiaokuan
 * @Date 2019-12-02
 * @Description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHistoryResp implements Serializable {

    private Integer alarmId;
    private String deviceId;
    private BigDecimal gpsLng;
    private BigDecimal gpsTidu;
    private String ccStr = "";
    private String trainCode = "";
    private Date alarmTime;
    private Integer alarmEvent = 0;
    private String alarmEventName = "";
}
