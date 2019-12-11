package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/22 16:44
 * @description: 动车小闸试验
 * 1、 判断范围，在机车进入终点站，进入“调车”模式后，第一次起速为起点，到下次起车，速度小于10为终点，如果不大于10以下次停车位终点；
 * 2、 判断条件，闸缸压力不小于规定值（默认50kpa），且降速不小于规定值（默认3km/h）；管压不变，为定压。
 */
@Service
public class Item135 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int shuntIndex = 0;

        for (int i = list.size() - 1; i > 0; i--) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            // 找到最后一次进站的索引,都还没跳出去,那就结束方法
            if ("进站".equals(eventItem)) {
                return;
            }
            // 找到最后一次调车开车的索引
            if ("调车开车".equals(eventItem)) {
                shuntIndex = i;
                break;
            }
        }
        // 最后一次调车开车在最后一次进站之前
        boolean flag = false;
        Date endTime = null;
        Lkj lkj = list.get(shuntIndex);
        for (int i = shuntIndex + 1; i < list.size(); i++) {
            Lkj cur = list.get(i);
            if ("调车停车".equals(cur.getEventItem())) {
                endTime = cur.getTime();
                break;
            }
            if (NumberUtils.toInt(cur.getCylinderPressure()) > 50 && cur.getSpeed() >= 3) {
                // 没违规
                flag = true;
                break;
            }
        }
        if (!flag) {
            // 违规
            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                    .setEnd(endTime).setItemEnum(ItemRecord.ItemEnum.I32_135)
                    .setPhaseEnum(Phase.PhaseEnum.LKJ_135).setPhaseIllegal(1).builder());
        }
    }
}
