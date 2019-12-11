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
 * @date 2019/10/17 11:29
 * @description: 项点名称：货车低速缓解
 * 1.监控状态下
 * 2.速度大于零小于等于15KM/H
 * 3.从定压减压量不小于50KPA,管压不能回升,否则违章
 */
@Service
public class Item28 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.freight(head)) {
            return;
        }
        int limitSpeed = HeadUtils.limitSpeed(head);
        int constPressure = HeadUtils.constPressure(head);
        // 管压
        int pipePressure;
        // 下一条记录
        Lkj next;
        // 下一条的管压
        int nextPressure;
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            pipePressure = NumberUtils.toInt(lkj.getPipePressure());
            if (lkj.getSpeed() > 0 && lkj.getSpeed() <= limitSpeed
                    && pipePressure >= constPressure - 10 && pipePressure <= constPressure + 10) {
                do {
                    lkj = list.get(i);
                    next = list.get(i + 1);
                    pipePressure = NumberUtils.toInt(lkj.getPipePressure());
                    nextPressure = NumberUtils.toInt(next.getPipePressure());
                    i++;
                } while (pipePressure <= nextPressure && lkj.getSpeed() > 0 && lkj.getSpeed() <= limitSpeed && i <= end);
                if (pipePressure > nextPressure && lkj.getSpeed() > 0 && lkj.getSpeed() <= limitSpeed && i <= end) {
                    int startPressure = pipePressure;
                    Date startTime = lkj.getTime();
                    Lkj startLkj = lkj;
                    do {
                        lkj = list.get(i);
                        next = list.get(i + 1);
                        pipePressure = NumberUtils.toInt(lkj.getPipePressure());
                        nextPressure = NumberUtils.toInt(next.getPipePressure());
                        if (nextPressure < startPressure - 50) {
                            break;
                        }
                        if (lkj.getSpeed() > 0 && lkj.getSpeed() <= limitSpeed) {
                            if (nextPressure > pipePressure) {
                                add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                                        .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_28)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_28).builder());
                                break;
                            }
                        } else {
                            break;
                        }
                        i++;
                    } while (i <= end);
                }
            }
        }
    }
}

