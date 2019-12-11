package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/11/7 11:48
 * @description: 停车后未保压
 */
@Service
public class Item26 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int constPressure = HeadUtils.constPressure(head);
        // 客车在30秒内完成，货车在120秒内完成
        int time = head.getTrainType().contains("客") ? 30 : 120;
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("进站".equals(eventItem)) {
                int entrance = i;
                do {
                    lkj = list.get(++i);
                    eventItem = lkj.getEventItem();
                } while (!"出站".equals(eventItem) && i + 1 < end);
                // 没遇到出站 但是到了区间最后，也跳出判断，（到了list结尾除外）
                if (i + 1 <= end && i + 2 < list.size()) {
                    int outbound = i;
                    do {
                        lkj = list.get(--i);
                        eventItem = lkj.getEventItem();
                    } while (!"站内停车".equals(eventItem) && i - 1 > entrance);
                    if ("站内停车".equals(eventItem) && i > entrance) {
                        boolean flag = false;
                        Date startTime = lkj.getTime();
                        Lkj startLkj = lkj;
                        Date endTime;
                        do {
                            lkj = list.get(++i);
                            endTime = lkj.getTime();
                            // 客车小于30秒 货车小于120秒(第一条大于规定时间的)
                            if (DateUtils.diffDate(startTime, endTime, time)) {
                                // 第一条大于规定时间的前一条 前一条
                                lkj = list.get(i - 1);
                                endTime = lkj.getTime();
                                // 减压至少80（100浮动20）
                                if (NumberUtils.toInt(lkj.getPipePressure()) < constPressure - 80) {
                                    flag = true;
                                }
                                break;
                            }
                        } while (i + 1 < outbound);
                        if (!flag) {
                            add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                                    .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I8_26)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_26).builder());
                        }
                    }
                }
            }
        }
    }
}
