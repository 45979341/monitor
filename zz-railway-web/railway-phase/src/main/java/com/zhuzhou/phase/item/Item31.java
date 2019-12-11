package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.HeadUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.record.chart.SeriesListRecord;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * @author xiechonghu
 * @date 2019/10/22 9:27
 * @description: 项点名称：紧急制动后小闸违规缓解
 * 1、运行途中，列车实时紧急制动，需要判断制动缸压力是否违规缓解；
 * 2、判断范围，从定压管压变化到机车速度为0为止；
 * 3、在该范围内，如果制动缸压力小于规定值（默认250KPA），则违章；
 * 判断范围：从管压从定压开始下降到速度为0，缸压力不能小于250kpa。
 */
@Service
public class Item31 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("紧急制动".equals(eventItem)) {
                Date startTime = lkj.getTime();
                Lkj startLkj = lkj;
                boolean flag = false;
                Lkj next;
                int lkjPipe;
                int nextPipe;
                do {
                    lkj = list.get(i);
                    next = list.get(i + 1);
                    // 往下找的时候可能又检索到了紧急制动事件
                    if ("紧急制动".equals(lkj.getEventItem())) {
                        startTime = lkj.getTime();
                    }
                    lkjPipe = NumberUtils.toInt(lkj.getPipePressure());
                    nextPipe = NumberUtils.toInt(next.getPipePressure());
                    // 管压上升跳出
                    if (lkjPipe < nextPipe) {
                        break;
                    }
                    // 速度和管压为0跳出
                    if (lkj.getSpeed() == 0 && NumberUtils.toInt(lkj.getPipePressure()) == 0) {
                        break;
                    }
                } while (lkjPipe == nextPipe && ++i < end);
                // 压力下降才开始计算
                if (lkjPipe > nextPipe) {
                    lkj = list.get(i++);
                    if ("紧急制动".equals(lkj.getEventItem())) {
                        startTime = lkj.getTime();
                    }
                    // 速度大于0 压力大于0
                    if (lkj.getSpeed() > 0 && NumberUtils.toInt(lkj.getPipePressure()) > 0) {
                        // 缸压小于250
                        if (NumberUtils.toInt(lkj.getCylinderPressure()) < 250) {
                            flag = true;
                        }
                    }
                    // 速度和管压为0跳出
                    if (lkj.getSpeed() == 0 && NumberUtils.toInt(lkj.getPipePressure()) == 0) {
                        break;
                    }
                }
                if (flag) {
                    add(new PhaseAssist.Builder().setLkj(startLkj).setLkjId(lkjId).setStart(startTime)
                            .setEnd(lkj.getTime()).setItemEnum(ItemRecord.ItemEnum.I9_31)
                            .setPhaseEnum(Phase.PhaseEnum.LKJ_31).builder());
                }
            }
        }
    }
}