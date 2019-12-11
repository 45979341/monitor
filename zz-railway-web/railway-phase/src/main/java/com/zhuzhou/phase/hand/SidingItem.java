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

import java.util.List;

/**
 * @Author: chenzeting
 * Date:     2019/10/29
 * Description: 侧线运行
 */
@Service
public class SidingItem extends AbstractHandPhase {

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
        for (int i = 0;i < list.size();i++) {
            if (list.size() - 1 == i) {
                break;
            }
            Lkj lkj = list.get(i);
            String frontBehind = lkj.getFrontBehind();
            String signals = lkj.getSignals();
            String signalMachine = lkj.getSignalMachine();

            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("前")) {
                frontStatus = "前";
            } else if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("后")) {
                frontStatus = "后";
            }

            //收到双黄灯信号开始，且是进站信号机，到接到白灯信号。
            if (StringUtils.isNotEmpty(signalMachine) && signalMachine.startsWith("进站") && LkjSignalEnum.fromValue(signals, LkjSignalEnum.D_YELLOW)) {
                int t = 1;
                boolean flag = false;
                Lkj temp;
                do {
                    temp = list.get(i + t);
                    String tempSignals = temp.getSignals();
                    if (LkjSignalEnum.fromValue(tempSignals, LkjSignalEnum.WHITE)) {
                        flag = true;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t + 1);
                if (flag) {
                    // 侧线运行
                    hand(lkjId, dir, mp4List, frontStatus, null, lkj.getTime(), lkj.getTime(),
                            temp.getTime(), SignalMachineEnum.startValue(temp.getSignalMachine()), Phase.PhaseEnum.LKJ_HEAD_3);
                    i += t;
                }
            }
        }
    }
}
