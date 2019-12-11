package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.sys.SysPost;
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
 * @date 2019/10/22 14:47
 * @description: 过分相操纵违规
 * 运行途中，LKJ“过分相”，距离分相点前800米到分相点之间不能出现工况“制”；
 */
@Service
public class Item133 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i <= end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("过分相".equals(eventItem)) {
                double startKm = lkj.getKiloMeter();
                Date endTime = lkj.getTime();
                int t = 1;
                do {
                    lkj = list.get(i - t);
                    Double kiloMeter = lkj.getKiloMeter();
                    if (kiloMeter != null) {
                        double value = Math.abs(kiloMeter - startKm) * 1000;
                        if (value < 800 && "制动".equals(lkj.getTransaction())) {
                            // 违规
                            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                    .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I32_133)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_133).setPhaseIllegal(1).builder());
                            break;
                        } else if (value >= 800) {
                            break;
                        }
                    }
                    t++;
                } while (i > t);
            }
        }
    }
}
