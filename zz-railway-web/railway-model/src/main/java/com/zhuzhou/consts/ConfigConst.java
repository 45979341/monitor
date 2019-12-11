package com.zhuzhou.consts;

import com.zhuzhou.entity.config.ConfigModel;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.entity.config.ConfigSteward;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chenzeting
 * @Date 2019-03-21
 * @Description: 符号常量
 **/
public class ConfigConst {

    /**
     * 存取config_station车站数据
     * 初始化数据 2^15
     */
    public static final ConcurrentHashMap<String, ConfigStation> stationMap = new ConcurrentHashMap<>(2 << 14);

    /**
     * 存取config_model车型数据
     * 初始化数据 2^7
     */
    public static final ConcurrentHashMap<Integer, ConfigModel> modelMap = new ConcurrentHashMap(2 << 6);

    /**
     * 存取config_steward司机数据
     * 初始化数据 2^14
     */
    public static final ConcurrentHashMap<String, ConfigSteward> stewardMap = new ConcurrentHashMap(2 << 13);

    /**
     * 同步手势项点
     */
    public static final ConcurrentHashMap<String, Boolean> handMap = new ConcurrentHashMap(2 << 3);
}
