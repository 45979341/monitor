package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-17
 * @Description: 电制带闸
 * 1、列车运行途中，列车使用电阻制动，连续规定时间后（默认5秒），其闸缸压力大于规定值(默认150kpa)；则违章
 * 2、速度为零不分析；
 * 3、调车状态不分析；
 */
@Service
public class Item35 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        List<Lkj> list1 = new ArrayList<>();

        for (int i = start; i < end; i++) {

            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();

            if (lkj.getSpeed() > 0) {
                list1.add(lkj);
            } else {
                if (list1.size() > 0) {
                    Lkj cur = list1.get(0);
                    //Lkj temp = cur;
                    ArrayList<Lkj> list2 = new ArrayList<>();

                    for (int j = 0; j < list1.size()-1; j++) {
                        cur = list1.get(j);
                        if ("制动".equals(cur.getTransaction())) {
                            list2.add(cur);
                        } else {
                            if (list2.size() >= 2) {
                                if (DateUtils.diffDate(list2.get(0).getTime(),list2.get(list2.size()-1).getTime(),5)) {
                                    for (Lkj lkj1 : list2) {
                                        if (NumberUtils.toInt(lkj1.getCylinderPressure()) > 150) {
                                            add(new PhaseAssist.Builder().setLkj(lkj1).setLkjId(lkjId).setStart(lkj1.getTime())
                                                    .setEnd(lkj1.getTime()).setItemEnum(ItemRecord.ItemEnum.I10_35)
                                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_35).builder());
                                            break;
                                        }
                                    }
                                }
                            }
                            j = j + list2.size();
                            list2 = new ArrayList<>();
                            continue;
                        }
                    }
                }
                list1 = new ArrayList<>();
            }
        }
    }
}
