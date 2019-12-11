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
 * @date 2019/10/16 10:23
 * @description: 项点名称： 库内无工况试验
 * 在库内过程中完成一次
 * 非前牵<->非后牵 或者 非前制<->非后制
 * 缸压大于300 默认30秒内完成
 * 电力机车不分析
 */
@Service
public class Item8 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (HeadUtils.electricLocomotive(head)) {
            return;
        }
        for (int i = start; i < end; i++) {
            Lkj cur = list.get(i);
            Lkj next = list.get(i + 1);
            // 满足条件且和下一条不同
            if (conditions(cur) && !same(cur, next)) {
                do {
                    i++;
                    next = list.get(i + 1);
                    // 时间大于30秒 缸压小于等于300 不满足条件直接break
                    if (DateUtils.diffDate(cur.getTime(), next.getTime(), 30)
                            || NumberUtils.toInt(next.getCylinderPressure()) <= 300) {
                        break;
                    }
                    // 前后不同同为制动或牵引
                    if (!cur.getFrontBehind().equals(next.getFrontBehind()) && cur.getTransaction().equals(next.getTransaction()) && conditions(next)) {
                        // 做了 没违规 结束这个方法
                        return;
                    }
                } while (i < end);
            }
        }
        //没做就违章
        add(new PhaseAssist.Builder().setLkj(list.get(end).setRecordId(lkjId)).setLkjId(lkjId).setStart(list.get(end).getTime())
                .setEnd(list.get(end).getTime()).setItemEnum(ItemRecord.ItemEnum.I2_8)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_8).builder());
    }

    /**
     * 满足条件
     */
    private static boolean conditions(Lkj lkj) {
        return "非零".equals(lkj.getZeroBit())
                && ("牵引".equals(lkj.getTransaction()) || "制动".equals(lkj.getTransaction()))
                && ("向前".equals(lkj.getFrontBehind()) || "向后".equals(lkj.getFrontBehind()))
                && NumberUtils.toInt(lkj.getCylinderPressure()) > 300;
    }

    /**
     * 和一下条比较看是否相同
     */
    private static boolean same(Lkj lkj, Lkj next) {
        return lkj.getZeroBit().equals(next.getZeroBit())
                && lkj.getTransaction().equals(next.getTransaction())
                && lkj.getFrontBehind().equals(next.getFrontBehind())
                && NumberUtils.toInt(next.getCylinderPressure()) > 300;
    }
}
