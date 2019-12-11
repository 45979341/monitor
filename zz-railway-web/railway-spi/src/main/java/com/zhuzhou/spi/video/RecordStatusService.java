package com.zhuzhou.spi.video;

import com.zhuzhou.dto.video.RecordStatusResult;
import com.zhuzhou.entity.video.RecordStatus;
import com.zhuzhou.spi.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-02
 */
public interface RecordStatusService extends BaseService<RecordStatus> {
    public RecordStatusResult getCountForRecord();
}
