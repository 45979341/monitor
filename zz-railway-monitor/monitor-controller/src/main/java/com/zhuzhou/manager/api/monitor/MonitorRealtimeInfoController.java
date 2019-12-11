package com.zhuzhou.manager.api.monitor;

import com.zhuzhou.spi.monitor.MonitorRealtimeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 实时信息 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-06-18
 * Description:
 */
@Controller
public class MonitorRealtimeInfoController {
    @Autowired
    private MonitorRealtimeInfoService monitorRealtimeInfoService;
}
