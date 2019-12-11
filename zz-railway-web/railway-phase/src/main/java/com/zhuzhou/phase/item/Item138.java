package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/18 16:57
 * @description: 项点名称:停车后手柄未回零
 * 列车运行途中，停车后规定时间内（默认30秒），必须回零位，否则违章；
 */
@Service
public class Item138 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = 0; i < list.size() - 1; i++) {
            Lkj lkj = list.get(i);
            Lkj next = list.get(i + 1);
            if (lkj.getSpeed() != 0 && next.getSpeed() == 0) {
                Date startTime = next.getTime();
                for (i += 1; i < list.size(); i++) {
                    Lkj cur = list.get(i);
                    Date endTime = cur.getTime();
                    if (!DateUtils.diffDate(startTime, endTime, 30)) {
                        if ("零位".equals(cur.getZeroBit()) || cur.getSpeed() != 0) {
                            break;
                        }
                    } else {
                        // 记录
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(startTime)
                                .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I33_138)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_138).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                        break;
                    }
                }
            }
        }
    }
}
