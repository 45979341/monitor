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

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-21
 * @Description: 违规解除电制
 * 1、监控状态，牵引为"制动"，如电阻取消时（下一条记录不是”制动“），管压不为定压时则违章,完成
 */
@Service
public class Item34 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();

            if ("制动".equals(lkj.getTransaction())) {
                int t = i + 1;
                Lkj cur = list.get(t);
                while ("制动".equals(cur.getTransaction()) && t < list.size()) {
                    cur = list.get(t++);
                }
                i = t;
                if (NumberUtils.toInt(cur.getPipePressure()) < constPipe - 10) {
                    add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                            .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I10_34)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_34).builder());
                }
            }

        }
    }
}
