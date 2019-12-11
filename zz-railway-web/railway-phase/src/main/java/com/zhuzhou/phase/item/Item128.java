package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-18
 * @Description: 超长列车缓解
 * 1、列车计长超过规定值或以上（默认70）；只分析货车
 * 2、机车前方信号为红黄，且速度为零的情况下，缓解列车（管压不能上升）；
 */
@Service
public class Item128 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (head.getTrainType().contains("货")) {
            if (head.getLength() > 70) {
                //包含每一段的符合的范围(红黄，速度为0)在进行判断管压是否上升
                List<List<Lkj>> yelReds = new ArrayList<>();
                List<Lkj> yelRed = new ArrayList<>();
                Lkj lkj = null;

                for (int i = start; i < end; i++) {
                    lkj = list.get(i);
                    //符合条件则加入当前的范围
                    if ("红黄".equals(lkj.getSignals()) && lkj.getSpeed() == 0) {
                        yelRed.add(lkj);
                    } else {    //不符合则加入包含符合的集合中
                        //要两条以上才
                        if (yelRed.size() > 1) {
                            yelReds.add(yelRed);
                            yelRed = new ArrayList<>();
                        }
                    }
                }

                //遍历每一个范围,判断是否正常
                for (List<Lkj> red : yelReds) {
                    Lkj cur = red.get(0);
                    Lkj temp = cur;
                    for (int i = 1; i < red.size(); i++) {
                        cur = red.get(i);
                        //如果当前范围内升压了，则违规
                        if (NumberUtils.toInt(temp.getPipePressure()) - NumberUtils.toInt(cur.getPipePressure()) < 0) {
                            add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                    .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_128)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_128).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                            break;
                        } else {
                            temp = cur;
                        }
                    }
                }
            }
        }
    }
}
