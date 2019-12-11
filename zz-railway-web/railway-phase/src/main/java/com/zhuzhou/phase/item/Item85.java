package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/24 15:22
 * @description: 转速为0
 * 1、电力机车不分析；（内燃机）
 * 2、列车运行途中，柴油机转速为0，则记录一次；
 * 3、该项点可选；
 */
@Service
public class Item85 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.electricLocomotive(head)) {
            return;
        }
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            if (lkj.getSpeed() == 0) {
                Date startTime = lkj.getTime();
                do {
                    i++;
                    lkj = list.get(i);
                    if (lkj.getSpeed() > 0) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(list.get(i - 1).getTime()).setItemRecordIllegal(4).setPhaseIllegal(1).setItemEnum(ItemRecord.ItemEnum.I28_85)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_85).builder());
                        break;
                    }
                } while (i < end);
            }
        }
    }
}
