package com.zhuzhou.impl.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.dao.config.ConfigStationMapper;
import com.zhuzhou.spi.config.ConfigStationService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-11
 */
@Service
public class ConfigStationServiceImpl extends BaseServiceImpl<ConfigStationMapper, ConfigStation> implements ConfigStationService {

    @Override
    public Map<String, ConfigStation> listToMap() {
        List<ConfigStation> list = list();
        Map<String, ConfigStation> collect = list.stream().collect(Collectors.toMap(k -> k.getRoadNum() + SymbolConst.UNDER + k.getStationNum(), v -> v));
        return collect;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(List<ConfigStation> excelList) {
        //删除数据
        removeAll();
        //批量添加车站数据
        saveBatch(excelList);
        for (ConfigStation cs : excelList) {
            ConfigConst.stationMap.put(cs.getRoadNum() + " -" + cs.getStationNum(), cs);
        }
    }
}
