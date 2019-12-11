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

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-21
 * @Description: 小闸未缓解
 * 1、牵引为"制"，管压变为0后延时5秒后缸压大于50kpa,违章
 */
@Service
public class Item36 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();

            if ("制动".equals(lkj.getTransaction())) {
                int t = i+1;
                Lkj cur = list.get(t);
                //紧急制动：管压突变为0
                if (NumberUtils.toInt(lkj.getPipePressure()) > 0 && NumberUtils.toInt(cur.getPipePressure()) == 0) {
                    Lkj temp = cur;
                    //延后5s后
                    while (t < list.size()) {
                        cur = list.get(++t);
                        if (DateUtils.diffDate(temp.getTime(),cur.getTime(),5)) {
                            if (NumberUtils.toInt(cur.getCylinderPressure()) > 50) {
                                add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                        .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I10_36)
                                        .setPhaseEnum(Phase.PhaseEnum.LKJ_36).builder());
                            }
                            break;
                        }
                    }
                }
            }

        }
    }
}
