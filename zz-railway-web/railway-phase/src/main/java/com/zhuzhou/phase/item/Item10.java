package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author xiechonghu
 * @Date 2019-10-15
 * @Description: **库内超速**
 * 走行超速判断以下条件（如果设定库内车次，按照库内车次设定值判断，其他按以下四个条件判断）：
 * 1、出库库内走行最大速度不能大于设定值（默认20KM/H）；
 * 2、出库机走线最大速度不能大于设定值（默认30KM/H）；
 * 3、入库库内走行最大速度不能大于设定值（默认20KM/H）；
 * 4、入库机走线最大速度不能大于设定值（默认30KM/H）；
 * <p>
 * 只判断这个：
 * 1.时速大于限速。或速度大于20km/h
 **/
@Service
public class Item10 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        // 库内
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int speed = lkj.getSpeed();
            // 取限速与20较小的一个
            int limit = Math.min(20, NumberUtils.toInt(lkj.getRateLimit()));
            if (speed > limit) {
                Date startTime = lkj.getTime();
                //3-10
                do {
                    i++;
                    Lkj temp = list.get(i);
                    int tempSpeed = temp.getSpeed();
                    limit = Math.min(20, NumberUtils.toInt(temp.getRateLimit()));
                    // 记录超速的时间段，一直超速出库则记录到出库时刻
                    if (tempSpeed <= limit || i == end) {
                        //违章
                        add(new PhaseAssist.Builder().setLkj(temp.setRecordId(lkjId)).setLkjId(lkjId).setStart(startTime)
                                .setEnd(temp.getTime()).setItemEnum(ItemRecord.ItemEnum.I3_10)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_10).builder());
                        break;
                    }
                } while (end > i);
            }
        }
    }
}
