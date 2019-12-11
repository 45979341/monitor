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

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/15 16:31
 * @description: 项点名称: 连挂前无一度停车
 * 1.在调车状态下，开车对标前，最后一次调车停车前走行距离在小于等于20米，大于等于10米的范围内。
 * 2.这段走行距离的速度需小于5km/h.
 * 3.否则违章（*车次为5开头的五位数不分析）最后一次调车开车到最后一次调车停车，10-20米内距离，不符合标准算违章
 */
@Service
public class Item11 extends AbstractPhase {
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
                    lkj = list.get(j);
                    eventItem = lkj.getEventItem();
                    if ("调车停车".equals(eventItem)) {
                        endIndex = j;
                    }
                    if ("调车开车".equals(eventItem)) {
                        startIndex = j;
                        break;
                    }
                }
                Lkj startLkj = list.get(startIndex);
                Lkj endLkj = list.get(endIndex);
                int value = Math.abs(startLkj.getDistance() - endLkj.getDistance());
                // 距离小于10 大于20违规
                if (value < 10 || value > 20) {
                    // 违规
                    Date startTime = startLkj.getTime();
                    Date endTime = endLkj.getTime();
                    add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                            .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I4_11)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_11).builder());
                }
            }
        }
    }
}