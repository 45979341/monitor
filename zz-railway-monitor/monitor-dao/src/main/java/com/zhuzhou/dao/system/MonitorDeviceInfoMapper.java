package com.zhuzhou.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhou.entity.system.MonitorDeviceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 设备管理
 * </p>
 * @Author: chenzeting
 * Date:     2019-12-04
 * Description:
 */
@Repository
public interface MonitorDeviceInfoMapper extends BaseMapper<MonitorDeviceInfo> {
    /**
     * 获取设备id
     * @return
     */
    public List<String> getDeviceId();
}
