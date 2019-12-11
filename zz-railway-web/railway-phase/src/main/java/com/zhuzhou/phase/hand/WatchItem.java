package com.zhuzhou.phase.hand;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.lkj.SignalMachineEnum;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractHandPhase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenzeting
 * Date:     2019/10/29
 * Description: 探身瞭望，确认仪表项点
 */
@Service
public class WatchItem extends AbstractHandPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, String dir, List<Mp4> mp4List) {

        //监控状态 1：监控，2：降级
        int monitorStatus = 0;
        // 调车状态 1：调车，2：非调车
        int shuntStatus = 0;
        //前：一端，后：二端
        String frontStatus;
        String option = head.getOption();
        if ("I端".equals(option)) {
            frontStatus = "前";
        } else {
            frontStatus = "后";
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                break;
            }
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            Double kiloMeter = lkj.getKiloMeter();
            int speed = lkj.getSpeed();
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
            String frontBehind = lkj.getFrontBehind();
            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("前")) {
                frontStatus = "前";
            } else if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("后")) {
                frontStatus = "后";
            }

            //监控非调车
            if (monitorStatus == 1 && shuntStatus == 2) {
                if (speed > 0) {
                    // 1.分相前，分相后500m内.(过分相)
                    // TODO 2. 出站后500m内
                    if ("过分相".equals(eventItem)) {
                        // 确认仪表
                        hand(lkjId, dir, mp4List, frontStatus, null, DateUtils.opSecond(lkj.getTime(), -20), DateUtils.opSecond(lkj.getTime(), -20),
                                DateUtils.opSecond(lkj.getTime(), 20), SignalMachineEnum.startValue(lkj.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_6);
                    } else if ("出站".equals(eventItem)) {
                        int t = 1;
                        Lkj cur;
                        do {
                            cur = list.get(i + t);
                            Double curKiloMeter = cur.getKiloMeter();
                            if (kiloMeter == null || curKiloMeter == null) {
                                break;
                            }
                            //出站500米
                            if (Math.abs(kiloMeter - curKiloMeter) >= 0.5) {
                                // 确认仪表
                                hand(lkjId, dir, mp4List, frontStatus, null, DateUtils.opSecond(lkj.getTime(), -5), DateUtils.opSecond(lkj.getTime(), -5),
                                        cur.getTime(), SignalMachineEnum.startValue(lkj.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_6);
                                i += t;
                                break;
                            }
                            ++t;
                        } while (list.size() > i + 1);
                    }
                }
            }

            // 调车开车之前1分内钟。
            if ("调车开车".equals(eventItem) || "降级开车".equals(eventItem) || "站内开车".equals(eventItem)) {
                // 探身眺望
                hand(lkjId, dir, mp4List, frontStatus, null, lkj.getTime(),
                        DateUtils.opSecond(lkj.getTime(), -60), lkj.getTime(), SignalMachineEnum.startValue(lkj.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_7);
            }
        }
    }
}
