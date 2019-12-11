package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  姓名：刘欣武
 *  时间：2019-10-17
 *  项点名称：单机未前移
 *  项点描述：
 *  项点开发：单机机车，区间内停车，信号为“绿灯”或“绿黄灯”或“黄灯”时，如果单机停车规定时间内（默认5分钟），未前行规定值以上（默认15米），则记录违章一次；
 *  分析结果：
 */


/**
 * 1 进站 出战
 * 2 靠近进站的区间停车
 * 3靠近出战的区间开车
 * 4 时间小于五分钟距离小于15米才违章
 * 5 时间大于五分钟直接违章
 */
@Service
public class Item80 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //只分析单机
        if(HeadUtils.startWith5Length5(head)){
            LinkedHashMap<String, List<Lkj>> maps = LkjListUtil.makeDate(list, lkjId, start,end, "进站", "出站");
            for(Map.Entry<String, List<Lkj>> entry:maps.entrySet()){
                List<Lkj> values = entry.getValue();
//                List<Lkj> processData = getProcessData(values);
                //分析
//                if(!analyseData(processData)){
//                    Lkj lkj = values.get(0);
//                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
//                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I26_80)
//                            .setPhaseEnum(Phase.PhaseEnum.LKJ_80).builder());
//                    System.out.println("违章");
//                }
                analyseList(values,lkjId);
            }
        }
    }

    public void analyseList(List<Lkj> list,String lkjId){
        //找出最靠近进站的区间停车
        Lkj start = null;
        Lkj end = null;
        int flag1 = 0;//条件标志
        int flag2 = 0;
        for(int i=0;i<list.size();i++){
            Lkj lkj = list.get(i);
            String event = lkj.getEventItem();
            if("区间停车".equals(event) && flag1==0){
                String signal = lkj.getSignals();
                if("绿灯".equals(signal)||"黄灯".equals(signal)||"绿黄灯".equals(signal)){
                    flag1 = 1;
                    start = lkj;
                }
            }
            if("区间开车".equals(event)){
                String signal = lkj.getSignals();
                if("绿灯".equals(signal)||"黄灯".equals(signal)||"绿黄灯".equals(signal)){
                    flag2 = 1;//必须含有
                    end = lkj;
                }
            }
        }
        if(flag1>0 && flag2>0){
            //才判断可不可能违章
            //时间小于五分钟才判断
            if(end.getTime().getTime()-start.getTime().getTime()<=5*60*1000){
                //公里标,未前行15米以上
                if((end.getKiloMeter()-start.getKiloMeter())*1000<15){
                    //违章
                    add(new PhaseAssist.Builder().setLkj(start).setLkjId(lkjId).setStart(start.getTime())
                            .setEnd(end.getTime()).setItemEnum(ItemRecord.ItemEnum.I26_80)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_80).builder());
                }
            }else{
                //大于5分钟直接违章
                add(new PhaseAssist.Builder().setLkj(start).setLkjId(lkjId).setStart(start.getTime())
                        .setEnd(end.getTime()).setItemEnum(ItemRecord.ItemEnum.I26_80)
                        .setPhaseEnum(Phase.PhaseEnum.LKJ_80).builder());
            }
        }

    }

    //截取需要分析的数据
    public List<Lkj> getProcessData(List<Lkj> list){
        List<Lkj> needProcessList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Lkj lkj = list.get(i);
            String signal = lkj.getSignals();
            if("绿灯".equals(signal)||"黄灯".equals(signal)||"绿黄灯".equals(signal)){
                needProcessList.add(lkj);
            }
        }
        return needProcessList;
    }

    //分析
    public boolean analyseData(List<Lkj> list){
        boolean bool = true;//默认不违规
        if(list.size()>0){
            Lkj first = list.get(0);
            Lkj last = list.get(list.size()-1);
            //5分钟内
            if(last.getTime().getTime()-first.getTime().getTime()<=5*60*1000){
                //未前行15米以上
                if(last.getDistance()-first.getDistance()<15){
                    bool = false;
                }
            }
        }

        return bool;

    }
}


