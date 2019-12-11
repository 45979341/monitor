package com.zhuzhou.impl.config;

import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.dao.config.ConfigPhaseMapper;
import com.zhuzhou.spi.config.ConfigPhaseService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 项点分数表 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-07-24
 */
@Service
public class ConfigPhaseServiceImpl extends BaseServiceImpl<ConfigPhaseMapper, ConfigPhase> implements ConfigPhaseService {
    @Autowired
    private ConfigPhaseMapper configPhaseMapper;
}
