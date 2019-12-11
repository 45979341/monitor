package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-24
 *  项点名称：管压为0
 *  项点描述：1、运行途中，列车速度非零状态下；
 *          2、列车管压力小于规定值（默认200KPA）；
 *          3、判断列车管压力；
 *          4、排除所有由于紧急制动导致的管压为0；缸压小于20kpa
 *  项点开发：监控状态下，速度不为0，缸压小于20kpa（排除紧急制动情况），管压小于200kpa，则识别为项点。
 *  分析结果：
 */
@Service
public class Item37 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        boolean bool = false;//为了将连续一起该记录的整合成一条记录
        int flag = 0;//事件标志
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            //监控状态下
            Integer speed = lkj.getSpeed();
            int cylinderPressure = NumberUtils.toInt(lkj.getCylinderPressure());
            int pipePressure = NumberUtils.toInt(lkj.getPipePressure());
            //恒压下
            if((pipePressure<=510 && pipePressure>=490) || (pipePressure<=610 && pipePressure>=590)){
               if("紧急制动".equals(lkj.getEventItem())){
                   flag = 1;
               }
            }
            if(speed>0 && cylinderPressure<20 && pipePressure<200 && flag==0){
                //则记录
                if(!bool){
                    add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                            .setItemRecordIllegal(4).setPhaseIllegal(1)
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I11_37)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_37).builder());
                    bool = true;
                }
            }else{
                bool = false;
            }

            //再次恒压下，回归初始状态
            if((pipePressure<=510 && pipePressure>=490) || (pipePressure<=610 && pipePressure>=590)){
                if(!"紧急制动".equals(lkj.getEventItem())){
                    flag = 0;
                }
            }
        }
    }
}
