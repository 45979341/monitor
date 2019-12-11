package com.zhuzhou.spi.system;

import com.zhuzhou.entity.system.MonitorDeviceInfo;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 * 设备管理
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-04
 */
public interface MonitorDeviceInfoService extends BaseService<MonitorDeviceInfo> {
    /**
     * 获取设备id
     * @return
     */
    public List<String> getDiviceId();
}
