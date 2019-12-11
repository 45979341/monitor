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
 * @date 2019/10/18 15:23
 * @description: 项点名称:靠标停车超速
 * 信号机:站中心 信号:红灯 或 红黄灯
 * 站内停车
 * 1、列车站内停车时，停车距离规定范围内（默认100米），其速度不能超过规定值（默认10KM/H）；
 * 2、列车站内停车时，停车距离规定范围内（默认50米），其速度不能超过规定值（默认5KM/H）；
 */
@Service
public class Item125 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            boolean condition = "站内停车".equals(lkj.getEventItem())
                    && ("红灯".equals(lkj.getSignals()) || "红黄".equals(lkj.getSignals()))
                    && "站中心".equals(lkj.getSignalMachine());
            if (condition) {
                int t = 1;
                do {
                    Lkj cur = list.get(i - t);
                    condition = ("红灯".equals(cur.getSignals()) || "红黄".equals(cur.getSignals()))
                            && "站中心".equals(cur.getSignalMachine());
                    if (condition) {
                        if (cur.getDistance() < 50 && cur.getSpeed() <= 5) {
                            t++;
                        } else if (cur.getDistance() >= 50 && cur.getSpeed() < 100 & cur.getSpeed() <= 10) {
                            t++;
                        } else if (cur.getDistance() >= 100) {
                            break;
                        } else {
                            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                    .setEnd(lkj.getTime()).setItemRecordIllegal(4).setItemEnum(ItemRecord.ItemEnum.I32_125)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_125).setPhaseIllegal(2).setItemRecordIllegal(4).builder());
                            break;
                        }
                    } else {
                        break;
                    }
                } while (t < i);
            }
        }
    }
}
