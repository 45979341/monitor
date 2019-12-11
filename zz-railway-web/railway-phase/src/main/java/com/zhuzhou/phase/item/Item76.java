package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-18
 * @Description: 违规换向
 * 1、列车运行途中，机车速度非零情况下，机车手柄前后位互换（前牵~后牵）；违章
 */
@Service
public class Item76 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        List<Lkj> list1 = new ArrayList<>();
        for (int i = start;i < end;i++) {
            Lkj lkj = list.get(i);
            if (lkj.getSpeed() > 0) {
                if (lkj.getFrontBehind().equals("向前")) {
                    int t = i;
                    while (lkj.getFrontBehind()!=null&&lkj.getFrontBehind().equals("向前")
                            && lkj.getSpeed() > 0 && t < list.size()) {
                        lkj = list.get(t++);
                    }
                    if (lkj.getFrontBehind()!=null&&lkj.getFrontBehind().equals("向后")) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_76)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_76).builder());
                    }
                    i = t-2;
                } else if (lkj.getFrontBehind().equals("向后")) {
                    int t = i;
                    while (lkj.getFrontBehind()!=null&&lkj.getFrontBehind().equals("向后")
                            && lkj.getSpeed() > 0 && t < list.size()) {
                        lkj = list.get(t++);
                    }
                    if (lkj.getFrontBehind()!=null&&lkj.getFrontBehind().equals("向前")) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_76)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_76).builder());
                    }
                    i = t-2;
                }
            }
        }
    }
}
