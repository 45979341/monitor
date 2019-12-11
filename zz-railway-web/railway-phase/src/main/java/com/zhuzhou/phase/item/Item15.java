package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  姓名：刘欣武
 *  时间：2019-10-15
 *  项点名称：贯通试验违标
 *  项点描述：
 *  项点开发：（监控开车状态下，起始车站到下一个车站，速度40km/h以上，管压下降40kpa以上，速度下降5km/h以上，每一次开车都需要判断）
 *              出站到进站之间，速度40公里以上。出现定压管压下降40以上又恢复，速度下降大于等于5公里为正常
 *  中标结果：没分析出来则通过，做了才判断对不对。
 */
@Service
public class Item15 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //(单机不分析)
        if(HeadUtils.startWith5Length5(head)||HeadUtils.bu(head)){
            return;
        }
        //组装数据
        LinkedHashMap<String,List<Lkj>> map = LkjListUtil.makeDate(list, lkjId, start,end,"出站","进站");
        //分析数据
        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> value = entry.getValue();
            if(!analyseDataList(value)){
                //违章
                Lkj lkj = value.get(0);
                Lkj end2 = value.get(value.size()-1);
                add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkj.getRecordId()).setStart(lkj.getTime())
                        .setEnd(end2.getTime()).setItemEnum(ItemRecord.ItemEnum.I6_15)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_15).builder());
            }
        }
    }

    public boolean analyseDataList(List<Lkj> list){
        boolean bool = true;//默认不违章
        int index = 0;
        int size = list.size();
        //定压
        int maxPressure = LkjListUtil.getMaxPressure(list);
        do{
            List<Lkj> result = new ArrayList<>();
            for(int i=index;i<size;i++){
                index = i+1;
                Lkj current = list.get(i);
                int currentPressure = NumberUtils.toInt(current.getPipePressure());
                Lkj next = list.get(i+1==size?i:i+1);
                int nextPressure = NumberUtils.toInt(next.getPipePressure());
                if(current.getSpeed()>40 && currentPressure==maxPressure &&currentPressure>nextPressure){//管压下降
                    result.add(current);//添加当前
                    for(int j=index;j<size;j++){
                        index = j+1;
                        Lkj temp = list.get(j);
                        result.add(temp);
                        Integer tempPressure = NumberUtils.toInt(temp.getPipePressure());
                        //当管压与最开始的压力相同时，则回缓完成
                        if(temp.getPipePressure().equals(currentPressure)||tempPressure>currentPressure){
                            break;
                        }
                    }
                    break;
                }
            }
            //判断一下该段是否符合条件
            if(result.size()>0){
                List<String> pressureCollect = result.stream().map(dto -> dto.getPipePressure()).collect(Collectors.toList());
                //管压变化必须大于40以上
                if(NumberUtils.toInt(Collections.max(pressureCollect))-NumberUtils.toInt(Collections.min(pressureCollect))>40){
                    //速度变化大于5
                    List<Integer> speedCollect = result.stream().map(dto -> dto.getSpeed()).collect(Collectors.toList());
                    if(Collections.max(speedCollect)-Collections.min(speedCollect)<5){
                        bool = false;
                    }
                }
            }
        }while(bool && index<size);
        return bool;
    }
}
