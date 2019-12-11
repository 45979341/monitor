package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.FrontStatus;
import com.zhuzhou.dao.ai.FrontStatusMapper;
import com.zhuzhou.spi.ai.FrontStatusService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-06
 */
@Service
public class FrontStatusServiceImpl extends BaseServiceImpl<FrontStatusMapper, FrontStatus> implements FrontStatusService {
    @Autowired
    private FrontStatusMapper frontStatusMapper;
}
