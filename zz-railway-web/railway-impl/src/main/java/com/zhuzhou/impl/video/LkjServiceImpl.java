package com.zhuzhou.impl.video;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.dao.video.LkjMapper;
import com.zhuzhou.entity.ai.CoverStatus;
import com.zhuzhou.entity.ai.FrontStatus;
import com.zhuzhou.entity.ai.MachineRoomStatus;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.enums.lkj.SignalMachineEnum;
import com.zhuzhou.enums.video.FileSufEnum;
import com.zhuzhou.framework.utils.spring.ApplicationContextUtils;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.phase.EventName;
import com.zhuzhou.phase.core.DefaultPackItem;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.ai.CoverStatusService;
import com.zhuzhou.spi.ai.FrontStatusService;
import com.zhuzhou.spi.ai.MachineRoomStatusService;
import com.zhuzhou.spi.interior.InteriorService;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.spi.video.LkjService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Service
@Slf4j
public class LkjServiceImpl extends BaseServiceImpl<LkjMapper, Lkj> implements LkjService {

    @Autowired
    private MachineRoomStatusService machineRoomStatusService;

    @Autowired
    private FrontStatusService frontStatusService;

    @Autowired
    private CoverStatusService coverStatusService;

    @Autowired
    private InteriorService interiorService;

