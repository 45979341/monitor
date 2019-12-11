package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
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
 * @Date 2019-10-16
 * @Description: 库内最大减压
 * 1、以定压为基准，500KPA时最大减压规定值（默认参数140KPA(浮动10KPA)）；
 * 2、以定压为基准，600KPA时最大减压规定值（默认参数170KPA(浮动10KPA)）；
 * 3、最加减压时需恒定5秒钟（无参数）;
 * 4、分析范围为库内作业（乘务员上车至闸楼处）；
 * 5、机车速度为零的情况。货车500（+-10），客车600（+-10）.未分析出为违章（退出出段时间点）
 */
@Service
public class Item2 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        int constPipe = GarageUtils.getConstPipe(head).getConstPipe();   //定压

        for (int i = start;i < end ; i++) {    //库内
            Lkj lkj = list.get(i);
            lkj.setRecordId(lkjId);
            int speed = lkj.getSpeed();
            String pipe = lkj.getPipePressure();

            if (speed == 0 && StringUtils.isNotEmpty(pipe)) {   //速度为0或者管压非空

                Lkj cur = lkj;
                Lkj temp = cur;
                int t = 1;

                //往下寻找直到不同值跳出
                while (NumberUtils.toInt(cur.getPipePressure()) == NumberUtils.toInt(temp.getPipePressure()) && (i+t) < end) {
                    cur = list.get(i+t);
                    t++;
                }

                //判断这段相同缸压值是否超过5秒
                if (DateUtils.diffDate(temp.getTime(), cur.getTime(),5)) {
                    int ipipe = NumberUtils.toInt(temp.getPipePressure());
                    if (constPipe == 500) {
                        if (constPipe - ipipe >= 100 && constPipe - ipipe <= 170) { //客车
                            return;
                        }
                    } else {
                        if (constPipe - ipipe >= 140 && constPipe - ipipe <= 200) {   //货车
                            return;
                        }
                    }

                    i = i + t - 1;
                    temp = cur;
                }
            }

        }

        //计入违章
        Lkj lkj = list.get(end);
        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_2)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_2).builder());
    }
}
