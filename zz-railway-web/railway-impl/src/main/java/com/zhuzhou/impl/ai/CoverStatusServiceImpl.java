package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.CoverStatus;
import com.zhuzhou.dao.ai.CoverStatusMapper;
import com.zhuzhou.spi.ai.CoverStatusService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-11
 */
@Service
public class CoverStatusServiceImpl extends BaseServiceImpl<CoverStatusMapper, CoverStatus> implements CoverStatusService {
    @Autowired
    private CoverStatusMapper coverStatusMapper;
}
