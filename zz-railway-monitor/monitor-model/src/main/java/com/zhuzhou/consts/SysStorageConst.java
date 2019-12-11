package com.zhuzhou.consts;

import com.zhuzhou.vo.exterior.MonitorAlarmReq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author chenzeting
 * @Date 2019-06-17
 * @Description:
 **/
public class SysStorageConst {

    public static final int MAX_SIZE = 100;

    public static Map<String, Integer> deviceStatusMap = new HashMap();

    /**
     * 缓存历史数据100条
     */
    public static ConcurrentLinkedQueue<MonitorAlarmReq> alarmListLocal = new ConcurrentLinkedQueue<>();

    /**
     * 队列接收数据，针对报警推送接口
     */
    public static ConcurrentLinkedQueue<MonitorAlarmReq> alarmReqQueue = new ConcurrentLinkedQueue();

//    public static Map<String, ArrayList<RealtimeReq>> deviceRealTimeLimitQueue = new ConcurrentHashMap();
//
//    public static ConcurrentLinkedQueue<RealtimeReq> realtimeReqQueue = new ConcurrentLinkedQueue();
}
