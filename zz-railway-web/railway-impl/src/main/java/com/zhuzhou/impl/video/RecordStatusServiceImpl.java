package com.zhuzhou.impl.video;

import com.zhuzhou.dto.video.RecordStatusResult;
import com.zhuzhou.entity.video.RecordStatus;
import com.zhuzhou.dao.video.RecordStatusMapper;
import com.zhuzhou.spi.video.RecordStatusService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-02
 */
@Service
public class RecordStatusServiceImpl extends BaseServiceImpl<RecordStatusMapper, RecordStatus> implements RecordStatusService {
    @Autowired
    private RecordStatusMapper recordStatusMapper;

    @Override
    public RecordStatusResult getCountForRecord() {
        return recordStatusMapper.getCountforRecord();
    }
}
