package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.garageutils.GarageUtils;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-17
 * @Description: 电制停车
 * 1、列车途中运行，停车过程中（站内停车、区间停车、机外停车），只采取了电阻制动，而无空气制动；
 */
@Service
public class Item33 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        List<List<Lkj>> lists = new ArrayList<>();
        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        for (int i = start; i < end; i++) {

            Lkj cur = list.get(i);
            Integer speed = cur.getSpeed();
            int t = 1;
            List<Lkj> stop = new ArrayList<>();
            stop.add(cur);

            if (speed == 10) {
                Lkj temp = cur;
                while (10 >= cur.getSpeed() && (i + t)<list.size()) {
                    cur = list.get(i+t);
                    stop.add(cur);
                    if (cur.getSpeed() == 0) {
                        stop.add(cur);
                        lists.add(stop);
                        break;
                    }
                    t++;
                }
                i = i + t;
            }

        }

        for (int i = 0; i < lists.size(); i++) {
            List<Lkj> list1 = lists.get(i);
            //速度10降到0是否一直定压
            boolean isConst = false;
            //速度10降到0是否缸压起始为0
            boolean isZero = false;
            //是否进行电阻制动
            boolean isEletric = false;
            Lkj fault;  //违章点

            //循环当前是否一直处于定压
            for (Lkj lkj : list1) {
                if (NumberUtils.toInt(lkj.getPipePressure()) >= constPipe - 10 && NumberUtils.toInt(lkj.getPipePressure()) <= constPipe + 10) {
                    isConst = true;
                } else {
                    isConst = false;
                    break;
                }
            }

            //查看当前范围内是否有缸压为0
            for (Lkj lkj : list1) {
                if (NumberUtils.toInt(lkj.getCylinderPressure()) == 0) {
                    isZero = true;
                } else {
                    isZero = false;
                }
            }

            //前提一是否成立
            if (isConst && isZero) {
                fault = list1.get(0);
                for (Lkj lkj : list1) {
                    if (lkj.getTransaction().equals("制动")) {
                        isEletric = true;
                        break;
                    }
                }

                //违章
                if (isEletric) {
                    add(new PhaseAssist.Builder().setLkj(fault).setLkjId(lkjId).setStart(fault.getTime())
                            .setEnd(fault.getTime()).setItemEnum(ItemRecord.ItemEnum.I10_33)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_33).builder());
                }
            }

        }

    }
}
