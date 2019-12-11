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
 * @date 2019/10/21 11:42
 * @description: 项点名称：初减压不足
 * 1、初减不足针对列车运行途中进行判断；
 * 2、列车从定压下降不足规定值（默认50KPA，浮动10KPA。如600标压，则保压范围为560KPA~540KPA必须保压设定值），同时持续时间大于规定值（默认5秒）；
 * 3、电力机车根据均衡风缸判断，内燃机车根据列车管压力判断；
 * 4、速度在非零情况下。
 * 监控非调车
 * 下降小于等于40，然后回升，在最低点持续大于5s
 */
@Service
public class Item22 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int constPressure = HeadUtils.constPressure(head);
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int lkjPressure = NumberUtils.toInt(lkj.getPipePressure());
            int speed = lkj.getSpeed();
            // 定压
            if (lkjPressure >= constPressure - 10 && lkjPressure <= constPressure + 10 && speed > 0) {
                Lkj cur;
                Lkj next;
                int curPressure;
                int nextPressure;
                do {
                    cur = list.get(i);
                    next = list.get(i + 1);
                    curPressure = NumberUtils.toInt(cur.getPipePressure());
                    nextPressure = NumberUtils.toInt(next.getPipePressure());
                    if (cur.getSpeed() == 0 || nextPressure > curPressure) {
                        break;
                    }
                    i++;
                } while (curPressure == nextPressure && i + 1 < end);
                if (nextPressure < curPressure && nextPressure >= constPressure - 40 && i + 1 < end) {
                    Lkj startLkj = cur;
                    do {
                        cur = list.get(i);
                        next = list.get(i + 1);
                        curPressure = NumberUtils.toInt(cur.getPipePressure());
                        nextPressure = NumberUtils.toInt(next.getPipePressure());
                        // 速度为0排除
                        if (cur.getSpeed() == 0) {
                            break;
                        }
                        if (nextPressure > curPressure) {
                            if (DateUtils.diffDate(cur.getTime(), next.getTime(), 5)) {
                                add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startLkj.getTime())
                                        .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I8_22)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_22).builder());
                            }
                        }
                        i++;
                        // 减压最低不能超过40，否则排除
                    } while (nextPressure >= constPressure - 40 && i + 1 < end);
                }
            }
        }
    }
}

