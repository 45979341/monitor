package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/18 16:15
 * @description: 项点名称:主断后缓解列车
 * 只分析本务电力机车
 * LKJ“主断断开”到“主断闭合”之间，列车管压不能上升。
 */
@Service
public class Item132 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.electricLocomotive(head)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            int pipe = NumberUtils.toInt(lkj.getPipePressure());
            if ("主断断开".equals(lkj.getEventItem())) {
                Lkj cur;
                String curEvent;
                do {
                    i++;
                    cur = list.get(i);
                    curEvent = cur.getEventItem();
                } while (!"主断闭合".equals(curEvent) && NumberUtils.toInt(cur.getPipePressure()) <= pipe && i < list.size());
                if (!"主断闭合".equals(curEvent) && i < list.size()) {
                    Lkj temp;
                    do {
                        i++;
                        temp = list.get(i);
                    } while (!"主断闭合".equals(temp.getEventItem()) && NumberUtils.toInt(temp.getPipePressure()) > NumberUtils.toInt(cur.getPipePressure()));
                    // 记录
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(cur.getTime())
                            .setEnd(list.get(i-1).getTime()).setItemEnum(ItemRecord.ItemEnum.I32_132)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_132).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                }
            }
        }
    }
}
