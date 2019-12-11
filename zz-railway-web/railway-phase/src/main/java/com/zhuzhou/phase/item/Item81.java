package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-18
 * @Description: 未转换机车信号开车
 * 1、机车运行途中，站内开车时（包括始发站），机车信号为红灯，白灯；
 * 3、分析范围。从进站到出站后下两个信号机，信号机没有从红灯，白灯转其他灯。没转其他灯就是违章
 */
@Service
public class Item81 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {

        //存储每一对进站出站
        List<List<Lkj>> inOut = new ArrayList<>();
        boolean isIn = false;
        boolean isOut = false;
        List<Lkj> list1 = new ArrayList<>();
        Lkj lkj = null;

        //先找到一对进出站及出站后两个信号灯范围
        for (int i = start;i<end;i++) {

            lkj = list.get(i);

            if (lkj.getEventItem().equals("进站")) {
                isIn = true;
            }

            if (isIn && lkj.getEventItem().equals("出站")) {
                list1.add(lkj);
                isOut = true;
            } else if (isIn) {
                list1.add(lkj);
            }

            //进站并且出站
            if (isIn && isOut) {
                int t = i + 1;
                //信号机
                int signalNum = 1;
                Lkj cur = list.get(t);
                Lkj temp = cur;
                while (t < list.size()) {
                    cur = list.get(++t);
                    if (cur.getEventItem().equals("进站")) {      //这种是应对那种没有经过两个信号机就进入下一个站的情况
                        inOut.add(list1);
                        isIn = false;
                        isOut = false;
                        list1 = new ArrayList<>();
                        i = t - 1;
                        break;
                    }
                    if (temp.getSignalMachine() != null && cur.getSignalMachine() != null) {
                        //如果进入下一个信号机则加1
                        if (!temp.getSignalMachine().equals(cur.getSignalMachine())) {
                            signalNum++;
                        }
                    }

                    if (signalNum <= 2) {
                        list1.add(temp);
                        temp = cur;
                    } else {        //遇到第三个信号机范围就退出当前嵌套循环，寻找下一个范围
                        inOut.add(list1);
                        isIn = false;
                        isOut = false;
                        list1 = new ArrayList<>();
                        i = t;
                        break;
                    }
                }
            }

        }

        for (List<Lkj> lkjs : inOut) {  //循环每一个范围
            Lkj cur = null;
            boolean isChange = true;
            for (int i = 0; i < lkjs.size(); i++) {
                cur = lkjs.get(i);
                if ("红灯".equals(cur.getSignals()) || "白灯".equals(cur.getSignals())) {   //范围内如果有红灯白灯则进入判断
                    isChange = false;
                    int t = i + 1;
                    while (t < lkjs.size()) {
                        cur = lkjs.get(t++);
                        if (!(cur.getSignals().equals("红灯") || cur.getSignals().equals("白灯"))) {//若没有转换成白灯红灯以外
                            isChange = true;
                            break;
                        }
                    }
                    if (!isChange) {
                        add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I27_81)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_81).builder());
                        break;
                    }
                }
            }

        }

    }
}
