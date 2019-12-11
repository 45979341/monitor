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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  姓名：刘欣武
 *  时间：2019-10-17
 *  项点名称：紧急制动后开车未试闸
 *  项点描述：1、列车紧急制动或管压人为撂闸至零后，开车前必须进行制动机试验（默认50KPA,上下浮动10KPA）；
 *          2 、电力机车按均衡风缸判断，内燃机车按列车管压力判断；
 *          3、单机不分析；（单机除外）；监控状态下，管压变0后，开车前缓解到定压再减压50（+-10）
 *  项点开发：降压必须50以上否则判违章
 *  分析结果：
 */
@Service
public class Item21 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //单机不分析
        if(HeadUtils.startWith5Length5(head)){
            return;
        }
        //获取数据
        LinkedHashMap<String,List<Lkj>> map = getProcessData(list);

        //processData(map,lkjId);

        //analyseData(map,lkjId);
        analyseData2(map,lkjId);
    }

    public LinkedHashMap<String,List<Lkj>> getProcessData(List<Lkj> list){
        LinkedHashMap<String,List<Lkj>> map = new LinkedHashMap<>();
        //监控
        //监控状态 1：监控，2：降级
        int monitorStatus = 0;
        int index = 0;
        do{
            List<Lkj> resultList = new ArrayList<>();
            String key = "";
            int flag = 0;
            for (int i = index; i < list.size(); i++) {
                index = i+1;
                Lkj lkj = list.get(i);
                String eventItem = lkj.getEventItem();
                if ("开车对标".equals(eventItem)) {
                    monitorStatus = 1;
                    key = i+"";
                    flag = 1;//保证数据从开车对标开始收集
                } else if (eventItem.contains("降级")) {
                    monitorStatus = 2;
                }
                if(monitorStatus==1){
                    resultList.add(lkj);
                }else if(monitorStatus==2 && flag==1){
                    resultList.add(lkj);//把这个降级依然添加list里去
                    map.put(key,resultList);
                    break;
                }
            }
        }while(index<list.size());
        return map;
    }


    /**
     * 判断规则：
     *车管从600/500kpa 5秒内下降到0以后，又到600/500，开车前有列车管下降50以上的记录，否则是违章。
     */
    public void analyseData2(LinkedHashMap<String,List<Lkj>> map,String lkjId){

        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> list = entry.getValue();
            //找到紧急制动
            int size = list.size();
            int index = 0;
            do{
                for(int i=index;i<size;i++){
                    index = i+1;
                    Lkj lkj = list.get(i);
                    if("紧急制动".equals(lkj.getEventItem())){
                        //条件1：恒压-0-恒压
                        long startTime = lkj.getTime().getTime();
                        int pre = NumberUtils.toInt(lkj.getPipePressure());
                        //从定压开始下降
                        if((pre>=590&&pre<=610) || (pre>=490&&pre<=510)){//不是从恒压下降，则过滤，不判断。
                            int flag1 = 0;
                            int flag2 = 0;
                            for(int j=index;j<size;j++){
                                index = j+1;
                                Lkj temp = list.get(j);
                                Lkj next = list.get(index==size?j:index);
                                int tempPressure = NumberUtils.toInt(temp.getPipePressure());
                                if(tempPressure==0 && flag1==0){
                                    long endTime = temp.getTime().getTime();
                                    //条件2：5秒之内必须管压到0，否则直接过滤。不判断
                                    if(endTime-startTime<=5*1000){
                                        flag1 = 1;
                                        continue;
                                    }
                                }
                                if(flag1==1){
                                    if(tempPressure==pre && flag2==0){//已经上升到恒压
                                        flag2 = 1;
                                        continue;
                                    }
                                    if(flag2==1){//条件3：需要找到次记录下，后面有下降50管压的数据，否则就是违章
                                        if("紧急制动".equals(next.getEventItem())){
                                            //System.out.println("违章1");//没有下降50的情况发生，则违章
                                            breakRules(lkj,lkjId);
                                            break;//不影响下一次循环判断
                                        }
                                        if(tempPressure==pre){
                                            //管压一直恒定，没减少
                                            continue;
                                        }
                                        //只有一个紧急制动的数据范围，全循环完毕，以然没找到下降50记录，则
                                        if(index==size){
                                            //System.out.println("违章2");
                                            breakRules(lkj,lkjId);
                                        }
                                        //只要经过下面的判断，则为管压下降了一个幅度，则本次循环结束
                                        if(tempPressure>=pre-50 && tempPressure<pre){//下降少于50或者没下降都是违章
                                            //System.out.println("违章3");
                                            breakRules(lkj,lkjId);
                                            break;
                                        }else{
                                            break;//下降满足情况，则不违章
                                        }
                                    }
                                }
                            }
                            break;//跳出中循环，才能重新从index值开始循环
                        }
                    }
                }
            }while(index<size);
        }
    }

    private void breakRules(Lkj lkj,String lkjId){
        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_21)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_21).builder());
    }

    public void analyseData(LinkedHashMap<String,List<Lkj>> map,String lkjId){
        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> list = entry.getValue();
            int pressure = LkjListUtil.getMaxPressure(list);
            int size = list.size();
            int index = 0;
            int minPressure = 0;
            Lkj minLkj = null;
            do{
                int flag = 0;
                int count = 0;
                for(int i=index;i<size;i++){
                    index = i+1;
                    Lkj currentLkj = list.get(i);
                    Lkj nextLkj = list.get(index==size?i:index);
                    int currentPressure = NumberUtils.toInt(currentLkj.getPipePressure());
                    int nextPressure = NumberUtils.toInt(nextLkj.getPipePressure());
                    if(currentPressure==0 && currentPressure<nextPressure){//管压从0开始
                        flag = 1;
                        continue;
                    }
                    if(currentPressure<=nextPressure && flag==1){//管压持续上升
                        continue;
                    }
                    if(currentPressure==pressure && currentPressure>nextPressure && flag==1){//管压达到定压，并下降
                        count= 1;
                        continue;
                    }
                    if(currentPressure>=nextPressure && flag==1 && count==1){
                        continue;
                    }
                    if(currentPressure<nextPressure && flag==1 && count==1 ){//管压开始回升，则达到最低值
                        minPressure = currentPressure;
                        minLkj = currentLkj;
                        break;
                    }
                }
                //判断
                if(pressure-minPressure<50){
                    add(new PhaseAssist.Builder().setLkj(minLkj).setLkjId(lkjId).setStart(minLkj.getTime())
                            .setEnd(minLkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_21)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_21).builder());
                }

            }while(index<size);


        }
    }


    public void processData(LinkedHashMap<String,List<Lkj>> map, String lkjId){
        for(Map.Entry<String,List<Lkj>> entry:map.entrySet()){
            List<Lkj> list = entry.getValue();
            int index = 0;
            while(index<list.size()){
                List<Lkj> needProcess = new ArrayList<>();
                Integer pressure = 0;
                int count = 0;
                for(int i=index;;i++){
                    Lkj currentLkj = list.get(i);
                    Lkj nextLkj = list.get(i+1);
                    Integer currentPressure = Integer.parseInt(currentLkj.getPipePressure());
                    Integer nextPressure = Integer.parseInt(nextLkj.getPipePressure());
                    if(currentLkj.getSpeed()==0 && nextLkj.getSpeed()==0){
                        if(currentPressure<nextPressure && currentPressure==0){
                            //速度为0情况下，管压从0开始上升
                            needProcess.add(currentLkj);
                        }
                        if(currentPressure<=nextPressure){
                            //速度为0情况下，管压开始上升
                            needProcess.add(currentLkj);
                        }
                        if(currentPressure>nextPressure && count==0){
                            pressure = currentPressure;//赋值一次
                            count++;
                        }
                        if(currentPressure>nextPressure){
                            needProcess.add(currentLkj);
                        }
                    }else if(currentLkj.getSpeed()>0){
                        index = i+1;
                        break;
                    }
                }
                if(needProcess.size()>0){
                    Lkj lkj = needProcess.get(0);
                    Lkj lastLkj = needProcess.get(needProcess.size()-1);
                    //分析是否违规
                    Integer minPressure = LkjListUtil.getMinPressure(needProcess);
                    Integer changePressure = pressure-minPressure;
                    if(!(changePressure>=40 && changePressure<=60)){
                        //违规
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lastLkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_21)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_21).builder());
                    }
                }
            }
        }
    }
}