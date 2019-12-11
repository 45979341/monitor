package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.lkj.LkjSignalEnum;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-10-18
 * @Description: 违章使用调车键
 * 1、由监控状态进入调车状态，红黄灯下无规定次数以上换向记录（默认3次）；
 **/
@Service
public class Item114 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        //监控状态 1：监控，2：降级
        int monitorStatus = 0;
        // 调车状态 1：调车，2：非调车
        int shuntStatus = 0;
        for (int i = 0;i < list.size();i++) {
            if (i == list.size() - 1) {
                break;
            }
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("开车对标".equals(eventItem)) {
                monitorStatus = 1;
            } else if ("退出调车".equals(eventItem) || "出站".equals(eventItem)) {
                shuntStatus = 2;
            } else if ("进入调车".equals(eventItem)) {
                shuntStatus = 1;
                monitorStatus = 2;
            } else if (eventItem.contains("降级")) {
                monitorStatus = 2;
            }
            if (monitorStatus == 1) {
                Lkj next = list.get(i + 1);
                String nextEventItem = next.getEventItem();
                if ("进入调车".equals(nextEventItem)) {
                    int t = 1;
                    //统计前后转换次数
                    int count = 0;
                    do {
                        Lkj temp = list.get(i + t);
                        String tempSignals = temp.getSignals();
                        String front = temp.getFrontBehind();
                        //如果为红黄信号灯
                        if (LkjSignalEnum.fromValue(tempSignals, LkjSignalEnum.RED_YELLOW, LkjSignalEnum.RED)) {
                            do {
                                Lkj cur = list.get(i + t + 1);
                                if (!cur.getFrontBehind().equals(front)) {
                                    ++count;
                                    if (count == 3) {
                                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(temp.getTime())
                                            .setEnd(cur.getTime()).setItemRecordIllegal(4).setPhaseIllegal(1).setItemEnum(ItemRecord.ItemEnum.I32_124)
                                            .setPhaseEnum(Phase.PhaseEnum.LKJ_114).builder());
                                        break;
                                    }
                                    front = cur.getFrontBehind();
                                }
                                ++t;
                            } while (list.size() > i + t + 2);
                            break;
                        }
                        ++t;
                    } while(list.size() > i + t + 1);
                }
            }
        }
    }
}
