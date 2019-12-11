package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/18 11:47
 * @description: 项点名称: 柴速超速
 * 只分析内燃机车
 * 机车运行途中，转速(转速电流)超过1000，记录即可
 */
@Service
public class Item84 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.dieselLocomotive(head)) {
            return;
        }
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            if (NumberUtils.toInt(lkj.getSpeedElectricity()) > 1000) {
                Lkj cur;
                do {
                    i++;
                    cur = list.get(i);
                } while (NumberUtils.toInt(cur.getSpeedElectricity()) > 1000 && i <= end);
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                        .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I28_84)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_84).setPhaseIllegal(1).builder());
            }
        }
    }
}
