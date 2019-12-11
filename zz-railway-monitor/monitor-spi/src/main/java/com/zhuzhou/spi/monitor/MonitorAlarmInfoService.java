package com.zhuzhou.spi.monitor;

import com.zhuzhou.dto.history.AlarmHistoryInfoResp;
import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.form.monitor.MonitorAlarmInfoListForm;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 * 报警信息表 服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-17
 */
public interface MonitorAlarmInfoService extends BaseService<MonitorAlarmInfo> {
    /**
     * 获取历史报警信息
     * @param form
     * @return
     */
    List<AlarmHistoryInfoResp> getAlarmHistoryInfo(MonitorAlarmInfoListForm form);
}
