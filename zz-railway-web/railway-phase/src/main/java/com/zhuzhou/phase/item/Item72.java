package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/30 11:32
 * @description: 区间非正常停车
 * 运行途中
 * 1：LKJ檢索“区间停车”；
 * 2.补机不分析；
 * 3、红黄灯，红灯，白灯，灭灯情况除外。
 * 灭灯不管
 */
@Service
public class Item72 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (HeadUtils.bu(head)) {
            return;
        }
        list.stream().limit(end).skip(start).forEach(cur -> {
            String signals = cur.getSignals();
            boolean condition = "区间停车".equals(cur.getEventItem()) && !(("红黄".equals(signals) || "红灯".equals(signals) || "白灯".equals(signals)));
            if (condition) {
                add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(DateUtils.opSecond(cur.getTime(), -5))
                        .setEnd(DateUtils.opSecond(cur.getTime(), 5)).setItemEnum(ItemRecord.ItemEnum.I23_72)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_72).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
            }
        });
    }
}