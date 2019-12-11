package com.zhuzhou.vo.exterior;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author chenzeting
 * @Date 2019-06-18
 * @Description:
 **/
@Data
public class RealtimeReq implements Serializable {

    private static final long serialVersionUID = 8455598257562293197L;
    private String deviceId;
    private BigDecimal gpsLng;
    private BigDecimal gpsTidu;
    private BigDecimal gpsAngle;
    private BigDecimal gpsSpeed;
    private Integer gpsDir;
    private String gpsName;
    private Date alarmTime;
    private Integer devType;
    private Integer devInfoCode;
    private Integer devRegId;
    private Integer deviceCode;
    private String carName;
    private String ccStr;
    private String trainNumStr;
    private Integer trainNum;
    private String trainStr;
    private Integer trainCode;
    private Integer trainNumber;
    private Integer driverId;
    private Integer viceDriverId;
    private String driverName;
    private String viceDriverName;
    private Integer keHuo;
    private Integer benBu;
    private Integer count;
    private Integer maxWeight;
    private Integer maxLength;
    private Integer mileStone;
    private Integer curSpeed;
    private Integer limitSpeed;
    private Integer rotateSpeed;
    private Integer pressureValue;
    private Integer brakePressure;
    private Integer signalMachineNum;
    private Integer signalMachineType;
    private Integer signalLEDType;
    private Integer signalLEDStatus;
    private Integer workCondition;
    private Integer breakOut;
    private Integer driveStatusA;
    private Integer driveStatusB;
    private Integer driverRoom;
    private Integer jcgk;
    private Integer sdgStatus;
    private Integer zdStatus;
    private Integer sbJiwei;
    private Integer reconnect;
    private Integer dazhac;
    private Integer xiaozhac;
    private Integer otherzdc;
    private Integer otherzdpc;
    private Integer nzdj;
    private Integer zdg;
    private Integer baseRoad;
    private Integer virtuRoad;
    private Integer stationNum;
    private Integer weiyi;
    private Integer fenquZhixian;
    private Integer fenquCexian;
    private Integer qianfangZhixian;
    private Integer qianfangCexian;
}
