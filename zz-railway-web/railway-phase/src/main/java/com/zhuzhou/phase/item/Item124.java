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
 * @author xiechonghu
 * @date 2019/10/22 13:51
 * @description: 运行中超过规定速度
 * 列车运行途中，运行速度与限制速度差值小于规定值，则违规；
 */
@Service
public class Item124 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int speed = lkj.getSpeed();
            int rateLimit = NumberUtils.toInt(lkj.getRateLimit());
            if (speed > rateLimit) {
                Date startTime = lkj.getTime();
                do {
                    i++;
                    lkj = list.get(i);
                    speed = lkj.getSpeed();
                    rateLimit = NumberUtils.toInt(lkj.getRateLimit());
                    if (speed <= rateLimit) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_124)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_124).builder());
                        break;
                    }
                } while (i < end);
            }
        }
    }
}
