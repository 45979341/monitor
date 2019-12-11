package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.garageutils.GarageUtils;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-17
 * @Description: 摘解未保压
 * 1、最后一次进站，到lkj文件结束，未出现（进入调车），不分析。如果出现（进入调车），
 * 最后一次停车到（进入调车）之间必须出现一次管压下降到0，而且（调车开车）前一条数据必须为定压。
 */
@Service
public class Item27 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        List<Lkj> list1 = new ArrayList<>();
        boolean isPipeZero = false;     //第一次进入调车前是否管压为0
        boolean isEnterChange = false;  //是否进入调车
        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();


        for (int i = end; i > start; i--) {     //获取到最后一次进站到结束的片段
            if (list.get(i).getEventItem().equals("进站")) {
                list1.add(list.get(i));
                break;
            }
            list1.add(list.get(i));
        }

        Collections.reverse(list1);     //反转list

        Lkj cur = null;
        int t = 0;

        for (int i = 1; i < list1.size(); i++) {
            cur = list1.get(i);
            if (NumberUtils.toInt(cur.getPipePressure()) == 0) {    //进入调车前是否管压为0
                isPipeZero = true;
            }
            if (cur.getEventItem().equals("进入调车")) {    //是否有进入调车状态
                isEnterChange = true;
                t = i;
                break;
            }
        }

        if (isEnterChange) {       //未进入调车则直接跳过
            for (int i = t;i<list1.size();i++) {
                cur = list1.get(i);
                if (cur.getEventItem().equals("调车开车")) {
                    if ((NumberUtils.toInt(list1.get(i-1).getPipePressure()) >= constPipe - 10 && NumberUtils.toInt(list1.get(i-1).getPipePressure()) <= constPipe + 10)
                            && isPipeZero) {    //调车开车后，管压有过为0并前一条数据定压则正常
                        return;
                    } else {    //违规
                        add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I8_27)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_27).builder());
                        break;
                    }
                }
            }
        }

    }
}
