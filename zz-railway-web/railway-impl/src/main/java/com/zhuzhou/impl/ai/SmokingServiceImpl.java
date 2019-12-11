package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.Smoking;
import com.zhuzhou.dao.ai.SmokingMapper;
import com.zhuzhou.spi.ai.SmokingService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * ai识别-吸烟 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-08-06
 */
@Service
public class SmokingServiceImpl extends BaseServiceImpl<SmokingMapper, Smoking> implements SmokingService {
    @Autowired
    private SmokingMapper smokingMapper;
}
