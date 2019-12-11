package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.formula.udf.IndexedUDFFinder;
import org.springframework.stereotype.Service;

import java.nio.channels.Pipe;
import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/21 18:14
 * @description: **紧急制动后违规缓解**
 * 监控状态下，列车紧急制动（列车管压力从600/500下降到0）后，
 * 在机车速度非零的情况下，管压从规定值（默认200KPA）以下开始缓解，就是违章。
 * 11.25更新：紧急制动后减压，管压200到0的过程不能缓解 否则违规
 **/
@Service
public class Item25 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            int pipePressure;
            if ("紧急制动".equals(eventItem)) {
                Date startTime = lkj.getTime();
                do {
                    lkj = list.get(++i);
                    eventItem = lkj.getEventItem();
                    pipePressure = NumberUtils.toInt(lkj.getPipePressure());
                    if ("紧急制动".equals(eventItem)) {
                        startTime = lkj.getTime();
                    }
                } while (pipePressure > 200 && i + 1 < end);
                if (pipePressure <= 200 && i + 1 < end) {
                    Lkj cur;
                    Lkj next;
                    int curPressure;
                    int nextPressure;
                    do {
                        cur = list.get(++i);
                        next = list.get(i + 1);
                        curPressure = NumberUtils.toInt(cur.getPipePressure());
                        nextPressure = NumberUtils.toInt(next.getPipePressure());
                        if (cur.getSpeed() == 0) {
                            break;
                        }
                        if (nextPressure > curPressure) {
                            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                    .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I8_25)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_25).builder());
                            break;
                        }
                    } while (i + 1 < end && nextPressure > 0);
                }
            }
        }
    }
}
