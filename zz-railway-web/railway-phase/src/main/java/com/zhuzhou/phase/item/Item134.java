package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 补机未按规定校正
 * 1、补机运行途中；
 * 3、进站信号机前后设定距离（默认100米），在该范围内是否存在自动校正键；
 */
@Service
public class Item134 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        if (head.getTrainType().contains("补")) {
            boolean isAfter = false;

            for (int i = start; i < end; i++) {
                boolean isBefore = false;
                Lkj lkj = list.get(i);
                if ("进站".equals(lkj.getEventItem())) {
                    int t = i;
                    Lkj temp = lkj;
                    Lkj cur = list.get(t - 1);
                    while (t > 0) {
                        if (cur.getKiloMeter() != null) {
                            if (Math.abs(Math.abs(temp.getKiloMeter()) - Math.abs(cur.getKiloMeter())) * 1000 <= 100) {
                                if ("车位对正".equals(cur.getEventItem())) {
                                    isBefore = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        cur = list.get(--t);
                    }

                    if (isBefore) {
                        continue;
                    } else {
                        cur = list.get(i);
                        add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_134)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_134).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                    }

                }
            }
        }

    }
}
