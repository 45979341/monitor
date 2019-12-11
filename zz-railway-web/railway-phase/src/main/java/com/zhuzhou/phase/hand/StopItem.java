package com.zhuzhou.phase.hand;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.lkj.LkjSignalEnum;
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
 * Description: 停车信号
 */
@Service
public class StopItem extends AbstractHandPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, String dir, List<Mp4> mp4List) {

        //前：一端，后：二端
        String frontStatus;
        String option = head.getOption();
        if ("I端".equals(option)) {
            frontStatus = "前";
        } else {
            frontStatus = "后";
        }
        // 停车事件
        for (int i = 0;i < list.size();i++) {
            if (list.size() - 1 == i) {
                break;
            }
            Lkj lkj = list.get(i);
            String frontBehind = lkj.getFrontBehind();
            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("前")) {
                frontStatus = "前";
            } else if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("后")) {
                frontStatus = "后";
            }

            int speed = lkj.getSpeed();
            String signals = lkj.getSignals();
            //1、速度大于0km/h.
            //2、判断区域为：收到红灯或红黄信号，直到速度为0km/h时。
            if (speed > 0 && LkjSignalEnum.fromValue(signals, LkjSignalEnum.RED, LkjSignalEnum.RED_YELLOW)) {
                Lkj temp;
                int t = 1;
                do {
                    temp = list.get(i + t);
                    // 信号灯不为红 或 红黄时（往上找最后停车时）
                    if (!LkjSignalEnum.fromValue(temp.getSignals(), LkjSignalEnum.RED, LkjSignalEnum.RED_YELLOW)) {
                        hand(lkjId, dir, mp4List, frontStatus, null, temp.getTime(), DateUtils.opSecond(lkj.getTime(), -10),
                                temp.getTime(), SignalMachineEnum.startValue(lkj.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_4);
                        i += t;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t + 1);
            }
        }
    }
}
