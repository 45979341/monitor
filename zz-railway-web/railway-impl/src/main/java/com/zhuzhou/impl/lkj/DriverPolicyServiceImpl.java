package com.zhuzhou.impl.lkj;

import com.zhuzhou.entity.lkj.DriverPolicy;
import com.zhuzhou.dao.lkj.DriverPolicyMapper;
import com.zhuzhou.spi.lkj.DriverPolicyService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-10
 */
@Service
public class DriverPolicyServiceImpl extends BaseServiceImpl<DriverPolicyMapper, DriverPolicy> implements DriverPolicyService {
    @Autowired
    private DriverPolicyMapper driverPolicyMapper;
}
