package com.zhuzhou.phase.utils;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.phase.model.Situation;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiechonghu
 * @date 2019/11/12 10:36
 * @description:
 */
public class SituationUtil {

    public static List<Situation> getSection(List<Lkj> list) {
        List<Situation> situations = new ArrayList<>(32);
        situations.addAll(out(list));
        situations.addAll(monitor(list));
        situations.addAll(shunt(list));
        situations.addAll(combination(list));
        // 其它 全部list
        situations.add(new Situation("其它", 0, list.size() - 1, null, null));
        return situations;
    }

    /**
     * 退出出段
     */
    private static List<Situation> out(List<Lkj> list) {
        List<Situation> section = new ArrayList<>(2);
        for (int i = 0; i < list.size(); i++) {
            Lkj lkj = list.get(i);
            String eventItem = lkj.getEventItem();
            if ("退出出段".equals(eventItem)) {
                Situation situation = new Situation();
                situation.setSituation("退出出段").setEnd(i).setEndTime(lkj.getTime());
                int t = 0;
                do {
                    lkj = list.get(i - ++t);
                    eventItem = lkj.getEventItem();
                } while (!"开车对标".equals(eventItem) && i - t > 0);
                situation.setStart(i - t).setStartTime(lkj.getTime());
                section.add(situation);
            }
        }
        return section;
    }

    /**
     * 监控 降级状态
     */
    private static List<Situation> monitor(List<Lkj> list) {
        //监控状态 1：监控，2：降级
        int monitorStatus = 0;
        List<Situation> timeList = new ArrayList<>();
        String eventItem;
        // 状态名字
        String name = null;
        // 每一个状态开始时间 结束时间为lkj.getTime()
        Date startTime = null;
        // 开始索引
        Integer start = null;
        Lkj lkj;
        for (int i = 1; i < list.size(); i++) {
            lkj = list.get(i);
            boolean flag = false;
            eventItem = lkj.getEventItem();
            if ("开车对标".equals(eventItem)) {
                monitorStatus = 1;
                flag = true;
            } else if (eventItem.contains("降级")) {
                monitorStatus = 2;
                flag = true;
            }
            if (flag) {
                // 第一条
                if (timeList.size() == 0) {
                    name = monitor(monitorStatus);
                    startTime = lkj.getTime();
                    // 先记录区间名称和开始时间
                    Situation situation = new Situation();
                    timeList.add(situation.setSituation(name).setStart(i).setStartTime(lkj.getTime()));
                    continue;
                }
                Situation last = timeList.get(timeList.size() - 1);
                // 这一条和上一条相同则记录为一条
                if (name.equals(last.getSituation())) {
                    last.setEnd(i).setEndTime(lkj.getTime());
                } else {
                    Situation situation = new Situation();
                    timeList.add(situation.setSituation(name).setStart(start).setEnd(i).setStartTime(startTime).setEndTime(lkj.getTime()));
                }
                name = monitor(monitorStatus);
                start = i;
                startTime = lkj.getTime();
            }
        }
        if (timeList.size() == 0) {
            return timeList;
        } else {
            Situation last = timeList.get(timeList.size() - 1);
            lkj = list.get(list.size() - 1);
            if (name.equals(last.getSituation())) {
                last.setEnd(list.size() - 1).setEndTime(lkj.getTime());
            } else {
                timeList.add(new Situation(name, start, list.size() - 1, startTime, lkj.getTime()));
            }
            return timeList;
        }

    }

