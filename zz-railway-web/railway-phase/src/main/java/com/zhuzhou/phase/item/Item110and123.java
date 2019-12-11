package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.model.Situation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/11/14 16:30
 * @description:
 */
@Service
public class Item110and123 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        List<Situation> situations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            if ("退出出段".equals(lkj.getEventItem())) {
                int t = 1;
                Lkj cur;
                do {
                    cur = list.get(i - t);
                } while (!"开车对标".equals(cur.getEventItem()) && i - ++t > 0);
                situations.add(new Situation().setStart(i - t).setEnd(i));
            }
        }
        System.out.println(situations);
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            if ("IC卡拔出".equals(lkj.getEventItem())) {
                int index = i;
                boolean b = situations.stream().anyMatch(situation ->
                        (situation.getStart() < index && situation.getEnd() > index));
                if (b) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I32_110)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_110).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                } else {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(DateUtils.opSecond(lkj.getTime(), -5))
                            .setEnd(DateUtils.opSecond(lkj.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I32_123)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_123).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            }

        }
    }
}
