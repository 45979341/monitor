package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.Front;
import com.zhuzhou.dao.ai.FrontMapper;
import com.zhuzhou.spi.ai.FrontService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-27
 */
@Service
public class FrontServiceImpl extends BaseServiceImpl<FrontMapper, Front> implements FrontService {
    @Autowired
    private FrontMapper frontMapper;
}