    /**
     * 调车 非调车状态
     */
    private static List<Situation> shunt(List<Lkj> list) {
        // 调车状态 1：调车，2：非调车
        int shuntStatus = 0;
        List<Situation> timeList = new ArrayList<>();
        String eventItem;
        // 状态名字
        String name = null;
        // 每一个状态开始时间 结束时间为lkj.getTime()
        Date startTime = null;
        // 开始索引
        Integer start = null;
        Lkj lkj;
        for (int i = 1; i < list.size(); i++) {
            lkj = list.get(i);
            boolean flag = false;
            eventItem = lkj.getEventItem();
            if ("退出调车".equals(eventItem) || "出站".equals(eventItem)) {
                shuntStatus = 2;
                flag = true;
            } else if ("进入调车".equals(eventItem)) {
                shuntStatus = 1;
                flag = true;
            }
            if (flag) {
                // 第一条
                if (timeList.size() == 0) {
                    name = shunt(shuntStatus);
                    startTime = lkj.getTime();
                    // 先记录区间名称和开始时间
                    Situation situation = new Situation();
                    timeList.add(situation.setSituation(name).setStart(i).setStartTime(lkj.getTime()));
                    continue;
                }
                Situation last = timeList.get(timeList.size() - 1);
                // 这一条和上一条相同则记录为一条
                if (name.equals(last.getSituation())) {
                    last.setEnd(i).setEndTime(lkj.getTime());
                } else {
                    Situation situation = new Situation();
                    timeList.add(situation.setSituation(name).setStart(start).setEnd(i).setStartTime(startTime).setEndTime(lkj.getTime()));
                }
                name = shunt(shuntStatus);
                start = i;
                startTime = lkj.getTime();
            }
        }
        if (timeList.size() == 0) {
            return timeList;
        } else {
            Situation last = timeList.get(timeList.size() - 1);
            lkj = list.get(list.size() - 1);
            if (name.equals(last.getSituation())) {
                last.setEnd(list.size() - 1).setEndTime(lkj.getTime());
            } else {
                timeList.add(new Situation(name, start, list.size() - 1, startTime, lkj.getTime()));
            }
            return timeList;
        }

    }

    /**
     * 四种组合状态
     */
    private static List<Situation> combination(List<Lkj> list) {
        //监控状态 1：监控，2：降级
        int monitorStatus = 0;
        // 调车状态 1：调车，2：非调车
        int shuntStatus = 0;
        List<Situation> section = new ArrayList<>(8);
        String eventItem;
        // 状态名字
        String name = null;
        // 每一个状态开始时间 结束时间为lkj.getTime()
        Date startTime = null;
        // 开始索引
        Integer start = null;
        Lkj lkj;
        for (int i = 1; i < list.size(); i++) {
            lkj = list.get(i);
            boolean flag = false;
            eventItem = lkj.getEventItem();
            if ("开车对标".equals(eventItem)) {
                monitorStatus = 1;
                flag = true;
            } else if ("退出调车".equals(eventItem) || "出站".equals(eventItem)) {
                shuntStatus = 2;
                flag = true;
            } else if ("进入调车".equals(eventItem)) {
                shuntStatus = 1;
                flag = true;
            } else if (eventItem.contains("降级")) {
                monitorStatus = 2;
                flag = true;
            }
            if (flag) {
                // 第一条
                if (section.size() == 0) {
                    name = situation(monitorStatus, shuntStatus);
                    startTime = lkj.getTime();
                    // 先记录区间名称和开始时间
                    Situation situation = new Situation();
                    section.add(situation.setSituation(name).setStart(i).setStartTime(lkj.getTime()));
                    continue;
                }
                Situation last = section.get(section.size() - 1);
                // 这一条和上一条相同则记录为一条
                if (name.equals(last.getSituation())) {
                    last.setEnd(i).setEndTime(lkj.getTime());
                } else {
                    Situation situation = new Situation();
                    section.add(situation.setSituation(name).setStart(start).setEnd(i).setStartTime(startTime).setEndTime(lkj.getTime()));
                }
                name = situation(monitorStatus, shuntStatus);
                start = i;
                startTime = lkj.getTime();
            }
        }
        if (section.size() == 0) {
            return section;
        } else {
            Situation last = section.get(section.size() - 1);
            lkj = list.get(list.size() - 1);
            if (name.equals(last.getSituation())) {
                last.setEnd(list.size() - 1).setEndTime(lkj.getTime());
            } else {
                section.add(new Situation(name, start, list.size() - 1, startTime, lkj.getTime()));
            }
            // 只留组合的状态
            return section.stream().filter(situation -> situation.getSituation().length() > 3).collect(Collectors.toList());
        }
    }

    private static String situation(int monitor, int shunt) {
        return monitor(monitor) + shunt(shunt);
    }

    private static String monitor(int i) {
        if (i == 1) {
            return "监控";
        } else if (i == 2) {
            return "降级";
        } else {
            return "";
        }

    }

    private static String shunt(int i) {
        if (i == 1) {
            return "调车";
        } else if (i == 2) {
            return "非调车";
        } else {
            return "";
        }
    }
}
