package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.garageutils.GarageUtils;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-21
 * @Description: 库内无过充试验
 * 1、机车列车管压力超过定压（500，600）规定值或以上（默认30KPA）；
 * 2、特殊车型可在参数设置表中添加，以便排除不做分析；
 * 3、分析范围为库内作业（乘务员上车至闸楼处）；
 * 4、机车速度为零的情况；
 * 5、特殊车型除外，增加机车车型配置（和谐，复兴除外）
 */
@Service
public class Item6 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();
        List<Lkj> list1 = new ArrayList<>();

        //特殊车型除外，增加机车车型配置（和谐，复兴除外）
        if (head.getMotorType() >= 231 && head.getMotorType() <= 239) {
            return;
        } else {
            //分析范围为库内作业
            for (int i = start; i < end; i++) {
                Lkj lkj = list.get(i);
                lkj.setRecordId(lkjId);
                int speed = lkj.getSpeed();
                String pipePressure = lkj.getPipePressure();

                //速度为0
                if (speed == 0 && StringUtils.isNotEmpty(pipePressure)) {
                    //机车列车管压力超过定压规定值
                    if (NumberUtils.toInt(pipePressure) >= constPipe + 30) {
                        list1.add(lkj);
                    } else {
                        if (list1.size() >= 1) {
                            Lkj lkj1 = list1.get(0);
                            add(new PhaseAssist.Builder().setLkj(lkj1).setLkjId(lkjId).setStart(lkj1.getTime())
                                    .setEnd(lkj1.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_6)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_6).builder());
                            list1 = new ArrayList<>();
                        }
                    }
                }
            }
        }
    }
}
