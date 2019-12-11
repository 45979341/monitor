package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 确认调车信号时间短
 * 1、调车状态下，每次停车后速度为0开始计时，8秒内无手柄转非零的记录；只分析库内车次，补机，其他不分析；
 */
@Service
public class Item117 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        if (head.getTrainType().contains("补")) {
            for (int i = start;i<end;i++) {
                Lkj lkj = list.get(i);
                String eventItem = lkj.getEventItem();

                if (lkj.getSpeed() == 0) {
                    int t = i+1;
                    Lkj temp = lkj;
                    Lkj cur = list.get(t);
                    boolean isChange = false;
                    while (!DateUtils.diffDate(temp.getTime(),cur.getTime(),8) && t<list.size()) {
                        if ("非零".equals(cur.getZeroBit())) {
                            isChange = true;
                        }
                        cur = list.get(++t);
                    }
                    if (isChange) {
                        i = t;
                        continue;
                    } else {
                        i = t;
                        add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_117)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_117).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        continue;
                    }
                }

            }
        }
    }
}
