package com.zhuzhou.phase.utils;

import com.zhuzhou.entity.video.Lkj;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

public class LkjListUtil {
    /**
     * 判断一段，管压变化在170 100 +-20 范围内，并维持60秒
     */
    public static boolean changePressureLimit(List<Lkj> list,Integer maxPressure,Integer pressure){
        boolean bool = false;
        Integer endIndex = 0;
        int size = list.size();
        do{
            List<Lkj> needProcessList = new ArrayList<>();
            int index = 0;
            //找到开始位置
            long startTime = 0L;
            for(int i=endIndex;i<size;i++){
                Lkj lkj = list.get(i);
                Lkj nextLkj = list.get(i+1==size?i:i+1);
                int currentPressure = NumberUtils.toInt(lkj.getPipePressure());
                int nextPressure = NumberUtils.toInt(nextLkj.getPipePressure());
                if(currentPressure==maxPressure && nextPressure<currentPressure){
                    needProcessList.add(lkj);
                    //startTime = lkj.getTime().getTime();
                    index = i;
                    break;
                }
            }

            //添加数据,并寻找结束位置
            long endTime = 0L;
            int flag = 0;//判断for循环的if执行过没
            int targetStartPressure = 0;
            if(index>0){
                for(int i=index+1;i<size;i++){
                    needProcessList.add(list.get(i));
                    Lkj lkj = list.get(i);
                    Lkj nextLkj = list.get(i+1==size?i:i+1);
                    int currentPressure = NumberUtils.toInt(lkj.getPipePressure());
                    int nextPressure = NumberUtils.toInt(nextLkj.getPipePressure());
                    if(currentPressure<nextPressure){
                        //开始升压，则此阶段降压结束
                        endTime = nextLkj.getTime().getTime();
                        flag = 1;
                        endIndex = i+1;
                        targetStartPressure = currentPressure;
                        break;
                    }
                }
            }else{
                //开始都没有
                endIndex = list.size();
            }

            if(flag==0){
                //数据没有回升压力的迹象
                bool = false;
                endIndex = list.size();
            }else{
                //判断降压过程时间是否持续60秒以上
                if(needProcessList.size()>0){
                    //获取保压开始时间
                    for(Lkj lkj:needProcessList){
                        if(NumberUtils.toInt(lkj.getPipePressure())==targetStartPressure){
                            startTime = lkj.getTime().getTime();
                            break;
                        }
                    }
                    if(endTime-startTime>=60000){
                        //管压下降范围要大于规定值
                        int minPressure = LkjListUtil.getMinPressure(needProcessList);
                        int changePressure = maxPressure-minPressure;
                        //if(changePressure>=pressure-20 && changePressure<=pressure+20){
                        if(changePressure>=pressure-20){//下降值100以上即可
                            bool = true;
                        }
                    }
                }
            }


        }while (!bool && endIndex<list.size());
        return bool;
    }


    /**
     * 组装成需处理的数据
     * 从开始事件到结束事件，整个lkj文件肯定很多段
     * key值为下表开始-结束,value为list
     */
    public static LinkedHashMap<String, List<Lkj>> makeDate(List<Lkj> list,String lkjId, int start,int end,String beginTitle,String endTitle){
        LinkedHashMap<String,List<Lkj>> map  = new LinkedHashMap<>();
        for(int i = start;i <= end;i++) {
            //找出几段的出站进站
            List<Lkj> targetList =  new ArrayList<>();
            Lkj lkj = list.get(i);
            lkj.setRecordId(lkjId);
            String event = lkj.getEventItem();
            int k = 0;//设置i下标变化值，减少循环
            if(beginTitle.equals(event)){
                targetList.add(lkj);
                for(int j = i+1;j<list.size();j++){
                    Lkj temp = list.get(j);
                    temp.setRecordId(lkjId);
                    targetList.add(temp);
                    k = j;
                    if(endTitle.equals(temp.getEventItem())){
                        map.put(i+"-"+j,targetList);
                        break;
                    }
                }
                i = k;
            }
        }
        return map;
    }

    /**
     * 求列表里最大管压值,通常最大管压值即为列车的恒压
     */
    public static Integer getMaxPressure(List<Lkj> list){
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        List<String> collect = list.stream().map(Lkj::getPipePressure).collect(Collectors.toList());
        collect.sort(((p1, p2) -> NumberUtils.toInt(p2) - NumberUtils.toInt(p1)));
        Integer maxPressure = NumberUtils.toInt(collect.get(0));
        if((maxPressure<=610 && maxPressure>=590) || (maxPressure<=510 && maxPressure>=490)){
            return maxPressure;
        }
        return 0;
    }

    /**
     * 求列表里最小管压值
     */
    public static Integer getMinPressure(List<Lkj> list){
        List<String> collect = list.stream().map(Lkj::getPipePressure).collect(Collectors.toList());
        collect.sort(((p1, p2) -> NumberUtils.toInt(p2) - NumberUtils.toInt(p1)));
        Integer minPressure = 0;
        if(list.size()>0){
            minPressure = NumberUtils.toInt(collect.get(list.size()-1));
        }
        return minPressure;
    }

    /**
     * 判断列表里首末项点相差管压在 规定  范围内，并维持时间 规定 秒
     * result列表里数据为：刚开始降压，到压力刚回升
     */
    public static boolean judgePressureTime(List<Lkj> result,long time,Integer pressure,int limitPre){
        boolean bool = false;
        Lkj first = result.get(0);
        Lkj last = result.get(result.size()-1);
        int firstPressure = NumberUtils.toInt(first.getPipePressure());
        int lastPressure = NumberUtils.toInt(last.getPipePressure());
        int changePressure = firstPressure-lastPressure;
        if(Math.abs(last.getTime().getTime()-first.getTime().getTime())>=time*1000&&
        changePressure>=pressure-limitPre && changePressure<=pressure+limitPre){
            bool = true;
        }
        return bool;
    }


    /**
     * 监控非调车
     * 的结果列表
     */
    public static List<Lkj> getControlData(List<Lkj> list,int start, int end){
        List<Lkj> result = new ArrayList<>();
        //筛选数据
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            if(StringUtils.isBlank(lkj.getPipePressure())){
                continue;
            }
            result.add(lkj);
        }
        return result;
    }

    /**
     * 获取LinkedHashMap中末尾元素
     */
    public static  <K, V> Map.Entry<K, V> getTail(LinkedHashMap<K, V> map) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        return tail;
    }

    /**
     * 去掉LKJ列表里，管压缸压为空的数据
     */
    public static List<Lkj> removeNull(List<Lkj> list){
        return list.stream().filter(dto->StringUtils.isNotBlank(dto.getPipePressure())&&StringUtils.isNotBlank(dto.getCylinderPressure())).collect(Collectors.toList());
    }
}
