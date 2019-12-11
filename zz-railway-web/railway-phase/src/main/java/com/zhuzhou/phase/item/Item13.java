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
 * @date 2019/10/15 15:06
 * @description: 项点名称：连挂后拉钩试验
 * 最后一次调车停车到开车对标
 * 完成一次非零前<-->非零后的转换 事件小于15秒
 */
@Service
public class Item13 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        // 车次为5开头的五位数不分析
        if (HeadUtils.startWith5Length5(head)) {
            return;
        }
        Lkj lkj;
        int startIndex = end;
        int endIndex = end;

        for (int i = 0; i < end; i++) {
            lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("开车对标".equals(eventItem)) {
                // 非零前<-->非零后
                boolean condition = false;
                // 缸压为0
                boolean pressure = false;
                for (int j = i; j > 0; j--) {
                    lkj = list.get(j);
                    eventItem = lkj.getEventItem();
                    if ("调车停车".equals(eventItem)) {
                        Date time = lkj.getTime();
                        startIndex = j;
                        while (DateUtils.diffDate2(time, list.get(++j).getTime(), 15) && j + 1 < i) {
                        }
                        endIndex = j;
                        break;
                    }
                }

                for (int j = startIndex; j < endIndex; j++) {
                    lkj = list.get(j);
                    if (NumberUtils.toInt(lkj.getCylinderPressure()) == 0) {
                        pressure = true;
                    }
                    // 非零前或后
                    if ("非零".equals(lkj.getZeroBit())
                            && ("向前".equals(lkj.getFrontBehind()) || "向后".equals(lkj.getFrontBehind()))) {
                        Lkj cur;
                        do {
                            cur = list.get(j);
                            if (NumberUtils.toInt(cur.getCylinderPressure()) == 0) {
                                pressure = true;
                            }
                            // 非零前或后和前面不同
                            if ("非零".equals(cur.getZeroBit())
                                    && ("向前".equals(cur.getFrontBehind()) || "向后".equals(cur.getFrontBehind()))
                                    && !lkj.getFrontBehind().equals(cur.getFrontBehind())) {
                                condition = true;
                            }
                        } while (j++ <= endIndex);
                    }

                }

                if (!(condition && pressure)) {
                    add(new PhaseAssist.Builder().setLkj(list.get(startIndex)).setLkjId(lkjId).setStart(list.get(startIndex).getTime())
                            .setEnd(list.get(endIndex).getTime()).setItemEnum(ItemRecord.ItemEnum.I4_13)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_13).builder());
                    break;
                }
            }
        }
    }
}