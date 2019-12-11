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

import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-21
 * @Description: 库内无过量减压
 * 1、以定压为基准，过量减压设定值（默认240KPA，上下浮动20KPA）；
 * 2、特殊车型可在参数设置表中添加，以便排除不做分析；(和谐机车，复兴以外不分析)
 * 3、分析范围为库内作业（乘务员上车至闸楼处）；
 * 4、机车速度为零的情况；
 */
@Service
public class Item3 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        Integer constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        //特殊车型除外，增加机车车型配置（和谐，复兴除外）
        if (head.getMotorType() >= 231 && head.getMotorType() <= 239) {
            return;
        } else {
            //库内范围
            for (int i = start; i < end; i++) {

                Lkj lkj = list.get(i);
                lkj.setRecordId(lkjId);
                int speed = lkj.getSpeed();
                String pipePressure = lkj.getPipePressure();

                //速度不为0
                if (speed == 0 && StringUtils.isNotEmpty(pipePressure)) {
                    if (NumberUtils.toInt(lkj.getPipePressure()) >= 590 && NumberUtils.toInt(lkj.getPipePressure()) <= 610) {
                        int t = i + 1;
                        Lkj temp = lkj;
                        Lkj cur = list.get(t);
                        //当前处于定压并且后一个不为定压才能开始循环
                        while (NumberUtils.toInt(temp.getPipePressure()) - NumberUtils.toInt(cur.getPipePressure()) > 0 && t < end) {
                            if (NumberUtils.toInt(temp.getPipePressure()) - NumberUtils.toInt(cur.getPipePressure()) >= 230
                                    && NumberUtils.toInt(temp.getPipePressure()) - NumberUtils.toInt(cur.getPipePressure()) <= 290) {
                                return;
                            }
                            cur = list.get(++t);
                        }
                        i = t + 1;
                    }
                }
            }
        }

        Lkj lkj1 = list.get(end);
        add(new PhaseAssist.Builder().setLkj(lkj1).setLkjId(lkjId).setStart(lkj1.getTime())
                .setEnd(lkj1.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_3)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_3).builder());
    }
}
