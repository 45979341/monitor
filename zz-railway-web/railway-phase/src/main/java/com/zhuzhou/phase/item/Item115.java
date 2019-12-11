package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/18 14:35
 * @description: 项点名称:重联补机过早提手柄
 * 只分析补机
 * 速度从0到非0，走行距离小于10米，如果是零位变成非零位。记录
 */
@Service
public class Item115 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.bu(head)) {
            return;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            Lkj lkj = list.get(i);
            Lkj next = list.get(i + 1);
            boolean condition = lkj.getSpeed() == 0 && next.getSpeed() != 0
                    && Math.abs(lkj.getDistance() - next.getDistance()) < 10 && Math.abs(lkj.getDistance() - next.getDistance()) > 0
                    && "零位".equals(lkj.getZeroBit()) && "非零".equals(next.getZeroBit());
            if (condition) {
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                        .setEnd(lkj.getTime()).setItemRecordIllegal(4).setItemEnum(ItemRecord.ItemEnum.I32_115)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_115).builder());
                i++;
            }
        }
    }
}
