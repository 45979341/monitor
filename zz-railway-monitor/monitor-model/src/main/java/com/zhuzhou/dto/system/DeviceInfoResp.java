package com.zhuzhou.dto.system;

import com.zhuzhou.entity.system.MonitorDeviceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeviceInfoResp implements Serializable {
    private List<MonitorDeviceInfo> data;
    private Long total;
    private Long currentPage;
    private Long pageSize;
}
