package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-17
 * @Description: 停车后未回手柄
 * 1、列车运行途中，停车规定时间后（默认10秒），手柄还在“非X制”位；
 * 2、列车运行途中，停车规定时间后（默认10秒），手柄还在“非X牵”位；
 * 3、降级或调车状态不分析；
 */
@Service
public class Item79 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        List<Lkj> list1 = new ArrayList<>();

        for (int i = start; i < end; i++) {

            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();

            if (lkj.getSpeed() == 0) {
                list1.add(lkj);
            } else {
                if (list1.size() > 1) {
                    Lkj temp = list1.get(0);
                    Lkj cur = null;

                    for (int j = 1; j < list1.size(); j++) {
                        cur = list1.get(j);
                        if (DateUtils.diffDate(temp.getTime(),cur.getTime(),10)) {
                            if ("非零".equals(cur.getZeroBit()) && ("牵引".equals(cur.getTransaction()) || "制动".equals(cur.getTransaction()))) {
                                add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                        .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_79)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_79).builder());
                                break;
                            }
                        }
                    }
                }
                list1 = new ArrayList<>();
            }
        }


    }
}
