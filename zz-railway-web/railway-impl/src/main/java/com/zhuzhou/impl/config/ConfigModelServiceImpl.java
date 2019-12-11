package com.zhuzhou.impl.config;

import com.zhuzhou.entity.config.ConfigModel;
import com.zhuzhou.dao.config.ConfigModelMapper;
import com.zhuzhou.spi.config.ConfigModelService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-07-04
 */
@Service
public class ConfigModelServiceImpl extends BaseServiceImpl<ConfigModelMapper, ConfigModel> implements ConfigModelService {
    @Autowired
    private ConfigModelMapper configModelMapper;
}
