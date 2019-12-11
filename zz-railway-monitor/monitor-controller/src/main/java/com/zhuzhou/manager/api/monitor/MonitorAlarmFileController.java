package com.zhuzhou.manager.api.monitor;

import com.zhuzhou.spi.monitor.MonitorAlarmFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 报警关联文件信息 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-06-18
 * Description:
 */
@Controller
public class MonitorAlarmFileController {
    @Autowired
    private MonitorAlarmFileService monitorAlarmFileService;
}
