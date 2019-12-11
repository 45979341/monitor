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
import java.util.stream.Collectors;

/**
 *  姓名：刘欣武
 *  时间：2019-10-18
 *  项点名称：1、列车运行途中，站内停车时间超过规定值（默认20分钟），必须执行一次打点（事件:定标键）；
 *           2、且停车时间范围内，每隔该设定值必须打点一次；
 *  项点描述：
 *  项点开发：
 *  分析结果：
 */
@Service
public class Item119 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //组装数据
        LinkedHashMap<String, List<Lkj>> map = LkjListUtil.makeDate(list, lkjId, start,end, "站内停车", "站内开车");
        //分析
        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> values = entry.getValue();
            List<String> collect = values.stream().map(Lkj::getEventItem).collect(Collectors.toList());
            if(!collect.contains("定标键")){
                Lkj lkj = values.get(0);
                //记录
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                        .setItemRecordIllegal(4).setPhaseIllegal(1)
                        .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_119)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_119).builder());
            }

        }


    }
}
