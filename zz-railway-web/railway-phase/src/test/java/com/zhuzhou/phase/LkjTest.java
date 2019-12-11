package com.zhuzhou.phase;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.phase.item.*;
import com.zhuzhou.phase.model.Situation;
import com.zhuzhou.phase.utils.SituationUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chenzeting
 * @Date 2019-10-14
 * @Description:
 **/
public class LkjTest {

    /**
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\byb\\Desktop\\33049-5038407.1111.xls");
        FileInputStream input = new FileInputStream(file);
        List<Lkj> list = ExcelUtils.read(input, Lkj.class, setRosterIntegerMapping());
        LkjHead lkjHead = processLkj(list);
        List<Situation> situations = SituationUtil.getSection(list);
        for (Situation s : situations){
            if ("其它".equals(s.getSituation())) {
                Item27 item = new Item27();
                item.exec(list, lkjHead,"",s.getStart(),s.getEnd());
            }
        }
    }

    private static Map<Integer, String> setRosterIntegerMapping() {
        Map<Integer, String> mapping = new LinkedHashMap<>();
        mapping.put(0, "lkjNo");
        mapping.put(1, "eventItem");
        mapping.put(2, "time");
        mapping.put(3, "kiloMeter");
        mapping.put(4, "other");
        mapping.put(5, "distance");
        mapping.put(6, "signalMachine");
        mapping.put(7, "signals");
        mapping.put(8, "speed");
        mapping.put(9, "rateLimit");
        mapping.put(10, "zeroBit");
        mapping.put(11, "transaction");
        mapping.put(12, "frontBehind");
        mapping.put(13, "pipePressure");
        mapping.put(14, "cylinderPressure");
        mapping.put(15, "speedElectricity");
        mapping.put(16, "cylinder1");
        mapping.put(17, "cylinder2");
        return mapping;
    }

    public static LkjHead processLkj(List<Lkj> list) {
        LkjHead h = new LkjHead();
        Field[] fields = h.getClass().getDeclaredFields();
        List<Lkj> temp = new ArrayList<>();
        for (int i = 0;i < list.size();i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            String other = lkj.getOther();
            if (i < 30) {
                for (Field f : fields) {
                    f.setAccessible(true);
                    EventName name = f.getAnnotation(EventName.class);
                    if (name != null && eventItem.equals(name.value())) {
                        Class<?> type = f.getType();
                        Object obj = null;
                        if(type == Integer.class || int.class == type){
                            obj = NumberUtils.toInt(other);
                        } else if(type == Float.class || float.class == type){
                            obj = NumberUtils.toFloat(other);
                        }else if (type == String.class) {
                            obj = other;
                        }
                        try {
                            f.set(h, obj);
                        } catch (IllegalAccessException e) {
                        }
                    }
                }
                continue;
            }
            if ("各通道速度".equals(eventItem) || "经过站号".equals(eventItem) || "工务线路号".equals(eventItem)
                    || "线路上下行".equals(eventItem) || "线路主三线".equals(eventItem) || "线路正反向".equals(eventItem)
                    || "重复公里标序号".equals(eventItem) || "速度等级".equals(eventItem) || "IC卡验证吗".equals(eventItem)
                    || "工务揭示命令号".equals(eventItem) || "有效揭示条数".equals(eventItem) || "未知事件".equals(eventItem)
                    ||  "记录日期".equals(eventItem) || "警惕确认".equals(eventItem)) {
                continue;
            }
            temp.add(lkj);
        }
        list.clear();
        list.addAll(temp);
        return h;
    }
}
