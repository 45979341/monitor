package com.zhuzhou.dao.video;

import com.zhuzhou.entity.video.UploadRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Repository
public interface UploadRecordMapper extends BaseMapper<UploadRecord> {

    /**
     * 查询上传记录列表
     * @param uploadRecord
     * @return
     */
    List<UploadRecord> findList(UploadRecord uploadRecord);
}
