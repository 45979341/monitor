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
 * @date 2019/10/24 14:44
 * @description: 手柄非零位过分相
 * 监控状态
 * 在LKJ事件中检索“过分相”后，“零位”项手柄在零位，如果手柄越过分相在“非零位",则违章。
 */
@Service
public class Item54 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("过分相".equals(eventItem) && "非零".equals(lkj.getZeroBit()) && lkj.getSpeed() > 0) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                        .setEnd(lkj.getTime()).setItemRecordIllegal(4).setPhaseIllegal(1).setItemEnum(ItemRecord.ItemEnum.I15_54)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_54).builder());
            }
        }
    }
}
