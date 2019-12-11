package com.zhuzhou.impl.link;

import com.zhuzhou.entity.link.Link;
import com.zhuzhou.dao.link.LinkMapper;
import com.zhuzhou.spi.link.LinkService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-11-15
 */
@Service
public class LinkServiceImpl extends BaseServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private LinkMapper linkMapper;
}
