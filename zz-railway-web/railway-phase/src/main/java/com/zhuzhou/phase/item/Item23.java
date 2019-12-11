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
 * @date 2019/10/21 16:39
 * @description: 项点名称：累计减压超过最大减压量
 * 1、针对通常工作状态下，列车运行途中进行判断；
 * 2、列车管压累计减压量不能大于规定值（600标压默认值为170KPA，500标压默认值为140KPA，浮动20KPA）；
 * 3、电力机车根据均衡风缸判断，内D燃机车根据列车管压力判断；
 * 4、速度在非零情况下；
 */
@Service
public class Item23 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int constPressure = HeadUtils.constPressure(head);
        int dropPressure = HeadUtils.maxDropPressure(head);
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int lkjPressure = NumberUtils.toInt(lkj.getPipePressure());
            int speed = lkj.getSpeed();
            if (lkjPressure >= constPressure - 10 && lkjPressure <= constPressure + 10 && speed > 0) {
                int curPipe;
                int nextPipe;
                Lkj cur;
                Lkj next;
                do {
                    cur = list.get(i);
                    next = list.get(i + 1);
                    curPipe = NumberUtils.toInt(cur.getPipePressure());
                    nextPipe = NumberUtils.toInt(next.getPipePressure());
                    if (nextPipe > curPipe && next.getSpeed() == 0) {
                        break;
                    }
                    i++;
                    // 开始下降
                } while (nextPipe == curPipe && i + 1 < end);
                if (nextPipe < curPipe && i + 1 < end) {
                    int startPressure = NumberUtils.toInt(cur.getPipePressure());
                    Lkj startLkj = cur;
                    Date startTime = cur.getTime();
                    do {
                        cur = list.get(i);
                        next = list.get(i + 1);
                        int nextSpeed = next.getSpeed();
                        curPipe = NumberUtils.toInt(cur.getPipePressure());
                        nextPipe = NumberUtils.toInt(next.getPipePressure());
                        // 减压到0或速度为0排除
                        if (cur.getSpeed() == 0 || curPipe == 0) {
                            break;
                        }
                        // 减压到管压开始回升
                        if (curPipe < nextPipe) {
                            if (nextPipe != 0 && nextSpeed != 0 && startPressure - NumberUtils.toInt(cur.getPipePressure()) > dropPressure) {
                                add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                                        .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I8_23)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_23).builder());
                            }
                            break;
                        }
                        i++;
                    } while (i + 1 < end);
                }
            }
        }
    }
}