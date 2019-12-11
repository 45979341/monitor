package com.zhuzhou.impl.monitor;

import com.zhuzhou.dao.monitor.MonitorAlarmFileMapper;
import com.zhuzhou.entity.monitor.MonitorAlarmFile;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.monitor.MonitorAlarmFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报警关联文件信息 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-18
 */
@Service
public class MonitorAlarmFileServiceImpl extends BaseServiceImpl<MonitorAlarmFileMapper, MonitorAlarmFile> implements MonitorAlarmFileService {
    @Autowired
    private MonitorAlarmFileMapper monitorAlarmFileMapper;
}
