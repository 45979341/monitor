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
import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-21
 *  项点名称：停车后小闸未制动
 *  项点描述：1、列车stop车后，机车速度在规定值内（默认5KM/H），如果列车管压力低于定压规定值（默认30KPA），则记录一次；
 *          2、单机停车时，制动缸压力小于规定值（默认50KPA），持续规定时间以上（默认5秒），则记录一次；
 *          3、调车状态不分析；
 *  项点开发：监控非调车状态；
 *  分析结果：
 */
@Service
public class Item103 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //监控非调车才分析
        List<Lkj> data = LkjListUtil.getControlData(list,start,end);//只能有一段数据

        if(HeadUtils.startWith5Length5(head)){
            //单机分析2
            analyseData2(data,lkjId);
        }else{
            //剩下的分析1
            analyseData1(data,lkjId);
        }
    }

    public void analyseData1(List<Lkj> list,String lkjId){
        int index = 0;
        int size = list.size();
        do{
            List<Lkj> result = new ArrayList<>();
            for(int i=index;i<size;i++){
                index = i+1;
                Lkj current = list.get(i);
                Lkj next = list.get(i+1==size?i:i+1);
                if(current.getSpeed()<=5){
                    result.add(current);
                    if(current.getSpeed()==0&&next.getSpeed()>0){
                        break;
                    }
                }
            }
            //判断
            if(result.size()>0){
                Lkj first = result.get(0);
                Lkj lkj = result.get(result.size()-1);
                if(NumberUtils.toInt(lkj.getPipePressure())<30){
                    //记录
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(first.getTime())
                            .setItemRecordIllegal(4).setPhaseIllegal(1)  //默认0:违章   1：正常  2：记录
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_103)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_103).builder());
                }
            }
        }while(index<size);

    }

    public void analyseData2(List<Lkj> data,String lkjId){
        int index = 0;
        do{
            int size = data.size();
            List<Lkj> result = new ArrayList<>();
            for(int i=index;i<size;i++){
                Lkj lkj = data.get(i);
                Lkj next = data.get(i+1==size?i:i+1);
                Integer cp = NumberUtils.toInt(lkj.getCylinderPressure());
                Integer nextCp = NumberUtils.toInt(next.getCylinderPressure());
                index = i+1;
                if(lkj.getSpeed()==0 && cp<50){
                    result.add(lkj);
                    if(!(next.getSpeed()==0 && nextCp<50)){
                        break;
                    }
                }
            }
            if(result.size()>0){
                Lkj first = result.get(0);
                Lkj last = result.get(result.size()-1);
                if(DateUtils.diffDate(first.getTime(),last.getTime(),5)){
                    //记录
                    add(new PhaseAssist.Builder().setLkj(first).setLkjId(lkjId).setStart(first.getTime())
                            .setItemRecordIllegal(4).setPhaseIllegal(1)  //默认0:违章   1：正常  2：记录
                            .setEnd(last.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_103)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_103).builder());
                }
            }
        }while(index<data.size());
    }

}
