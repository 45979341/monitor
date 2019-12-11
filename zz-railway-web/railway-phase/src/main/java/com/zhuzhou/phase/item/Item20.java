package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  姓名：刘欣武
 *  时间：2019-10-17
 *  项点名称：中间站本务机调车结束未简略试验
 *  项点描述：1、列车中间站停车，由通常工作状态转入调车状态后，再由调车状态恢复通常工作状态并开车时，必须进行制动机简略试验；
 *          2、电力机车按照均衡风缸判断，内燃机车按照列车管压力判断；
 *          3、判断标准参照始发站制动机试验判断标准（默认客车170KPA，货车100KPA，保压60秒）；
 *  项点开发：只判断站内停车-站内开车之间的退出调车-站内开车数据段
 *  分析结果：
 */
@Service
public class Item20 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //初步筛选数据
        LinkedHashMap<String, List<Lkj>> map1 = LkjListUtil.makeDate(list, lkjId, start, end,"站内停车", "站内开车");
        //组装数据
        //LinkedHashMap<String, List<Lkj>> map = getProcessMap(list,position);
        //分析数据
        if(head.getTrainType().contains("客")){
            processResult(map1,170,lkjId);
        }else{
            processResult(map1,100,lkjId);
        }
    }

    public void processResult(LinkedHashMap<String, List<Lkj>> map,Integer pressure,String lkjId){

        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> data = entry.getValue();
            //再次筛选数据
            List<Lkj> list = new ArrayList<>();
            for(int i=0;i<data.size();i++){
                Lkj lkj = data.get(i);
                if("退出调车".equals(lkj.getEventItem())){
                    for(int j=i;j<data.size();j++){
                        list.add(data.get(j));
                    }
                    break;
                }
            }
            if(list.size()>0){
                //客车170KPA，货车100KPA，保压60秒
                Integer maxPressure = LkjListUtil.getMaxPressure(list);
                if(!LkjListUtil.changePressureLimit(list,maxPressure,pressure)){
                    //违章
                    Lkj lkj = list.get(0);
                    Lkj endLkj = list.get(list.size()-1);
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                            .setEnd(endLkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_20)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_20).builder());
                }
            }
        }
    }

    public LinkedHashMap<String,List<Lkj>> getProcessMap(List<Lkj> list,int position){
        LinkedHashMap<String,List<Lkj>> map = new LinkedHashMap<>();
        List<Integer> endList = new ArrayList<>();
        for(int i=position;i<list.size();i++){
            Lkj lkj = list.get(i);
            String event = lkj.getEventItem();
            if("站内开车".equals(event)){
                endList.add(i);
            }
        }
        //找出多个需要分析的数据段
        endList.stream().forEach(x->{
            List<Lkj> processList = new ArrayList<>();
            for(int i=x;;i--){
                Lkj cur = list.get(i);
                processList.add(cur);
                if("退出调车".equals(cur.getEventItem())){
                    break;
                }
            }
            Collections.reverse(processList);
            map.put(x+"",processList);
        });
        return map;
    }
}
