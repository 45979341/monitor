package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 补机起车进级晚
 * 1、速度从0有速度的时候，走了8米要从”零位“变成”非零“
 */
@Service
public class Item131 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        if (head.getTrainType().contains("补")) {
            for (int i = start; i < end; i++) {
                Lkj lkj = list.get(i);
                if (lkj.getSpeed() == 0) {
                    int t = i;
                    Lkj cur = lkj;

                    while (cur.getSpeed() == 0 && t<list.size()) {
                        cur = list.get(++t);
                    }
                    Lkj temp = list.get(t-1);

                    boolean isChange = false;
                    //下次零之前是否走了八米
                    boolean isEight = true;
                    while (Math.abs(Math.abs(temp.getKiloMeter()) - Math.abs(cur.getKiloMeter())) <= 8 && t<list.size()-1) {
                        if ("非零".equals(cur.getZeroBit())) {
                            isChange = true;
                            break;
                        }
                        cur = list.get(++t);
                        if (cur.getSpeed() == 0) {
                            isEight = false;
                            i = t;
                            break;
                        }
                    }

                    if (isEight) {
                        if (isChange) {
                            i = t;
                            continue;
                        } else {
                            add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                    .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_131)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_131).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                            i = t;
                            continue;
                        }
                    }
                }
            }
        }

    }
}
