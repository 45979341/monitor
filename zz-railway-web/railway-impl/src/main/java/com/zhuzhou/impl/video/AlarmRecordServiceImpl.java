package com.zhuzhou.impl.video;

import com.zhuzhou.entity.video.AlarmRecord;
import com.zhuzhou.dao.video.AlarmRecordMapper;
import com.zhuzhou.spi.video.AlarmRecordService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-22
 */
@Service
public class AlarmRecordServiceImpl extends BaseServiceImpl<AlarmRecordMapper, AlarmRecord> implements AlarmRecordService {
    @Autowired
    private AlarmRecordMapper alarmRecordMapper;
}