    @Autowired
    private IdxIndexService idxIndexService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void analysisPhase(String recordId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead) {
        log.info("手势项点分析开始！id：{},时间:{}", recordId, LocalDateTime.now().toString());

        //预处理lkj文件，使得分析更加简单
        for (int i = 0; i < list.size(); i++) {
            if (list.size() - 1 == i) {
                break;
            }
            Lkj lkj = list.get(i);
            String signalMachine = lkj.getSignalMachine();

            //如果为站中心，站名。另外修改
            if (!SignalMachineEnum.fromValue(signalMachine)) {
                int t = 1;
                String signal = null;
                do {
                    Lkj next = list.get(i + t);
                    String nextSignalMachine = next.getSignalMachine();
                    if (SignalMachineEnum.fromValue(nextSignalMachine)) {
                        signal = nextSignalMachine;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t + 1);
                if (StringUtils.isNotEmpty(signal)) {
                    for (int j = 0; j < t; j++) {
                        Lkj next = list.get(i + j);
                        next.setSignalMachine(signal);
                    }
                    i += t;
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                break;
            }
            Lkj lkj = list.get(i);
            String frontBehind = lkj.getFrontBehind();
            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("中")) {
                int t = 1;
                do {
                    Lkj next = list.get(i + t);
                    String nextFrontBehind = next.getFrontBehind();
                    if (StringUtils.isNotEmpty(nextFrontBehind) && nextFrontBehind.contains("中")) {
                    } else {
                        String nextFrontStatus = "";
                        if (nextFrontBehind.contains("前")) {
                            nextFrontStatus = "前";
                        } else {
                            nextFrontStatus = "后";
                        }
                        int cur = t;
                        do {
                            --t;
                            Lkj temp = list.get(i + t);
                            temp.setFrontBehind(nextFrontStatus);
                        } while (t > 0);
                        i += cur;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t + 1);
            }
        }

        DefaultPackItem pack = ApplicationContextUtils.getContext().getBean("defaultPackItem", DefaultPackItem.class);
        pack.injectHand(list, lkjHead, recordId, dir, mp4List);
        log.info("手势项点分析结束！id：{},时间：{}", recordId, LocalDateTime.now().toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void analysisLkjPhase(List<Lkj> list, String lkjId) {
        log.info("LKJ开始分析项点，LKJ_ID：{},时间:{}", lkjId, LocalDateTime.now().toString());
        //处理lkj文件，主要是限速，距离不规范都去除，方便做查询
        LkjHead lkjHead = processLkj(list);
        DefaultPackItem pack = ApplicationContextUtils.getContext().getBean("defaultPackItem", DefaultPackItem.class);
        pack.inspectInject(list, lkjHead, lkjId);
        log.info("lkj项点分析完成，LKJ_ID：{},时间:{}", lkjId, LocalDateTime.now().toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void analysisMachine(String recordId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead) {
        log.info("机械间巡检分析开始！id：{},时间:{}", recordId, LocalDateTime.now().toString());
        if (CollectionUtils.isEmpty(mp4List)) {
            log.info("无机械间视频！id：{},时间：{}", recordId, LocalDateTime.now().toString());
            return;
        }
        //过滤剩机械间视频
        Map<String, List<Mp4>> machineMap = mp4List.stream().filter(f -> f.getChannelName().contains("机械间")).collect(Collectors.toMap(Mp4::getChannelNum,
                v -> {
                    List<Mp4> objects = new ArrayList<>();
                    objects.add(v);
                    return objects;
                }, (x, y) -> {
                    x.addAll(y);
                    return x;
                }
        ));
        List<Mp4> mp4s = null;
        for (Map.Entry<String, List<Mp4>> map : machineMap.entrySet()) {
            mp4s = map.getValue();
            break;
        }
        if (CollectionUtils.isEmpty(mp4s)) {
            log.info("无机械间视频！id：{},时间：{}", recordId, LocalDateTime.now().toString());
            return;
        }

        Date startTime = null;
        Date endTime = null;
        for (Lkj lkj : list) {
            String eventItem = lkj.getEventItem();
            if ("前端巡检1".equals(eventItem)) {
                startTime = lkj.getTime();
            }
            if ("前端巡检2".equals(eventItem)) {
                endTime = lkj.getTime();
            }

            if (startTime != null && endTime != null) {
                //新增
                MachineRoomStatus mr = new MachineRoomStatus();
                mr.setStartTime(startTime);
                mr.setEndTime(endTime);
                mr.setLkjId(recordId);
                String file;
                if (StringUtils.isEmpty(file = mp4ListProcess(dir, startTime, mp4s))) {
                    mr.setStatus(2);
                } else {
                    mr.setStatus(1);
                }
                mr.setFile(file);
                machineRoomStatusService.save(mr);
                startTime = null;
                endTime = null;
            } else if (startTime != null || endTime != null) {
                if (startTime != null) {
                    //如果前端巡检1->前端巡检2大于3分钟未处理，默认为多按操作
                    if (DateUtils.diffDate(lkj.getTime(), startTime, 180)) {
                        startTime = null;
                    }
                }
                if (endTime != null) {
                    if (DateUtils.diffDate(lkj.getTime(), endTime, 180)) {
                        endTime = null;
                    }
                }
            }
        }
        log.info("机械间巡检分析完毕！id：{},时间:{}", recordId, LocalDateTime.now().toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void analysisFront(String lkjId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead) {
        log.info("前方障碍物识别开始！id：{},时间:{}", lkjId, LocalDateTime.now().toString());
        if (CollectionUtils.isEmpty(mp4List)) {
            log.info("无前方视频！id：{},时间：{}", lkjId, LocalDateTime.now().toString());
            return;
        }
        boolean frontFlag = mp4List.stream().anyMatch(f -> f.getChannelName().contains("路况"));
        List<Mp4> collect;
        if (frontFlag) {
            collect = mp4List.stream().filter(f -> f.getChannelName().contains("路况")).collect(Collectors.toList());
        } else {
            collect = mp4List.stream().filter(f -> f.getChannelName().contains("前方")).collect(Collectors.toList());
        }
        if (collect.size() <= 0) {
            log.info("无前方视频！id：{},时间：{}", lkjId, LocalDateTime.now().toString());
        }
        String frontStatus = "";
        String option = lkjHead.getOption();
        if ("I端".equals(option)) {
            frontStatus = "前";
        } else {
            frontStatus = "后";
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                break;
            }
            Lkj lkj = list.get(i);
            String frontBehind = lkj.getFrontBehind();
            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("前")) {
                frontStatus = "前";
            } else if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("后")) {
                frontStatus = "后";
            }

            if ("前".equals(frontStatus) || "后".equals(frontStatus)) {
                boolean flag = false;
                int t = 1;
                do {
                    String tempFrontStatus = "";
                    Lkj temp = list.get(i + t);
                    String tempFrontBehind = temp.getFrontBehind();
                    if (StringUtils.isNotEmpty(tempFrontBehind) && tempFrontBehind.contains("前")) {
                        tempFrontStatus = "前";
                    } else if (StringUtils.isNotEmpty(tempFrontBehind) && tempFrontBehind.contains("后")) {
                        tempFrontStatus = "后";
                    }
                    List<Mp4> mp4Collect = null;
                    if ("前".equals(frontStatus) && "后".equals(tempFrontStatus)) {
                        mp4Collect = collect.stream().filter(f -> f.getChannelName().contains("一端")).collect(Collectors.toList());
                        flag = true;
                    } else if ("后".equals(frontStatus) && "前".equals(tempFrontStatus)) {
                        mp4Collect = collect.stream().filter(f -> f.getChannelName().contains("二端")).collect(Collectors.toList());
                        flag = true;
                    }
                    if (flag) {
                        try {
                            FrontStatus fs = new FrontStatus();
                            fs.setRecordId(lkjId);
                            fs.setStartTime(lkj.getTime());
                            fs.setEndTime(temp.getTime());
                            String file;
                            if (StringUtils.isEmpty(file = mp4ListProcess(dir, lkj.getTime(), mp4Collect))) {
                                fs.setStatus(2);
                            } else {
                                fs.setStatus(1);
                            }
                            fs.setFile(file);
                            frontStatusService.save(fs);
                        } catch (Exception e) {
                            log.info("对应的lkj文件为：{},视频文件缺失", lkjId, e);
                        }
                        flag = true;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t);
                if (!flag) {
                    FrontStatus fs = new FrontStatus();
                    fs.setRecordId(lkjId);
                    fs.setStartTime(lkj.getTime());
                    fs.setEndTime(list.get(i + t - 1).getTime());
                    if ("前".equals(frontStatus)) {
                        List<Mp4> mp4Collect = collect.stream().filter(f -> f.getChannelName().contains("一端")).collect(Collectors.toList());
                        String file;
                        if (StringUtils.isEmpty(file = mp4ListProcess(dir, lkj.getTime(), mp4Collect))) {
                            fs.setStatus(2);
                        } else {
                            fs.setStatus(1);
                        }
                        fs.setFile(file);
                        frontStatusService.save(fs);
                    } else if ("后".equals(frontStatus)) {
                        List<Mp4> mp4Collect = collect.stream().filter(f -> f.getChannelName().contains("二端")).collect(Collectors.toList());
                        String file;
                        if (StringUtils.isEmpty(file = mp4ListProcess(dir, lkj.getTime(), mp4Collect))) {
                            fs.setStatus(2);
                        } else {
                            fs.setStatus(1);
                        }
                        fs.setFile(file);
                        frontStatusService.save(fs);
                    }
                }
                i += t;
            }
        }
        log.info("前方障碍物分析完毕！id：{},时间:{}", lkjId, LocalDateTime.now().toString());
    }

    @Override
    public void analysisCover(String lkjId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead) {
        log.info("遮挡识别开始！id：{},时间:{}", lkjId, LocalDateTime.now().toString());
        if (CollectionUtils.isEmpty(mp4List)) {
            log.info("无遮挡视频！id：{},时间：{}", lkjId, LocalDateTime.now().toString());
            return;
        }
        String coverStatus = "";
        String option = lkjHead.getOption();
        if ("I端".equals(option)) {
            coverStatus = "前";
        } else {
            coverStatus = "后";
        }
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            String frontBehind = lkj.getFrontBehind();
            if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("前")) {
                coverStatus = "前";
            } else if (StringUtils.isNotEmpty(frontBehind) && frontBehind.contains("后")) {
                coverStatus = "后";
            }
            if ("前".equals(coverStatus) || "后".equals(coverStatus)) {
                boolean flag = false;
                int t = 1;
                do {
                    String tempCoverStatus = "";
                    Lkj temp = list.get(i + t);
                    String tempFrontBehind = temp.getFrontBehind();
                    if (StringUtils.isNotEmpty(tempFrontBehind) && tempFrontBehind.contains("前")) {
                        tempCoverStatus = "前";
                    } else if (StringUtils.isNotEmpty(tempFrontBehind) && tempFrontBehind.contains("后")) {
                        tempCoverStatus = "后";
                    }
                    if ("前".equals(coverStatus) && "后".equals(tempCoverStatus)) {
                        flag = true;
                    } else if ("后".equals(coverStatus) && "前".equals(tempCoverStatus)) {
                        flag = true;
                    }
                    if (flag) {
                        CoverStatus fs = new CoverStatus();
                        fs.setRecordId(lkjId);
                        fs.setStartTime(lkj.getTime());
                        fs.setEndTime(temp.getTime());
                        String file;
                        if (StringUtils.isEmpty(file = front(lkj.getTime(), mp4List, coverStatus))) {
                            fs.setStatus(2);
                        } else {
                            fs.setStatus(1);
                            fs.setFile(dir + file);
                        }
                        coverStatusService.save(fs);
                        flag = true;
                        break;
                    }
                    ++t;
                } while (list.size() > i + t);
                if (!flag) {
                    CoverStatus fs = new CoverStatus();
                    fs.setRecordId(lkjId);
                    fs.setStartTime(lkj.getTime());
                    fs.setEndTime(list.get(i + t - 1).getTime());
                    String file;
                    if (StringUtils.isEmpty(file = front(lkj.getTime(), mp4List, coverStatus))) {
                        fs.setStatus(2);
                    } else {
                        fs.setStatus(1);
                        fs.setFile(dir + file);
                    }
                    coverStatusService.save(fs);
                }
                i += t;
            }
        }
        log.info("遮挡分析完毕！id：{},时间:{}", lkjId, LocalDateTime.now().toString());
    }

    @Override
    public LkjHead processLkj(List<Lkj> list) {
        LkjHead h = new LkjHead();
        Field[] fields = h.getClass().getDeclaredFields();
        List<Lkj> temp = new ArrayList<>();
        for (int i = 0;i < list.size();i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            String other = lkj.getOther();
            if (i < 40) {
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
                    || "工务揭示命令号".equals(eventItem) || "有效揭示条数".equals(eventItem) || "数据交路号".equals(eventItem)
                    || "运行径路".equals(eventItem) || "未知事件".equals(eventItem) || "记录日期".equals(eventItem)
                    || "警惕确认".equals(eventItem)) {
                continue;
            }

            //过机校正，补上错误点距离
            if ("过机校正".equals(lkj.getEventItem())) {
                String[] split = other.split(",");
                try {
                    int v = NumberUtils.toInt(split[0]);
                    Lkj pre = list.get(i - 1);
                    int distance = pre.getDistance();
                    //如果累加距离超过上一条记录的距离，不做处理
                    if (distance > lkj.getDistance() + v) {
                        lkj.setDistance(lkj.getDistance() + v);
                    }
                } catch (Exception e) {
                }
            }
            temp.add(lkj);
        }
        list.clear();
        list.addAll(temp);
        return h;
    }


    /**
     * 获取指定视频对应的项点识别
     *
     * @param date
     * @param mp4List
     * @param frontBehind
     * @return if 返回为空，代表无此视频文件
     */
    private String front(Date date, List<Mp4> mp4List, String frontBehind) {
        String hms = DateUtils.format(DateUtils.all, date);
        List<Mp4> frontList = new ArrayList<>();
        List<Mp4> behindList = new ArrayList<>();
        for (Mp4 m : mp4List) {
            String channelName = m.getChannelName();
            if (StringUtils.isNotEmpty(channelName) && channelName.contains("一端司机室")) {
                frontList.add(m);
            }
            if (StringUtils.isNotEmpty(channelName) && channelName.contains("二端司机室")) {
                behindList.add(m);
            }
        }

        if ("前".equals(frontBehind)) {
            return getList(hms, frontList);
        } else if ("后".equals(frontBehind)) {
            return getList(hms, behindList);
        }
        return null;
    }


    /**
     * 获取指定视频对应的项点识别
     *
     * @param hms
     * @param frontList
     * @return if 返回为空，代表无此视频文件
     */
    private String getList(String hms, List<Mp4> frontList) {
        try {
            for (int k = 0; k < frontList.size(); k++) {
                Mp4 m = frontList.get(k);
                if (k == frontList.size() - 1) {
                    String prefixDate = m.getPrefixDate();
                    String suffixDate = m.getSuffixDate();
                    int compare = hms.compareTo(prefixDate + suffixDate);
                    if (compare < 0) {
                        return mp4Result(frontList.get(k - 1));
                    }
                    return mp4Result(m);
                } else {
                    String prefixDate = m.getPrefixDate();
                    String suffixDate = m.getSuffixDate();
                    int compare = hms.compareTo(prefixDate + suffixDate);
                    if (compare < 0) {
                        return mp4Result(frontList.get(k - 1));
                    }
                }
            }
        } catch (Exception e) {
            log.info("视频文件缺失,跳过", e);
            return null;
        }
        return null;
    }

    /**
     * 拼接视频完整名称
     *
     * @param m
     * @return
     */
    private String mp4Result(Mp4 m) {
        return m.getCabNum() + SymbolConst.UNDER + m.getFactoryName() + SymbolConst.UNDER + m.getChannelNum() +
                SymbolConst.UNDER + m.getChannelName() + SymbolConst.UNDER + m.getPrefixDate() +
                SymbolConst.UNDER + m.getSuffixDate() + SymbolConst.DOT + FileSufEnum.MP4.getSuf();
    }

    private String mp4ListProcess(String dir, Date date, List<Mp4> mp4List) {
        String hms = DateUtils.format(DateUtils.all, date);
        String mp4Str;
        if (StringUtils.isEmpty(mp4Str = getList(hms, mp4List))) {
            return mp4Str;
        }
        return dir + mp4Str;
    }
}
