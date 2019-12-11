package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-22
 *  项点名称：充风不足开车
 *  项点描述：1、列车开车后，机车速度在规定值内（默认5KM/H），如果列车管压力低于定压规定值（默认30KPA），则记录一次；
 *            2、特殊站可充分不足开车，需在参数中设置；
 *            3、调车状态不分析；
 *  项点开发：监控非调车状态,速度由0到5范围内,最后速度的管压小于30，记录一次
 *  分析结果：
 */
@Service
public class Item104 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //监控非调车状态数据,结果集合
        List<Lkj> data = LkjListUtil.getControlData(list,start,end);
        //分析
        analyseData(data,lkjId);

    }

    public void analyseData(List<Lkj> list,String lkjId){
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
                    if(next.getSpeed()>5){
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
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_104)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_104).builder());
                }
            }

        }while(index<size);

    }

}
