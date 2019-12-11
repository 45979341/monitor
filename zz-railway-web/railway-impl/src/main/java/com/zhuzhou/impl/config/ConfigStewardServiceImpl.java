package com.zhuzhou.impl.config;

import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.dao.config.ConfigStewardMapper;
import com.zhuzhou.spi.config.ConfigStewardService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-18
 */
@Service
public class ConfigStewardServiceImpl extends BaseServiceImpl<ConfigStewardMapper, ConfigSteward> implements ConfigStewardService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(List<ConfigSteward> excelList) {
        //删除数据
        removeAll();
        List<ConfigSteward> collect = excelList.parallelStream().filter(distinctByKey(ConfigSteward::getJobNum)).collect(Collectors.toList());

        //批量添加车站数据
        saveBatch(collect);
        for (ConfigSteward cs : collect) {
            ConfigConst.stewardMap.put(cs.getJobNum()+"", cs);
        }
    }
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
