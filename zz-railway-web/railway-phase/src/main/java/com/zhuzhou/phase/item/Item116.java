package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 电制转牵引时间短
 * 1、客车不分析；只分析货车
 * 2、机车工况由“非X制”位转为“非X牵”位，时间差小于规定值（默认10秒）；
 * 3、机车速度为非零；
 */
@Service
public class Item116 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (head.getTrainType().contains("货")) {
            for (int i = start; i < end; i++) {
                Lkj cur = list.get(i);
                if ("非零".equals(cur.getZeroBit()) && "制动".equals(cur.getTransaction())) {
                    if (cur.getSpeed() > 0) {
                        Lkj temp = cur;
                        int t = i + 1;
                        cur = list.get(t);

                        //过程中速度不能为0
                        boolean isSpeed1 = false;
                        boolean isSpeed2 = false;

                        //找到最后一个非零制
                        while ("非零".equals(cur.getZeroBit()) && "制动".equals(cur.getTransaction())
                                && t<list.size()) {
                            temp = cur;
                            cur = list.get(++t);
                            if (cur.getSpeed() == 0) {
                                isSpeed1 = true;
                                break;
                            }
                        }

                        if (!isSpeed1) {
                            while (!("非零".equals(cur.getZeroBit()) && "牵引".equals(cur.getTransaction()))
                                    && t<list.size()) {
                                cur = list.get(++t);
                                if (cur.getSpeed() == 0) {
                                    isSpeed2 = true;
                                    break;
                                }
                            }

                            if (!isSpeed2) {
                                if (DateUtils.diffDate(temp.getTime(),cur.getTime(),10)) {
                                    i = t + 1;
                                    continue;
                                } else {
                                    add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                            .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_116)
                                            .setPhaseEnum(Phase.PhaseEnum.LKJ_116).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                                    i = t + 1;
                                    continue;
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
