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

import java.awt.*;
import java.util.List;

/**
 * @author xiechonghu
 * @date 2019/10/21 9:26
 * @description: 项点名称：库内无柴油机调速试验
 * 库内无以下记录：只分析内燃车(库内 退出出段)
 * 1、机车柴油机转速必须大于设定值以上（600RPM）；
 * 2、制动缸压力大于等于设定值（默认280KPA）;
 * 3、机车工况在零位；
 * 4、ND机型排除不分析，电力机车不分析；
 * 5、分析范围为库内作业（乘务员上车至闸楼处）；
 * 6、机车速度为零的情况；
 */
@Service
public class Item7 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        if (!HeadUtils.dieselLocomotive(head) || HeadUtils.motorName(head, "ND")) {
            return;
        }
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            int rotate = NumberUtils.toInt(lkj.getSpeedElectricity());
            int cylinder = NumberUtils.toInt(lkj.getCylinderPressure());
            int speed = lkj.getSpeed();
            if (rotate > 600 && cylinder >= 280 && speed == 0) {
                do {
                    i++;
                    Lkj cur = list.get(i);
                    int curRotate = NumberUtils.toInt(cur.getSpeedElectricity());
                    int curCylinder = NumberUtils.toInt(cur.getCylinderPressure());
                    int curSpeed = cur.getSpeed();
                    if (curRotate <= 600 || curCylinder < 280 || curSpeed > 0 || i == end) {
                        // 违规记录
                        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                                .setEnd(list.get(i - 1).getTime()).setItemEnum(ItemRecord.ItemEnum.I2_7)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_7).builder());
                        break;
                    }
                } while (i < end);
            }
        }
    }
}
