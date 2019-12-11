package com.zhuzhou.spi.monitor;

import com.zhuzhou.dto.history.AlarmHistoryResp;
import com.zhuzhou.form.monitor.MonitorAlarmInfoListForm;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 历史信息接口类
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-02
 */
public interface MonitorAlarmHistoryService {

    /**
     * 获取历史轨迹
     * @param form
     * @return
     */
    public Map<String , List<AlarmHistoryResp>> getHistoryAlarmTrail(MonitorAlarmInfoListForm form);
}
