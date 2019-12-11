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
 * @author xiechonghu
 * @date 2019/10/22 11:25
 * @description: 进站信号未确认
 * 接近进站信号机前300米范围，LKJ事件“警惕键”存在，记录
 */
@Service
public class Item113 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = 0; i + 1 < list.size(); i++) {
            Lkj lkj = list.get(i);
            Lkj next = list.get(i + 1);
            String signalMachine = lkj.getSignalMachine();
            String nextSignalMachine = next.getSignalMachine();
            if (signalMachine == null || nextSignalMachine == null) {
                continue;
            }
            if (signalMachine.startsWith("进站") && !nextSignalMachine.startsWith("进站")) {
                int t = 1;
                Lkj cur;
                boolean flag = false;
                do {
                    cur = list.get(i - t);
                    if (cur.getDistance() < 300 && "警惕键".equals(cur.getEventItem())) {
                        //  存在 就记录
                        flag = true;
                        break;
                    } else if (cur.getDistance() >= 300) {
                        break;
                    }
                    t++;
                } while (t < i);
                if (flag) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(cur.getTime())
                            .setEnd(lkj.getTime()).setPhaseIllegal(2).setItemEnum(ItemRecord.ItemEnum.I32_113)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_113).builder());
                }
            }
        }
    }
}
