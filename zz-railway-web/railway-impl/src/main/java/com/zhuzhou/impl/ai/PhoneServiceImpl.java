package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.Phone;
import com.zhuzhou.dao.ai.PhoneMapper;
import com.zhuzhou.spi.ai.PhoneService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * ai识别-打手机 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-08-06
 */
@Service
public class PhoneServiceImpl extends BaseServiceImpl<PhoneMapper, Phone> implements PhoneService {
    @Autowired
    private PhoneMapper phoneMapper;
}
