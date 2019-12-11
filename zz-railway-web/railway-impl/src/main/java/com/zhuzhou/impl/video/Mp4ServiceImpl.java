package com.zhuzhou.impl.video;

import com.zhuzhou.dao.video.Mp4Mapper;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.form.video.Mp4KmLocationForm;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.spi.video.Mp4Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Service
public class Mp4ServiceImpl extends BaseServiceImpl<Mp4Mapper, Mp4> implements Mp4Service {
    @Autowired
    private IdxIndexService idxIndexService;

    @Override
    public int kmLocation(Mp4KmLocationForm form) {
        List<Idx> list = idxIndexService.remoteList(form.getRecordId());
        for (int i = 0;i < list.size();i++) {
            Idx idx = list.get(i);
            BigDecimal val = new BigDecimal(form.getKm()).subtract(new BigDecimal(idx.getKilometre()).divide(new BigDecimal(1000)));
            //公里标定位：接近2公里内
            if (Math.abs(val.intValue()) < 2) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public Idx startTimeLocation(String recordId, Date startTime) {
        List<Idx> list = idxIndexService.remoteList(recordId);
        long time = startTime.getTime();
        for (int i = 0;i < list.size();i++) {
            Idx idx = list.get(i);
            long platformTime = idx.getPlatformTime().getTime();
            if (platformTime > time) {
                return idx;
            }
        }
        return null;
    }
}
