package com.zhuzhou.phase.hand;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.lkj.LkjSignalEnum;
import com.zhuzhou.enums.lkj.SignalMachineEnum;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractHandPhase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chenzeting
 * Date:     2019/10/29
 * Description: 准备停车信号
 */
@Service
public class ReadyStopItem extends AbstractHandPhase {

    /**
     * lkj 判断1000米内
     */
    private static final int LKJ_DISTANCE = 1000;

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

            //信号
            String signals = lkj.getSignals();
            int speed = lkj.getSpeed();
            //监控状态
            if (monitorStatus == 1 && speed > 0 && LkjSignalEnum.fromValue(signals, LkjSignalEnum.YELLOW)) {
                Date start = lkj.getTime();
                //1、监控状态。
                //2、速度大于0km/h。
                //3、收到黄灯信号，判断区域：距离进站(进路)信号800m阶段；出站、通过、接近、预告信号800（600）；
                // 信号表示器100m；到跨越信号机，信号机编号变化。
                int t = 1;
                boolean flag = false;
                Lkj next;
                Lkj pre = lkj;
                do {
                    next = list.get(i + t);
                    String nextSignals = next.getSignals();
                    //下一条记录不满足条件
                    if (speed == 0 || !LkjSignalEnum.fromValue(nextSignals, LkjSignalEnum.YELLOW)) {
                        break;
                    }
                    int nextDistance = next.getDistance();
                    if (StringUtils.isEmpty(next.getSignalMachine())) {
                        break;
                    }
                    //如何距离小于上一条数据，则代表越过信号机
                    if (pre.getDistance() < next.getDistance()) {
                        //信号机发生改变
                        flag = true;
                        break;
                    } else {
                        if (nextDistance >= LKJ_DISTANCE) {
                            start = next.getTime();
                        }
                    }
                    pre = next;
                    ++t;
                } while (list.size() > i + t + 1);

                if (flag) {
                    // 准备停车信号
                    Date clone = start == null ? lkj.getTime() : start;
                    hand(lkjId, dir, mp4List, frontStatus, lkj.getTime(), clone, clone,
                            next.getTime(), SignalMachineEnum.startValue(lkj.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_2);
                    i += t;
                }
            }
        }
    }
}
