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

import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/17 10:47
 * @description: 项点名称：抱闸运行
 * 1、运行途中，列车管压力为定压，机车速度大于规定值（默认5KM/H）；
 * 2、列车制动缸压力大于或等于规定值（20KPA），且持续时间大于规定时间（默认120秒）；
 */
@Service
public class Item30 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int constPressure = HeadUtils.constPressure(head);
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int pipePressure = NumberUtils.toInt(lkj.getPipePressure());
            // 速度大于5 定压 缸压大于等于20
            if (lkj.getSpeed() > 5
                    && pipePressure <= constPressure + 10 && pipePressure >= constPressure - 10
                    && NumberUtils.toInt(lkj.getCylinderPressure()) >= 20) {
                int t = 1;
                int curPipePressure;
                int curCylinderPressure;
                int curSpeed;
                Lkj cur;
                do {
                    cur = list.get(i + t);
                    curPipePressure = NumberUtils.toInt(cur.getPipePressure());
                    curCylinderPressure = NumberUtils.toInt(cur.getCylinderPressure());
                    curSpeed = cur.getSpeed();
                    t++;
                } while (curPipePressure == pipePressure && curCylinderPressure >= 20 && curSpeed > 5 && list.size() > i + t);
                // 持续时间超过120秒 违章
                if (DateUtils.diffDate(lkj.getTime(), cur.getTime(), 120)) {
                    Lkj temp = list.get(i + t - 1);
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                            .setEnd(temp.getTime()).setItemEnum(ItemRecord.ItemEnum.I9_30)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_30).builder());
                }
                i += t;
            }
        }
    }
}