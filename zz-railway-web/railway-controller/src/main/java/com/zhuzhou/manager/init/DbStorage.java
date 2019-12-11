package com.zhuzhou.manager.init;

import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.entity.config.ConfigModel;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.spi.config.ConfigModelService;
import com.zhuzhou.spi.config.ConfigStationService;
import com.zhuzhou.spi.config.ConfigStewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-07-06
 * @Description: 程序加载，从数据库中读取数据到本地缓存
 **/
@Component
@Slf4j
public class DbStorage {

    @Autowired
    private ConfigStationService configStationService;
    @Autowired
    private ConfigModelService configModelService;
    @Autowired
    private ConfigStewardService configStewardService;

    @PostConstruct
    public void init(){
        log.info("——————————读取车站信息到本地内存————————————");
        List<ConfigStation> stationList = configStationService.list();
        for (ConfigStation cs : stationList) {
            ConfigConst.stationMap.put(cs.getRoadNum() + " -" + cs.getStationNum(), cs);
        }

        log.info("——————————读取车型信息到本地内存————————————");
        List<ConfigModel> modelList= configModelService.list();
        for (ConfigModel cm : modelList) {
            ConfigConst.modelMap.put(cm.getId(), cm);
        }

        log.info("——————————读取司机信息到本地内存————————————");
        List<ConfigSteward> stewardList = configStewardService.list();
        for (ConfigSteward cs : stewardList) {
            ConfigConst.stewardMap.put(cs.getJobNum()+"", cs);
        }
    }

    @PreDestroy
    public void destroy(){
        log.info("执行终止");
    }
}
