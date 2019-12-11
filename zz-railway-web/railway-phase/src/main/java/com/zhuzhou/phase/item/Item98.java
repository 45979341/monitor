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
 * @Author chenzeting
 * @Date 2019-10-18
 * @Description: 巡检违标
 * 1、在指定需要巡检区间内,必须有”前端巡检1” 、”后端巡检” 、”前端巡检2”的记录；
 * 2、”前端巡检1” 到”后端巡检”的时间必须大于规定的时间值(默认 60秒钟)；
 * 3、“前端巡检1” 到”前端巡检2”的时间必须大于规定的时间值(默认120秒钟)；
 * 4、“前端巡检1” 到”前端巡检2”的时间必须小于规定的时间值（默认300秒钟）；
 **/
@Service
public class Item98 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        for (int i = start;i < end;i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("前端巡检1".equals(eventItem)) {
                int t = 1;
                do {
                    Lkj next = list.get(i + t);
                    String nextEventItem = next.getEventItem();
                    if ("后端巡检".equals(nextEventItem)) {
                        //查出后端巡检，时间在60s——300s内，算正常
                        if(DateUtils.diffDate(lkj.getTime(), next.getTime(),60) && DateUtils.diffDate2(lkj.getTime(), next.getTime(), 300)) {
                            do {
                                Lkj next2 = list.get(i + t + 1);
                                String next2EventItem = next2.getEventItem();
                                if ("前端巡检2".equals(next2EventItem)) {
                                    //查出前端巡检2，时间在120s——300s内，算正常
                                    if(DateUtils.diffDate(lkj.getTime(), next2.getTime(),120) && DateUtils.diffDate2(lkj.getTime(), next2.getTime(), 300)) {
                                        //记录
                                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                                .setEnd(next2.getTime()).setItemRecordIllegal(4).setPhaseIllegal(1).setItemEnum(ItemRecord.ItemEnum.I31_98)
                                                .setPhaseEnum(Phase.PhaseEnum.LKJ_98).builder());
                                    } else {
                                        //违章
                                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                                .setEnd(next2.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_98)
                                                .setPhaseEnum(Phase.PhaseEnum.LKJ_98).builder());
                                    }
                                    break;
                                }
                                ++t;
                            } while(list.size() > i + t + 2);
                        } else {
                            //违章
                            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                    .setEnd(next.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_98)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_98).builder());
                            break;
                        }
                    }
                    ++t;
                } while(list.size() > i + t + 1);
            }
        }
    }
}
