package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.Cover;
import com.zhuzhou.dao.ai.CoverMapper;
import com.zhuzhou.spi.ai.CoverService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-11
 */
@Service
public class CoverServiceImpl extends BaseServiceImpl<CoverMapper, Cover> implements CoverService {
    @Autowired
    private CoverMapper coverMapper;
}
