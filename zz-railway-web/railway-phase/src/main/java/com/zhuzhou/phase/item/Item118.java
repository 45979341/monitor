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
import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-18
 *  项点名称：补机未及时回手柄
 *  项点描述：1、非补机不分析；
 *          2、列车机车速度大于零时，列车定压管压力减压规定值（默认50KPA），在规定时间（默认5秒）内手柄未在零位，则违章操纵；
 *  项点开发：
 *  分析结果：
 */
@Service
public class Item118 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //只分析特定车型
        if(head.getTrainType().contains("补")){
            //分析
            processData( list,HeadUtils.constPressure(head),lkjId,start,end);
        }

    }

    public void processData(List<Lkj> list,int pressure,String lkjId,int start,int end){
        int index = start;
        int size = list.size();
        int endFinal = end<=size?end:size;
        do{
            List<Lkj> result = new ArrayList<>();
            int count = 0;
            for(int i=index;i<endFinal;i++){
                Lkj lkj = list.get(i);
                Lkj nextLkj = list.get(i+1);
                Integer nextPressure = Integer.parseInt(nextLkj.getPipePressure());
                Integer curPressure = Integer.parseInt(lkj.getPipePressure());
                if(lkj.getSpeed()>0 && curPressure==pressure && nextPressure<pressure && count==0){
                    result.add(lkj);
                    count++;
                }
                if(count==1){
                    result.add(lkj);
                    if(curPressure<nextPressure){
                        //压力回升，跳出循环,记录指针
                        index = i;
                        break;
                    }
                }
                //记录指针
                index = i;
            }
            if(result.size()>0){
                if(LkjListUtil.judgePressureTime(result,5,50,10)){
                    Lkj last = result.get(result.size()-1);
                    if(!"零位".equals(last.getZeroBit())){
                        //记录
                        add(new PhaseAssist.Builder().setLkj(last).setLkjId(lkjId).setStart(last.getTime())
                                .setItemRecordIllegal(4).setPhaseIllegal(1)
                                .setEnd(last.getTime()).setItemEnum(ItemRecord.ItemEnum.I32_118)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_118).builder());
                    }
                }
            }

        }while(index<endFinal);
    }
}
