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
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 两段制动
 * 监控非调车状态下，分析范围：进站到出站。
 * 1.速度为0，管压从非定压恢复到定压，再次从定压到非定压。期间不超过20秒
 */
@Service
public class Item121 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();
        Integer constDown = constPipe - 10;
        Integer constUp = constPipe + 10;
        List<Lkj> list1 = new ArrayList<>();

        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();

            Lkj cur = lkj;
            int t = i;

            //进出站为一对
            if ("进站".equals(lkj.getEventItem())) {
                while (t < list.size()) {
                    //进站到出站速度必须为0的情况下
                    if (cur.getSpeed() == 0) {
                        list1.add(cur);
                        cur = list.get(++t);
                    } else {
                        break;
                    }
                    if ("出站".equals(cur.getTransaction())) {
                        list1.add(cur);
                        break;
                    }
                }
            }

            //进入当前符合的范围查找必须进出站一对
            if (list1.size() > 0) {
                boolean isConst = false;
                boolean isNotConst = false;
                Lkj temp = null;
                for (int j = 0; j < list1.size(); j++) {
                    lkj = list1.get(j);
                    //非定压
                    if (NumberUtils.toInt(lkj.getPipePressure()) < constDown) {
                        int t1 = j + 1;
                        temp = lkj;
                        cur = list1.get(t1);
                        while (t1 < list1.size()) {
                            if (NumberUtils.toInt(cur.getPipePressure()) >= constDown
                                    && NumberUtils.toInt(cur.getPipePressure()) <= constUp) {
                                isConst = true;
                            }
                            if (isConst) {
                                if (NumberUtils.toInt(cur.getPipePressure()) < constDown) {
                                    isNotConst = true;
                                    break;
                                }
                            }
                            cur = list1.get(t1++);
                        }
                    }
                    //判断是否违规
                    if (isConst && isNotConst) {
                        if (DateUtils.diffDate2(temp.getTime(),cur.getTime(),20)) {
                            add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                    .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_121)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_121).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        }
                        break;
                    } else {
                        break;
                    }
                }

                list1 = new ArrayList<>();
            }
            i = t;

        }

    }
}
