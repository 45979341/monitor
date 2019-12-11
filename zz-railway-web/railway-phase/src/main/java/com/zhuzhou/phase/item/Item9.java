package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/21 10:49
 * @description: 项点名称：库内无高压试验
 * 1、非电力机车不分析；
 * 2、机车库内在停车状态下，有事件工况前后变化、主断断开、主断闭合和自动过分相动作记录；
 * 3、未装功能扩展盒机车除外；
 * 4、分析范围为库内作业（乘务员上车至闸楼处）；
 */
@Service
public class Item9 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.electricLocomotive(head)) {
            return;
        }
        boolean b = list.stream().limit(end).skip(start).anyMatch(lkj ->
                NumberUtils.toInt(lkj.getSpeedElectricity()) > 20 && lkj.getSpeed() == 0);
        if (!b) {
            Lkj lkj = list.get(end);
            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                    .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I2_9)
                    .setPhaseEnum(Phase.PhaseEnum.LKJ_9).builder());
        }
    }
}
