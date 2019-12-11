package com.zhuzhou.dto.history;

import lombok.Data;
import org.elasticsearch.common.util.concurrent.PrioritizedCallable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiechonghu
 * @date 2019/12/5 16:18
 * @description:
 * 历史报警信息
 */
@Data
public class AlarmHistoryInfoResp implements Serializable {

    /**
     * 设备标识
     */
    private String deviceId;
    /**
     * 车号编码
     */
    private String trainCode;
    /**
     * 车次
     */
    private String ccStr;
    /**
     * 司机号
     */
    private Integer driverId;

    private Date startTime;

    private Date endTime;
    /**
     * 报警总数
     */
    private Integer alarm;
    /**
     * 正常
     */
    private Integer normal;
    /**
     * 异常
     */
    private Integer unNormal;
    /**
     * 未审核
     */
    private Integer unchecked;
    /**
     * 已审核
     */
    private Integer checked;
    /**
     * 审核不通过
     */
    private Integer noPass;

}
