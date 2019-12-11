package com.zhuzhou.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 设备信息
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("monitor_device_info")
public class MonitorDeviceInfo implements Serializable {

    @TableId
    private String deviceId;
    private String oldDeviceId;
    private String firstOrgName;
    private String seconOrgName;
    private String thirdOrgName;
    private Integer cameraNumber;
    private String cameraName;
    private Integer hadInfo;
    private Integer inLine;
    private Integer devType;
    private Integer devInfoCode;
    private Integer devRegId;
    private Integer deviceCode;
    private String carName;
    private Integer deptId;
    private String manufactor;
    private String hardwareVersion;
    private String softwareVersion;
    private Integer status;
    private Integer isDelete;
    private String remark;
}
