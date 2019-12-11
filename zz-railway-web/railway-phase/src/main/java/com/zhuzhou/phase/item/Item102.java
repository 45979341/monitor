package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-21
 *  项点名称：库内未检查缓解状态
 *  项点描述：库内无以下记录：
 *          1、机车库内停车状态下；
 *          2、机车制动缸压力值保持在规定值以下（默认30KPA）以下，维持规定时间（默认30秒）以上
 *  项点开发：
 *  分析结果：
 */
@Service
public class Item102 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //有效数据(退出出段,lkj文件只有一个)
        List<Lkj> datas = makeData(list, start,end);
        //分析数据
        analyseDatas(datas, lkjId);
    }

    //截取有效数据
    public List<Lkj> makeData(List<Lkj> list, int start,int end){
        List<Lkj> data = new ArrayList<>();
        int flag = 0;
        for(int i=start;i<end;i++){
            Lkj lkj = list.get(i);
            String event = lkj.getEventItem();
            //库内
            if("退出出段".equals(event)){
                flag = 1;
                break;
            }
            //停车
            if(lkj.getSpeed()==0){
                data.add(lkj);
            }
        }
        if(flag == 0){
            data.clear();
        }
        return data;
    }

    public void analyseDatas(List<Lkj> datas, String lkjId){
        int index = 0;
        do{
            List<Lkj> process = new ArrayList<>();//需要判断的列表数据
            if(datas!=null && datas.size()>0){
                for(int i=index;i<datas.size();i++){
                    Lkj cur = datas.get(i);
                    Lkj next = null;
                    if(i==datas.size()-1){
                        next = cur;
                    }else{
                        next = datas.get(i+1);
                    }
                    if(NumberUtils.toInt(cur.getCylinderPressure())<=30){
                        process.add(cur);
                        if(NumberUtils.toInt(next.getCylinderPressure())>30){
                            index = i+1;
                            break;
                        }
                    }
                    index = i+1;
                }
                //判断本段是否符合条件
                if(process.size()>0){
                    Lkj first = datas.get(0);
                    Lkj last = datas.get(process.size()-1);
                    if(DateUtils.diffDate2(first.getTime(),last.getTime(),30)){
                        //记录
                        add(new PhaseAssist.Builder().setLkj(first).setLkjId(lkjId).setStart(first.getTime())
                                .setItemRecordIllegal(4).setPhaseIllegal(1)  //默认0:违章   1：正常  2：记录
                                .setEnd(last.getTime()).setItemEnum(ItemRecord.ItemEnum.I31_102)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_102).builder());
                    }
                }
            }
        }while(index<datas.size());


    }

}
