package com.zhuzhou.impl.temp;

import com.zhuzhou.entity.temp.UploadDir;
import com.zhuzhou.dao.temp.UploadDirMapper;
import com.zhuzhou.spi.temp.UploadDirService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-06-21
 */
@Service
public class UploadDirServiceImpl extends BaseServiceImpl<UploadDirMapper, UploadDir> implements UploadDirService {
    @Autowired
    private UploadDirMapper uploadDirMapper;
}
