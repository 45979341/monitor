package com.zhuzhou.spi.config;

import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.spi.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-11
 */
public interface ConfigStationService extends BaseService<ConfigStation> {

    /**
     * roadNum_stationNum 做为key,
     * 对象做为value
     * @return
     */
    Map<String, ConfigStation> listToMap();

    /**
     * 导入excel
     * @param excelList
     */
    void importExcel(List<ConfigStation> excelList);
}
