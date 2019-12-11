package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.lkj.LkjSignalEnum;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-18
 * @Description: 信号不良出库
 * 1、 信号栏中绿灯、绿黄、黄灯、双黄、双黄闪、黄2、黄2闪,红黄、红黄闪环线跳动显示不少于1次；
 * 2、 补机纳入分析;
 * 3、 检索范围库内作业；
 */
@Service
public class Item112 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        List<Lkj> before = new ArrayList<>();
        Lkj pos = list.get(end);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int g = 0;
        int h = 0;
        int i = 0;
        int j = 0;

        for (int i1 = start; i1 < end; i1++) {
            Lkj lkj = list.get(i1);

            if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.GREEN)) {
                a++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.GREEN_YELLOW)) {
                b++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.YELLOW)) {
                c++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.D_YELLOW)) {
                d++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.D_YELLOW_FLASH)) {
                e++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.YELLOW2)) {
                f++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.YELLOW2_FLASH)) {
                g++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.RED_YELLOW)) {
                h++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.RED)) {
                i++;
            } else if (LkjSignalEnum.fromValue(lkj.getSignals(),LkjSignalEnum.WHITE)) {
                j++;
            }
        }

        //每种灯至少出现一次
        if (a >= 1 && b >= 1 && c >= 1 && d >= 1 &&e >= 1 &&f >= 1 &&g >= 1 &&h >= 1&&i >= 1&&j>=1) {
            return;
        } else {
            add(new PhaseAssist.Builder().setLkj(pos).setLkjId(lkjId).setStart(pos.getTime())
                    .setEnd(pos.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_112)
                    .setPhaseEnum(Phase.PhaseEnum.LKJ_112).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
        }


    }
}
