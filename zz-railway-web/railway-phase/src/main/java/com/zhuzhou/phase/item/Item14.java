package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  姓名：刘欣武
 *  时间：2019-10-15
 *  项点名称：未按规定对标
 *  项点描述：
 *  项点开发：1.在LKJ监控状态下，LKJ事件中有“降级开车”后在后面的事件项中出现了"车位向前',"车位向后”，“车位对中”，“过机不校”4种情况中的任一种情况时，都判为未按规定对标。
 *           2.在开车后的连续两个通过信号机间出现的以上4情况都算
 *  分析结果：中标则违章
 */
@Service
public class Item14 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //开车对标-过信号机
        LinkedHashMap<String, List<Lkj>> map = LkjListUtil.makeDate(list, lkjId,start, end, "开车对标", "过信号机");
        for(Map.Entry<String, List<Lkj>> entry:map.entrySet()){
            analyseTargetList(entry.getValue());
        }
    }

    public void analyseTargetList(List<Lkj> targetList){
        // list里必须含有两个 【车位向前',"车位向后”，“车位对中”，“过机不校”】
        int count = 0;
        int size = targetList.size();
        for(int i=0;i<size;i++){
            Lkj lkj = targetList.get(i);
            String event = lkj.getEventItem();
            if("车位向前".equals(event)||"车位向后".equals(event)||"车位对中".equals(event)||"过机不校".equals(event)){
                count++;
            }
        }
        if(count>=1){
            //查看过信号机与前一条记录的公里标差值与100米对比
            Lkj target = targetList.get(size-1);
            Lkj before = targetList.get(size-2);
            if(Math.abs(target.getKiloMeter()-before.getKiloMeter())*1000>=100){
                //违章
                add(new PhaseAssist.Builder().setLkj(target).setLkjId(target.getRecordId()).setStart(target.getTime())
                        .setEnd(target.getTime()).setItemEnum(ItemRecord.ItemEnum.I5_14)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_14).builder());
            }
        }

    }
}
