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
 * @Date 2019-10-16
 * @Description: 库内无单阀试验
 * 1、机车制动缸压力有由0上升至设定值（默认300KPA，上下浮动20KPA）；
 * 2、机车制动缸压力再由设定值下降为0；
 * 3、列车管压力为定压；
 * 4、分析范围为库内作业（乘务员上车至闸楼处）；
 * 5、机车速度为零的情况。
 */
@Service
public class Item5 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        int constPipe = GarageUtils.getConstPipe(head).getConstPipe();   //获取指定车型定压值
        boolean isZero = false;     //判断缸压是否为0
        boolean isArrived = false;  //判断是否到达300上下值
        boolean isBack = false;     //判断是否回到0

        for (int i = start;i < end ; i++) {  //库内
            Lkj lkj = list.get(i);
            lkj.setRecordId(lkjId);
            int speed = lkj.getSpeed();
            String pipe = lkj.getPipePressure();

            if (speed == 0 && StringUtils.isNotEmpty(pipe)) {   //速度为0或者管压非空
                if (NumberUtils.toInt(pipe) >= constPipe - 10 && NumberUtils.toInt(pipe) <= constPipe + 10) {
                    Lkj cur = lkj;
                    int t = 1;  //定压范围
                    Lkj temp = cur;

                    while (NumberUtils.toInt(cur.getPipePressure()) >= constPipe - 10
                            && NumberUtils.toInt(cur.getPipePressure()) <= constPipe + 10
                            && (i + t) < end) {
                        cur = list.get(i + t);
                        t++;
                    }   //找到定压范围

                    if (t > 2) {
                        cur = temp;
                        for (int c = 0;c < t-1;) {  //一个定压范围内寻找
                            int k = c+1;
                            if (NumberUtils.toInt(cur.getCylinderPressure()) == 0) {
                                isZero = true;
                            }

                            if (isZero) {   //找到了为0的,开始循环是否有到300上下
                                while (k < t-1) {
                                    cur = list.get(i+k);
                                    if (NumberUtils.toInt(cur.getCylinderPressure()) >=280
                                            && NumberUtils.toInt(cur.getCylinderPressure()) <= 320) {
                                        isArrived = true;
                                        break;
                                    }
                                    k++;
                                }
                                if (isArrived) {    //查看是否到300上下,开始循环最后是否回到0
                                    while (k < t-1) {
                                        cur = list.get(i + ++k);
                                        if (NumberUtils.toInt(cur.getCylinderPressure()) == 0) {
                                            isBack = true;
                                            break;
                                        }
                                    }
                                    if (isZero && isArrived && isBack) {    //三者成立则判定成功
                                        return;
                                    }
                                    isArrived = false;
                                }
                            }

                            isZero = false;
                            cur = list.get(i + c++);
                        }

                    }
                    i = i + t;
                }

            }

        }

        //计入违章
        Lkj lkj = list.get(end);
        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_5)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_5).builder());
    }
}
