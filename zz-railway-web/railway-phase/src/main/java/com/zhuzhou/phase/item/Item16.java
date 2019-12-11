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

import java.util.*;


/**
* 姓名:         刘欣武
* 创建时间:     2019/10/16 17:14
* 项点名称：    始发站制动机未试验
* 项点描述：     1、乘务员始发站开车前必须进行制动机试验；
 *              2、机车列车管压力下降规定值（默认货车100KPA，客车170KPA，上下浮动20KPA，内燃机车以列车管压力判断
 *              3、管压减压值维持时间不低于规定值（默认60秒）；降级运行-降级开车范围，否则违章
* 项点开发：    1、退出出段范围内
 *              2、开车对标前的一个（降级运行-降级开车）范围内
 *              3、管压有从恒压下降并回升范围内（可能出现多段）
 *              4、3范围内，下降的管压值在（80，120）范围内
 *              5、3范围内，从开始最低压到最后的列表里的最低压，它俩时间间隔必须在60秒以上，才算正常，否则都是违章
* 中标结果：    没中则违章
*/
@Service
public class Item16 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //组装数据
        List<Lkj> datas = makeData(list,lkjId,start,end);
        //过滤空值
        List<Lkj> collect = LkjListUtil.removeNull(datas);
        Integer maxPressure = LkjListUtil.getMaxPressure(collect);
        boolean bool = analyseDataList(collect,maxPressure,100);
        //判断违章
        if(!bool){
            Lkj lkj = datas.get(0);
            add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                    .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_16)
                    .setPhaseEnum(Phase.PhaseEnum.LKJ_16).builder());
        }
    }

    //找出往上最接近开车对标的一段运行-开车的list
    public List<Lkj> makeData(List<Lkj> list,String lkjId, int start,int end){
        List<Lkj> data = new ArrayList<>();
        int k = 0;
        List<Integer> begins = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        for(int i = start;i <= end;i++) {
            Lkj lkj = list.get(i);
            lkj.setRecordId(lkjId);
            String event = lkj.getEventItem();
            if("降级运行".equals(event)){
               begins.add(i);
            }
            if("降级开车".equals(event)){
                ends.add(i);
            }
            if("开车对标".equals(event)){
                k = i;
            }
        }
        int count = 0;
        for(int i=0;i<ends.size();i++){
            Integer val = ends.get(i);
            if(k-val>0){
                count++;
            }
        }
        if(count==0){
            //全在开车对标以后，则
            return data;
        }
        //正确下标结束值
        Integer end2 = ends.get(count-1);

        int beginCount=0;
        for(int i=0;i<begins.size();i++){//运行与开车不是成对出现。。。
            Integer val = begins.get(i);
            if(k-val>0 && val<end2){
                beginCount++;
            }
        }
        if(beginCount==0){
            //全在开车对标以后，则
            return data;
        }
        //正确下标开始值
        Integer begin = begins.get(beginCount-1);

        for(int i=begin;i<=end2;i++){
            data.add(list.get(i));
        }
        return data;
    }

    public boolean analyseDataList(List<Lkj> targetList,Integer maxPressure,Integer pressure){
        boolean bool = false;
        int index = 0;
        int size = targetList.size();
        do{
            List<Lkj> needProcessList = new ArrayList<>();
            Date endDate = null;
            for(int i=index;i<size;i++){
                index = i+1;
                Lkj lkj = targetList.get(i);
                Lkj nextLkj = targetList.get(i+1==size?i:i+1);
                int currentPressure = NumberUtils.toInt(lkj.getPipePressure());
                int nextPressure = NumberUtils.toInt(nextLkj.getPipePressure());
                if(currentPressure==maxPressure && currentPressure>nextPressure){//从定压开始下降
                    needProcessList.add(lkj);
                    for(int j=index;i<size;j++){
                        index = j+1;
                        Lkj temp = targetList.get(j);
                        needProcessList.add(temp);
                        Lkj nextTemp = targetList.get(j+1==size?j:j+1);
                        Integer tempPressure = NumberUtils.toInt(temp.getPipePressure());
                        Integer nextTempPressure = NumberUtils.toInt(nextTemp.getPipePressure());
                        if(tempPressure<nextTempPressure){//管压回升
                            endDate = nextTemp.getTime();
                            break;
                        }
                    }
                    break;
                }
            }
            //分析此小段是否符合条件
            if(needProcessList.size()>0){
                //bool = LkjListUtil.judgePressureTime(needProcessList,60,pressure,20);
                //管压减低100，上下20
                bool = judgeProcessData(needProcessList,endDate);

            }
        }while(!bool && index<size);
        return bool;
    }

    private boolean judgeProcessData(List<Lkj> result,Date endDate){
        boolean bool = false;
        Lkj first = result.get(0);
        Lkj last = result.get(result.size()-1);
        int minPre = NumberUtils.toInt(last.getPipePressure());
        int firstPressure = NumberUtils.toInt(first.getPipePressure());
        int lastPressure = NumberUtils.toInt(last.getPipePressure());
        int changePressure = firstPressure-lastPressure;
        Date beginDate = null;
        if(changePressure>=100-20 && changePressure<=100+20){
        //if(changePressure>=100){//下降100以上
            //保持最低压至少60秒
            for(int i=0;i<result.size();i++){
                Lkj lkj = result.get(i);
                if(NumberUtils.toInt(lkj.getPipePressure())==minPre){
                    //稳压开始
                    beginDate = lkj.getTime();
                    break;
                }
            }
        }
        if(beginDate!=null){
            if(endDate.getTime()-beginDate.getTime()>=60*1000){
                bool  = true;
            }
        }

        return bool;
    }


}