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

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/22 10:43
 * @description: 项点名称：货车大劈叉
 * 1、货车在运行途中，在速度非零的情况下，列车管压力有从定压开始进行减压的过程；（保压）
 * 2、如果在上述过程中，未投入使用电阻制动，则制动缸压力必须大于或等于设定值（默认50KPA）；
 * 3、判断范围，为列车管压力减压到最低点开始至缓解开始点；
 * 4、机车型号代号为231～239的机车型号不分析；（不管）
 */
@Service
public class Item32 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.freight(head)) {
            return;
        }
        int constPressure = HeadUtils.constPressure(head);
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int lkjPressure = NumberUtils.toInt(lkj.getPipePressure());
            int speed = lkj.getSpeed();
            if (lkjPressure > constPressure - 10 && lkjPressure < constPressure + 10 && speed > 0) {
                int curPipe;
                int nextPipe;
                Lkj cur;
                Lkj next;
                do {
                    cur = list.get(i);
                    next = list.get(i + 1);
                    curPipe = NumberUtils.toInt(cur.getPipePressure());
                    nextPipe = NumberUtils.toInt(next.getPipePressure());
                    if (nextPipe > curPipe || next.getSpeed() == 0) {
                        break;
                    }
                    i++;
                    // 开始下降
                } while (nextPipe >= curPipe && i < end);
                if (nextPipe < curPipe && nextPipe != 0 && next.getSpeed() > 0 && i < end) {
                    Date startTime = cur.getTime();
                    int startIndex = i;
                    Lkj startLkj = cur;
                    do {
                        cur = list.get(i);
                        next = list.get(++i);
                        curPipe = NumberUtils.toInt(cur.getPipePressure());
                        nextPipe = NumberUtils.toInt(next.getPipePressure());
                        // 管压开始上升 或速度等于0 跳出
                    } while (curPipe >= nextPipe && next.getSpeed() > 0 && i < end);
                    if ((curPipe <= nextPipe || nextPipe == 0) && cur.getSpeed() > 0 && i < end) {
                        Date endTime = cur.getTime();
                        int t = 1;
                        boolean flag = false;
                        do {
                            cur = list.get(i - t);
                            if ("制动".equals(cur.getTransaction()) || NumberUtils.toInt(cur.getCylinderPressure()) > 50) {
                                // 没违规
                                flag = true;
                            }
                            t++;
                        } while (i - t >= startIndex);
                        if (!flag) {
                            add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                                    .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I9_32)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_32).builder());
                        }
                    }
                }
            }
        }
    }
}