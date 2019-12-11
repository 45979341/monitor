package com.zhuzhou.page.config;

import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.page.PageForm;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-07-17
 * @Description:
 * @see com.zhuzhou.entity.config.ConfigStation
 **/
@Data
public class ConfigStationPageForm extends PageForm<ConfigStation> {

    /**
     * 交路号
     */
    @Condition
    private Integer roadNum;

    /**
     * 车站号
     */
    @Condition
    private Integer stationNum;

    /**
     * 车站名
     */
    @Condition(operator = Operator.LIKE)
    private String stationName;
}
