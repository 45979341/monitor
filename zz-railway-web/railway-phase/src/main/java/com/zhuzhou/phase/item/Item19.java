package com.zhuzhou.phase.item;

import com.zhuzhou.entity.lkj.ItemRecord;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.core.AbstractPhase;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.phase.utils.LkjListUtil;
import org.springframework.stereotype.Service;

import java.util.*;



/**
 *  姓名：刘欣武
 *  时间：2019-10-17
 *  项点名称：停车超20分未简略试验
 *  项点描述：
 *  项点开发：1、列车中间站停车，停车时间超过规定值，则必须进行制动机试验（默认20分钟）；
 *            2、计算范围为距离起车时间20分钟内；
 *            3、试验标准参照始发站制动机试验（默认客车170KPA，货车100KPA，保压60秒）
 *  分析结果：1、监控范围内
 *              2、站内停车-站内开车范围内  根据26211车次，现在又添加个区间停车-区间开车也算进范围内
 *              3、站内停车到站内开车停留的时间必须在20分钟以上才做判断
 *              4、3范围内有管压下降的趋势，否则违章
 *              5、下降趋势范围内，下降的管压大于80以上即可
 *              6、下降趋势范围内，开始的最低压与最末的最低压的时间必须60秒以上，否则违章
 */
@Service
public class Item19 extends AbstractPhase {
    @Override
    public void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end) {
        //组装数据
        LinkedHashMap<String, List<Lkj>> map = LkjListUtil.makeDate(list, lkjId, start,end, "站内停车", "站内开车");
        processList(map,100,lkjId);
        //组装数据
        LinkedHashMap<String, List<Lkj>> map2 = LkjListUtil.makeDate(list, lkjId, start,end, "区间停车", "区间开车");
        processList(map2,100,lkjId);
    }

    public void processList(LinkedHashMap<String, List<Lkj>> map,Integer pressure,String lkjId){
        for(Map.Entry<String, List<Lkj>> entry:map.entrySet()){
            List<Lkj> list = entry.getValue();
            if(list.size()>0){
                Lkj begin = list.get(0);
                Lkj end = list.get(list.size()-1);
                if(end.getTime().getTime()-begin.getTime().getTime()>=20*60*1000){
                    //客车170KPA，货车100KPA，保压60秒
                    Integer maxPressure = LkjListUtil.getMaxPressure(list);
                    if(!LkjListUtil.changePressureLimit(list,maxPressure,pressure)){
                        //违章
                        add(new PhaseAssist.Builder().setLkj(begin).setLkjId(lkjId).setStart(begin.getTime())
                                .setEnd(begin.getTime()).setItemEnum(ItemRecord.ItemEnum.I7_19)
                                .setPhaseEnum(Phase.PhaseEnum.LKJ_19).builder());
                    }
                }
            }
        }
    }



}