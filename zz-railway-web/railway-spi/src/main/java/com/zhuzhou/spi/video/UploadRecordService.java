package com.zhuzhou.spi.video;

import com.zhuzhou.entity.video.UploadRecord;
import com.zhuzhou.spi.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
public interface UploadRecordService extends BaseService<UploadRecord> {

    /**
     * 上传文件分析
     * @param files
     */
    void upload(MultipartFile[] files);
}
