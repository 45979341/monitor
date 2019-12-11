package com.zhuzhou.dao.video;

import com.zhuzhou.dto.video.RecordStatusResult;
import com.zhuzhou.entity.video.RecordStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-02
 */
@Repository
public interface RecordStatusMapper extends BaseMapper<RecordStatus> {
    /**
     * 返回项点处理信息
     * @return
     */
    public RecordStatusResult getCountforRecord();
}
