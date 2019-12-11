package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  姓名：刘欣武
 *  时间：2019-10-21
 *  项点名称：回手柄违规
 *  项点描述：
 *  项点开发：只分析内燃机车，判断机车转速在规定值以上时（默认600rpm），手柄回“零位”；则视为违章。
 *  分析结果：
 */
@Service
public class Item77 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if(HeadUtils.dieselLocomotive(head)){
            //判断机车转速在规定值以上时（默认600rpm）
            for(int i=start;i<=end;i++){
                Lkj lkj = list.get(i);
                if(NumberUtils.toInt(lkj.getSpeedElectricity())>=600){
                    //手柄必须在零位,否则违章
                    if(!"零位".equals(lkj.getZeroBit())){
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I25_77)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_77).builder());
                    }
                }
            }
        }
    }
}
