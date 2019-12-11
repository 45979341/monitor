package com.zhuzhou.spi.video;

import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.form.video.Mp4KmLocationForm;
import com.zhuzhou.spi.BaseService;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
public interface Mp4Service extends BaseService<Mp4> {

    /**
     * 根据公里标定位视频
     * @param form
     * @return
     */
    int kmLocation(Mp4KmLocationForm form);

    /**
     * 根据开始时间定位最近一条数据记录
     * @param recordId
     * @param startTime
     * @return idx
     */
    Idx startTimeLocation(String recordId, Date startTime);
}
