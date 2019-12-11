package com.zhuzhou.impl.statistic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.dao.statistic.StatisticMapper;
import com.zhuzhou.entity.bo.DriverPhasesStatisticBO;
import com.zhuzhou.entity.bo.PhasesStatisticBO;
import com.zhuzhou.entity.statistic.*;
import com.zhuzhou.impl.convert.PhasesStatistic2PhasesStatisticBO;
import com.zhuzhou.page.statistic.*;
import com.zhuzhou.spi.statistic.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticMapper statisticMapper;

    public StatisticServiceImpl() {
    }

    @Override
    public Integer todayDriver() {
        return statisticMapper.todayDriver();
    }

    @Override
    public List<NearlyWeekDateNumber> nearlyWeekRailNumber() {
        List<NearlyWeekDateNumber> sourceList = statisticMapper.nearlyWeekRailNumber();
        return full(sourceList);
    }

    @Override
    public List<NearlyWeekDateNumber> nearlyWeekPhaseNumber() {
        List<NearlyWeekDateNumber> sourceList = statisticMapper.nearlyWeekPhaseNumber();
        return full(sourceList);
    }

    @Override
    public List<NearlyWeekDateNumber> nearlyWeekIllegalPhaseNumber() {
        List<NearlyWeekDateNumber> sourceList = statisticMapper.nearlyWeekIllegalPhaseNumber();
        return full(sourceList);

    }

    @Override
    public List<NearlyWeekDateNumber> nearlyWeekIllegalGestureNumber() {
        List<NearlyWeekDateNumber> sourceList = statisticMapper.nearlyWeekIllegalGestureNumber();
        return full(sourceList);
    }

    @Override
    public List<NearlyWeekGestureAnalysis> nearlyWeekGestureAnalysis() {
        List<NearlyWeekGestureAnalysis> sourceList = statisticMapper.nearlyWeekGestureAnalysis();
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }
        int size = sourceList.size();
        if (size == 7) {
            return sourceList;
        }
        List<NearlyWeekGestureAnalysis> list = new ArrayList<>(7);
        LocalDate today = new LocalDate();
        int a = 0;
        // 七天
        for (int i = 0; i < 7; i++) {
            NearlyWeekGestureAnalysis gesture = sourceList.get(a);
            String date = gesture.getDriverDate();
            LocalDate dateTime = today.minusDays(6 - i);
            if (dateTime.toString().equals(date)) {
                list.add(gesture);
                if (a < size - 1) {
                    a++;
                }
            } else {
                list.add(new NearlyWeekGestureAnalysis()
                        .setDriverDate(dateTime.toString())
                        .setGesturePhase(0L)
                        .setMiss(0L)
                        .setNormal(0L)
                        .setUndispose(0L)
                        .setIllegal(0L));
            }
        }
        return list;
    }

    @Override
    public IPage<OneRailAnalysis> oneRailAnalysis(RecordIdPage page) {
        return statisticMapper.oneRailAnalysis(page);
    }

    @Override
    public IPage<RailStatistic> railStatistic(RailStatisticPage page) {
        return statisticMapper.railStatistic(page);
    }

    @Override
    public IPage<DriverPhasesStatisticBO> driverStatistic(DriverStatisticPage page) {
        IPage<DriverPhasesStatistic> listPage = statisticMapper.driverStatistic(page);
        return driverFormat(listPage);
    }

    @Override
    public IPage<PhasesStatisticBO> workshopStatistic(SearchPage page) {
        IPage<PhasesStatistic> listPage = statisticMapper.workshopStatistic(page);
        return format(listPage);
    }

    @Override
    public IPage<PhasesStatisticBO> locomotiveDepotStatistic(SearchPage page) {
        IPage<PhasesStatistic> listPage = statisticMapper.locomotiveDepotStatistic(page);
        return format(listPage);
    }

    @Override
    public IPage<PhasesStatisticBO> railwayStatistic(SearchPage page) {
        IPage<PhasesStatistic> listPage = statisticMapper.railwayStatistic(page);
        return format(listPage);
    }

    /**
     * 判断够不够7条数据，不够加空的 num=0
     *
     * @param sourceList
     * @return
     */
    private List<NearlyWeekDateNumber> full(List<NearlyWeekDateNumber> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            // 为空的话给一个明天的时间，生成七条空数据返回给前端展示
            sourceList.add(new NearlyWeekDateNumber().setDriverDate(new LocalDate().minusDays(-1).toString()));
        }
        int size = sourceList.size();
        // 够7条数据直接返回
        if (size == 7) {
            return sourceList;
        }
        List<NearlyWeekDateNumber> list = new ArrayList<>(7);
        LocalDate today = new LocalDate();
        int a = 0;
        // 七天
        for (int i = 0; i < 7; i++) {
            NearlyWeekDateNumber dateNumber = sourceList.get(a);
            String date = dateNumber.getDriverDate();
            LocalDate dateTime = today.minusDays(6 - i);
            if (dateTime.toString().equals(date)) {
                list.add(dateNumber);
                if (a < size - 1) {
                    a++;
                }
            } else {
                list.add(new NearlyWeekDateNumber()
                        .setDriverDate(dateTime.toString())
                        .setNum(0L));
            }
        }
        return list;
    }

    /**
     * 将查到的数据转换成前端方便处理的对象
     *
     * @param listPage
     * @return
     */
    private IPage<PhasesStatisticBO> format(IPage<PhasesStatistic> listPage) {
        List<PhasesStatistic> list = listPage.getRecords();
        List<PhasesStatisticBO> listBO = new ArrayList<>();
        for (PhasesStatistic o : list) {
            PhasesStatisticBO bo = new PhasesStatisticBO();
            HashMap<Integer, Integer> map = new HashMap<>(8);
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (name.startsWith("phase")) {
                    int phase = Integer.parseInt(name.substring(5));
                    try {
                        int num = (int) field.get(o);
                        if (num > 0) {
                            map.put(phase, num);
                        }
                    } catch (IllegalAccessException e) {
                        log.error("数据类型转换出错{}", e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Object value = field.get(o);
                        Field boField = bo.getClass().getDeclaredField(name);
                        boField.setAccessible(true);
                        boField.set(bo, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            bo.setPhases(map);
            listBO.add(bo);
        }
        PhasesStatistic2PhasesStatisticBO convert = new PhasesStatistic2PhasesStatisticBO();
        IPage<PhasesStatisticBO> listPageBO = convert.phasesStatisticConvert(listPage);
        return listPageBO.setRecords(listBO).setTotal((long) listBO.size());
    }

    private IPage<DriverPhasesStatisticBO> driverFormat(IPage<DriverPhasesStatistic> listPage) {
        List<DriverPhasesStatistic> list = listPage.getRecords();
        List<DriverPhasesStatisticBO> listBO = new ArrayList<>();
        for (DriverPhasesStatistic o : list) {
            DriverPhasesStatisticBO bo = new DriverPhasesStatisticBO();
            Map<Integer, Integer> map = new HashMap<>(256);
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if (name.startsWith("phase")) {
                    try {
                        int phase = Integer.parseInt(name.substring(5));
                        int num = (int) field.get(o);
                        if (num > 0) {
                            map.put(phase, num);
                        }
                    } catch (Exception e) {
                        log.error("数据类型转换出错{}", e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Object value = field.get(o);
                        Field boField = bo.getClass().getDeclaredField(name);
                        boField.setAccessible(true);
                        boField.set(bo, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            bo.setPhases(map);
            listBO.add(bo);
        }
        PhasesStatistic2PhasesStatisticBO convert = new PhasesStatistic2PhasesStatisticBO();
        IPage<DriverPhasesStatisticBO> listPageBO = convert.driverConvert(listPage);
        listPageBO.setRecords(listBO).setTotal((long) listBO.size());
        return listPageBO;
    }
}
