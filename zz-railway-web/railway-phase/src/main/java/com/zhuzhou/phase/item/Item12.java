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
 * @date 2019/10/15 17:54
 * @description: 项点名称：连挂超速
 * 1.在调车状态下，开车对标前，最后一次调车停车前走行距离在小于等于20米，大于等于10米的范围内。
 * 2.这段走行距离的速度需小于5km/h.
 * 3.否则违章（*车次为5开头的五位数不分析）110编号基础上，速度不超过5公里
 */
@Service
public class Item12 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        // 车次为5开头的五位数不分析
        if (HeadUtils.startWith5Length5(head)) {
            return;
        }
        Lkj lkj;
        String eventItem;
        int startIndex = end;
        int endIndex = end;
        for (int i = 0; i < end; i++) {
            lkj = list.get(i);
            eventItem = lkj.getEventItem();
            if ("开车对标".equals(eventItem)) {
                // 找到最后一次调车停车
                for (int j = i; j > 0; j--) {
                    lkj = list.get(i);
                    eventItem = lkj.getEventItem();
                    // 找到调车停车，再往后找15秒
                    if ("调车停车".equals(eventItem)) {
                        endIndex = j;
                    }
                    if ("调车开车".equals(eventItem)) {
                        startIndex = j;
                        break;
                    }
                }
                for (int j = startIndex; j <= endIndex; j++) {
                    lkj = list.get(j);
                    if (lkj.getSpeed() > 5) {
                        Date startTime = lkj.getTime();
                        do {
                            lkj = list.get(++j);
                            if (lkj.getSpeed() <= 5 || j == endIndex) {
                                //违章
                                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                        .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I4_12)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_12).builder());
                                break;
                            }
                        } while (j <= endIndex);
                    }
                }
            }
        }
    }
}