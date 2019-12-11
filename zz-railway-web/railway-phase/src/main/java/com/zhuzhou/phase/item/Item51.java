package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/24 14:38
 * @description: 错误输入侧线股道号(短输长)
 * 监控非调车状态，1.出站信号机到下个进站信号机，出现事件（车位向前，车位向后，车位对中），并且再其他（列）中，大于50米。违章。
 */
@Service
public class Item51 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String signalMachine = lkj.getSignalMachine();
            if (signalMachine == null) {
                continue;
            }
            if (signalMachine.startsWith("出站")) {
                do {
                    i++;
                    lkj = list.get(i);
                    String nextEventItem = lkj.getEventItem();
                    signalMachine = lkj.getSignalMachine();
                    if (signalMachine == null) {
                        continue;
                    }
                    if (signalMachine.startsWith("进站")) {
                        break;
                    }
                    boolean condition = ("车位向前".equals(nextEventItem) || "车位向后".equals(nextEventItem) || "车位对中".equals(nextEventItem))
                            && NumberUtils.toDouble(lkj.getOther()) > 50;
                    // 出现事件 且其他大于50
                    if (condition) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I14_51)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_51).builder());
                    }
                } while (i < end);
            }
        }
    }
}
