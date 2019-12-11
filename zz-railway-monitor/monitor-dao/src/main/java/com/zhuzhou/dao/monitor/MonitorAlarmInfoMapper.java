package com.zhuzhou.dao.monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 报警信息表 Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-17
 */
@Repository
public interface MonitorAlarmInfoMapper extends BaseMapper<MonitorAlarmInfo> {

    /**
     * 查找最大id
     * @return
     */
    Integer selectMaxId();
}
