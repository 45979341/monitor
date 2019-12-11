package com.zhuzhou.impl.system;

import com.zhuzhou.dao.system.MonitorDeviceInfoMapper;
import com.zhuzhou.entity.system.MonitorDeviceInfo;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.system.MonitorDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 设备管理
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-17
 */
@Service
public class MonitorDeviceInfoServiceImpl extends BaseServiceImpl<MonitorDeviceInfoMapper, MonitorDeviceInfo> implements MonitorDeviceInfoService {

    @Autowired
    private MonitorDeviceInfoMapper deviceInfoMapper;

    @Override
    public List<String> getDiviceId() {
        return deviceInfoMapper.getDeviceId();
    }
}
