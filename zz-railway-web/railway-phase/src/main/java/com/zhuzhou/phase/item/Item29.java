package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.garageutils.GarageUtils;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxiaokuan
 * @date 2019-07-30
 * @description:
 * **单阀制动停车**
 * 运行途中，列车站内停车、区间停车和机外停车时，满足以下几个条件则违章：
 * 1、列车速度由规定值或以上下降为零时（默认3KM/H）；
 * 2、列车管压力在定压状态（浮动20KPA）；
 * 3、制动缸压力上升规定值（默认50KPA）；
 * 4、制动缸压力上升规定值，且持续时间在规定值以上（默认20秒）；
 * 5、客货均分析，列车管定压按照列车管压力来判断；
 **/
@Service
public class Item29 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        for (int i = start;i < end;i++) {
            Lkj lkj = list.get(i);
            boolean isConst = false;
            boolean isTwenty = false;

            //站内停车、区间停车和机外停车时
            if (lkj.getEventItem().equals("站内停车")
                    || lkj.getEventItem().equals("区间停车")
                    || lkj.getEventItem().equals("机外停车")) {
                int t = i;
                Lkj cur = lkj;
                List<Lkj> list1 = new ArrayList<>();
                int speed = 0;

                //列车速度由规定值或以上下降为零
                do {
                    if (cur.getEventItem().contains("停车")) {
                        break;
                    }
                    list1.add(cur);
                    cur = list.get(--t);
                    speed = cur.getSpeed();
                } while (speed < 3 && t>0);
                
                //从达到规定值或以上开始循环匹配是否达到违规条件
                int count = list1.size();
                for (int j = t; j < list.size(); j++) {
                    if (count < 0) {
                        break;
                    }
                    cur = list.get(j);
                    Lkj temp = cur;
                    if (NumberUtils.toInt(cur.getCylinderPressure()) >= 50 && !isTwenty) {
                        int k = j+1;
                        while ((NumberUtils.toInt(cur.getPipePressure()) >= constPipe -20 && NumberUtils.toInt(cur.getPipePressure()) <= constPipe +20)
                                &&NumberUtils.toInt(cur.getCylinderPressure()) >= 50 && k <list.size()) {
                            cur = list.get(k++);
                            count--;
                        }
                        if (DateUtils.diffDate(temp.getTime(),cur.getTime(),20)) {
                            isTwenty = true;
                        }
                    }
                    count --;
                }
                if (isTwenty) {
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I9_29)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_29).builder());
                }
            }

        }
    }
}
