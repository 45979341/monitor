package com.zhuzhou.spi.config;

import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-18
 */
public interface ConfigStewardService extends BaseService<ConfigSteward> {

    /**
     * 导入司机数据
     * @param excelList
     */
    void importExcel(List<ConfigSteward> excelList);
}
