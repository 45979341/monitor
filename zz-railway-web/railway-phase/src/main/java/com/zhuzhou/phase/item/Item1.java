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
 * @Date 2019-10-15
 * @Description: 库内最小减压
 * 1、以定压为基准，需进行常规减压（设定参数值默认上限80KPA,下限40KPA）；
 * 2、列车管压力保压规定时间或以上（默认参数值60秒）；
 * 3、分析范围为库内作业（乘务员上车至闸楼处）
 */
@Service
public class Item1 extends AbstractPhase {

    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        //获取指定车型定压值
        int constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        //库内
        for (int i = start;i < end ; i++) {

            Lkj lkj = list.get(i);
            lkj.setRecordId(lkjId);
            int speed = lkj.getSpeed();
            String pipe = lkj.getPipePressure();

            //速度为0或者管压非空
            if (speed == 0 && StringUtils.isNotEmpty(pipe)) {
                Lkj cur = lkj;
                Lkj temp = cur;
                int t = 1;

                //往下寻找直到不同值跳出
                while (NumberUtils.toInt(cur.getPipePressure()) == NumberUtils.toInt(temp.getPipePressure()) && (i+t) < end) {
                    cur = list.get(i+t);
                    t++;
                }

                //判断这段相同缸压值是否超过60秒
                if (DateUtils.diffDate(temp.getTime(), cur.getTime(),60)) {
                    int ipipe = NumberUtils.toInt(temp.getPipePressure());
                    //判断是否在合法范围,上下浮动10
                    if (constPipe - ipipe >= 30 && constPipe - ipipe <= 90) {
                        return;
                    }
                    i = i + t - 1;
                    temp = cur;
                }
            }
        }

        //计入违章
        Lkj lkj = list.get(end);
        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_1)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_1).builder());

    }
}
