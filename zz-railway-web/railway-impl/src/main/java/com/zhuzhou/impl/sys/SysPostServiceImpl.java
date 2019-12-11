package com.zhuzhou.impl.sys;

import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.dao.sys.SysPostMapper;
import com.zhuzhou.spi.sys.SysPostService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements SysPostService {
    @Autowired
    private SysPostMapper sysPostMapper;
}
