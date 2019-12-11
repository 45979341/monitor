package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-18
 * @Description: 列车站内停车违规
 * 1、监控状态下。
 * 2、单机不判定，只检测客本，进站到出站间，出现两次站内以上开车，违章
 * 3、终点站未出现出站，不需要分析，也就是进站到出站是一对的。
 */
@Service
public class Item78 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        //存储每一对进站出站
        List<List<Lkj>> inOut = new ArrayList<>();
        boolean isIn = false;
        boolean isOut = false;

        if (HeadUtils.startWith5Length5(head)) {
            return;
        } else {
            if (head.getTrainType().contains("客")) {

                List<Lkj> list1 = new ArrayList<>();

                Lkj lkj = null;

                for (int i = start; i < end; i++) {

                    lkj = list.get(i);

                    String eventItem = lkj.getEventItem();

                    if (lkj.getEventItem().equals("进站")) {
                        isIn = true;
                    }

                    if (isIn && lkj.getEventItem().equals("出站")) {
                        list1.add(lkj);
                        isOut = true;
                    } else if (isIn) {
                        list1.add(lkj);
                    }

                    if (isIn && isOut) {
                        isIn = false;
                        isOut = false;
                        inOut.add(list1);
                        list1 = new ArrayList<>();
                    }

                }

                for (List<Lkj> lkjs : inOut) {

                    boolean inRegular = false;

                    for (int i = 0; i < lkjs.size(); i++) {
                        Lkj cur = lkjs.get(i);

                        if (cur.getSpeed() == 0) {

                            boolean zero = true;
                            boolean move = false;
                            int t = i + 1;

                            while (t < lkjs.size()) {
                                if (lkjs.get(t).getSpeed() > 0) {
                                    break;
                                }
                                t++;
                            }

                            while (t < lkjs.size()) {
                                if (lkjs.get(t).getSpeed() == 0) {
                                    add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                            .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_78)
                                            .setPhaseEnum(Phase.PhaseEnum.LKJ_78).builder());
                                    break;
                                }
                                t++;
                            }
                            i = t;
                        }
                    }
                }

            }
        }

    }
}
