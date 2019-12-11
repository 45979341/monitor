package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
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
 * @date 2019/10/21 17:38
 * @description: **列车管压过量供给**
 * 1、列车运行途中，在速度非零情况下，管压超过定压规定值或以上（默认40KPA）；
 * 2、电力机车以均衡风缸判断，内燃机车以列车管压力判断；
 * 3、分析范围为运行途中；
 **/
@Service
public class Item24 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int limitPressure = HeadUtils.limitPressure(head);
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int lkjPressure = NumberUtils.toInt(lkj.getPipePressure());
            int speed = lkj.getSpeed();

            // 超过定压+40
            if (lkjPressure >= limitPressure && speed > 0) {
                Date startTime = lkj.getTime();
                Lkj cur;
                int curPipe;
                do {
                    cur = list.get(i);
                    curPipe = NumberUtils.toInt(cur.getPipePressure());
                    i++;
                } while (curPipe >= limitPressure && cur.getSpeed() > 0 && i + 1 < list.size());
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                        .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I8_24)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_24).builder());
            }
        }
    }
}
