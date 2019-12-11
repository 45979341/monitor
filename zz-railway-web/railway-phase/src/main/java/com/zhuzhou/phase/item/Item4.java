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
 * @Description: 库内无紧急制动试验
 * 1、以定压为基准，列车管压从定压减压到20KPA或以下；
 * 2、减压时间小于等于设定值（默认5秒）；
 * 3、制动缸压力大于或等于设定值（默认400KPA，判断范围，紧急试验开始点至缓解开始点）；管压变为0后，缓慢上升到400KPA以上
 * 4、分析范围为库内作业（乘务员上车至闸楼处）；
 * 5、机车速度为零的情况。变化前管压下一条记录。
 */
@Service
public class Item4 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //获取指定车型定压值
        int constPipe = GarageUtils.getConstPipe(head).getConstPipe();

        /**
         * 三者均为真则判定正常
         */
        //管压是否在五秒内减到20以内
        boolean ps = false;
        //缸压是否升到400以上
        boolean ci = false;
        //管压是否重新回到400以上
        boolean prec = false;

        //库内
        for (int i = start;i < end ; i++) {
            Lkj lkj = list.get(i);
            int speed = lkj.getSpeed();
            String pipe = lkj.getPipePressure();

            //速度为0或者管压非空
            if (speed == 0 && StringUtils.isNotEmpty(pipe)) {

                Lkj cur = lkj;
                int t = 1;

                //定压值内才能进来
                if (NumberUtils.toInt(cur.getPipePressure()) >= constPipe-10
                        && NumberUtils.toInt(cur.getPipePressure()) <= constPipe+10) {

                    //找到定压后的点
                    while (NumberUtils.toInt(cur.getPipePressure()) >= constPipe-10
                            && NumberUtils.toInt(cur.getPipePressure()) <= constPipe+10
                            && (i+t) < end) {
                        cur = list.get(i+t);
                        t++;
                    }

                    Lkj temp = cur;

                    //开始判定是否在五秒内减压到20kpa以下
                    while ((i+t) < end) {
                        cur = list.get(i+t);
                        if (NumberUtils.toInt(cur.getPipePressure()) <= 20) {
                            //五秒内减压到20kpa以下，触发“第一个钥匙”
                            if (DateUtils.diffDate2(temp.getTime(),cur.getTime(),5)) {
                                ps = true;
                                break;
                            }
                        }
                        //重新遇到定压值退出
                        if (NumberUtils.toInt(cur.getPipePressure()) >= constPipe-10
                                && NumberUtils.toInt(cur.getPipePressure()) <= constPipe+10) {
                            ps = false;
                            i = i + t;
                            break;
                        }
                        t++;
                    }

                    //若拿到“第一个钥匙”,才能继续判别
                    if (ps) {
                        //管压大于20后退出循环
                        while (NumberUtils.toInt(cur.getPipePressure()) < 20 && (i+t) < end) {
                            if (NumberUtils.toInt(cur.getCylinderPressure()) >= 400) {
                                ci = true;
                                break;
                            }
                            cur = list.get(i + (++t));
                        }

                        if (ci) {
                            while (NumberUtils.toInt(cur.getPipePressure())<=400 && (i+t) < end) {
                                cur = list.get(i + (++t));
                                //恢复到400以上之后说明成功了
                                if (NumberUtils.toInt(cur.getPipePressure())>=400) {
                                    prec = true;
                                    break;
                                }
                            }
                        }

                        //判定成功
                        if (ps && ci && prec) {
                            return;
                        }
                    }

                }

            }

        }
        //计入违章
        Lkj lkj = list.get(end);
        add(new PhaseAssist.Builder().setLkj(lkj).setLkjId(lkjId).setStart(lkj.getTime())
                .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I1_4)
                .setPhaseEnum(Phase.PhaseEnum.LKJ_4).builder());
        return;
    }
}
