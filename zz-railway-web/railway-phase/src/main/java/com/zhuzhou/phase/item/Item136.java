package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  姓名：刘欣武
 *  时间：2019-10-22
 *  项点名称：中间站停车未检查闸瓦间隙
 *  项点描述：1、单机、补机不分析；
 *          2、中间站停车时间大于规定值（默认5分钟）；（ 不包含终点跟起始站）
 *          3、制动缸压力必须小于规定值（默认30KPA），且保压规定时间值以上（默认2分钟）；
 *          4、列车管压力必须下降规定值以上（默认100KPA）；
 *  项点开发：
 *  分析结果：
 */
@Service
public class Item136 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if(HeadUtils.startWith5Length5(head)||HeadUtils.bu(head)){
            return;
        }
        //组装数据
        LinkedHashMap<String, List<Lkj>> map = LkjListUtil.makeDate(list, lkjId, start,end, "进站", "出站");
        //分析
        for(Map.Entry<String, List<Lkj>> entry:map.entrySet()){
            List<Lkj> needList = entry.getValue();//进站-出站  数据
            //找到区间内最后一个站内停车-站内开车 数据段
            LinkedHashMap<String, List<Lkj>> erMap = LkjListUtil.makeDate(needList, lkjId, start,end, "站内停车", "站内开车");
            if(erMap!=null && erMap.size()>0){
                //只分析最后一段
                Map.Entry<String, List<Lkj>> tail = LkjListUtil.getTail(erMap);
                List<Lkj> target = tail.getValue();
                //5分钟内
                Lkj first = target.get(0);
                Lkj last = target.get(target.size()-1);
                if(DateUtils.diffDate(first.getTime(),last.getTime(),5*60)){
                    //分析3，4条件
                    if(!analyseData(target)){
                        //记录
                        add(new PhaseAssist.Builder().setLkj(first).setLkjId(lkjId).setStart(first.getTime())
                                .setItemRecordIllegal(4).setPhaseIllegal(1)  //默认0:违章   1：正常  2：记录
                                .setEnd(last.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_136)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_136).builder());
                    }
                }
            }else{
                //进站--出站之间，不包含有站内停车-站内开车，则过滤
            }
        }
    }

    //分析站内停车-站内开车数据
    public boolean analyseData(List<Lkj> list){
        int size = list.size();
        int index = 0;
        boolean bool = true;//默认有符合条件的数据
        do{
            List<Lkj> target = new ArrayList<>();//缸压小与30的列表
            for(int i=index;i<size;i++){
                index = i+1;
                Lkj current = list.get(i);
                Lkj next = list.get(i+1==size?i:i+1);
                if(NumberUtils.toInt(current.getCylinderPressure())<30){
                    target.add(current);
                    if(NumberUtils.toInt(next.getCylinderPressure())>=30){
                        break;
                    }
                }
            }
            //判断
            if(target.size()>0){
                Lkj first = target.get(0);
                Lkj last = target.get(target.size()-1);
                //保压为2分钟以上
                if(DateUtils.diffDate(first.getTime(),last.getTime(),2*60)){
                    //判断列车管压力
                    if(Math.abs(NumberUtils.toInt(first.getPipePressure())-NumberUtils.toInt(last.getPipePressure()))>=100){
                        bool = true;
                    }else{
                        bool = false;
                    }
                }else{
                    bool = false;
                }
            }else{
                bool = false;
            }

        }while(!bool && index<size);
        return bool;
    }
}
