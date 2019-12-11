package com.zhuzhou.manager.api.monitor;

import com.sun.org.apache.regexp.internal.RE;
import com.zhuzhou.dto.history.AlarmHistoryResp;
import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.form.monitor.MonitorAlarmInfoListForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.baidu.BaiduGpsService;
import com.zhuzhou.spi.monitor.MonitorAlarmHistoryService;
import com.zhuzhou.spi.monitor.MonitorAlarmInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 报警信息表 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-06-17
 * Description:
 */
@Controller
public class MonitorAlarmInfoController {
    @Autowired
    private MonitorAlarmInfoService monitorAlarmInfoService;
    @Autowired
    private BaiduGpsService baiduGpsService;
    @Autowired
    private MonitorAlarmHistoryService monitorAlarmHistoryService;
    /**
     * 历史报警列表
     * @param form
     * @return
     */
    @OAuth(required = false)
    @GetMapping("/history/alarm/list")
    public Result list(MonitorAlarmInfoListForm form) {
        monitorAlarmInfoService.getAlarmHistoryInfo(form);
        return Result.success().setData("sda");
    }

    /**
     * 历史轨迹
     * @param form
     * @return
     */
    @OAuth(required = false)
    @GetMapping("history/getAlarmHistory")
    public Result historyTrail(MonitorAlarmInfoListForm form) {
        Map<String, List<AlarmHistoryResp>> map = monitorAlarmHistoryService.getHistoryAlarmTrail(form);
        return Result.success().setData(map);
    }
}
