package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangxiaokuan
 * @Date 2019-10-22
 * @Description: 库内未做走车试验
 * 1、速度为0，库内
 * 3、闸缸有大于规定值以上记录（默认100KPA）；
 * 4、机车工况在“非零位”，且持续时间大于规定值（默认1秒）；
 */
@Service
public class Item137 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        
        for (int i = start; i < end; i++) {
            Lkj lkj = list.get(i);

            if (lkj.getSpeed() == 0) {
                
                List<Lkj> zero = new ArrayList();
                int t = i + 1;
                Lkj cur = lkj;
                
                while (t < end) {
                    if (cur.getSpeed() != 0) {
                        i = t;
                        break;
                    }
                    zero.add(cur);
                    cur = list.get(t++);
                }

                boolean isCy = false;
                boolean isCosis = false;

                if (zero.size() > 1) {
                    for (int j = 0; j < zero.size(); j++) {
                        Lkj lkj1 = zero.get(j);
                        if (NumberUtils.toInt(lkj1.getCylinderPressure()) >= 100) {
                            isCy = true;
                        }
                        if ("非零".equals(lkj1.getZeroBit())) {
                            int t1 = j;
                            Lkj temp = lkj1;
                            cur = zero.get(t1);
                            while (t1 < zero.size()) {
                                if ("非零".equals(cur.getZeroBit())) {
                                    if (DateUtils.diffDate(temp.getTime(),cur.getTime(),1)) {
                                        isCosis = true;
                                        i = i + zero.size();
                                        break;
                                    }
                                }
                                cur = zero.get(t1++);
                            }
                        }

                        if (isCy && isCosis) {
                            break;
                        } else {
                            add(new PhaseAssist.Builder().setLkj(cur).setLkjId(lkjId).setStart(cur.getTime())
                                    .setEnd(cur.getTime()).setItemEnum(ItemRecord.ItemEnum.I33_137)
                                    .setPhaseEnum(Phase.PhaseEnum.LKJ_137).setItemRecordIllegal(4).setPhaseIllegal(1).builder());
                            i = i + zero.size();
                            break;
                        }
                    }
                }

            }

        }
        
    }
}
