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
 * @Date 2019-10-18
 * @Description: 未缓解加载
 * 1.在列车运行中，机车速度大于零。
 * 2.检测机车工况在“非零和牵引”位，管压不为定压，既违章。注意：连续段都必须为同一个违章点
 */
@Service
public class Item75 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        List<Lkj> inRegular = new ArrayList<>();

        Lkj lkj = null;

        for (int i = start; i < end; i++) {

            lkj = list.get(i);

            String pipe = lkj.getPipePressure();

            if (lkj.getSpeed() > 0
                    && (lkj.getZeroBit().equals("非零") && lkj.getTransaction().equals("牵引"))
                    && (NumberUtils.toInt(pipe) < constPipe - 20 && NumberUtils.toInt(pipe) > constPipe + 20)) {
                inRegular.add(lkj);
            } else {
                if (inRegular.size() >= 1) {
                    if (DateUtils.diffDate2(inRegular.get(0).getTime(),inRegular.get(inRegular.size()-1).getTime(),2)) {
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_75)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_75).builder());
                    }
                    inRegular = new ArrayList<>();
                }
            }
        }
    }
}
