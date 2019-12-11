package com.zhuzhou.impl.monitor;

import com.zhuzhou.dao.monitor.MonitorRealtimeInfoMapper;
import com.zhuzhou.entity.monitor.MonitorRealtimeInfo;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.monitor.MonitorRealtimeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实时信息 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-18
 */
@Service
public class MonitorRealtimeInfoServiceImpl extends BaseServiceImpl<MonitorRealtimeInfoMapper, MonitorRealtimeInfo> implements MonitorRealtimeInfoService {
    @Autowired
    private MonitorRealtimeInfoMapper monitorRealtimeInfoMapper;
}
