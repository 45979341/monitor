//package com.zhuzhou.manager.api.exterior;
//
//import com.zhuzhou.consts.SysStorageConst;
//import com.zhuzhou.form.exterior.MonitorAlarmReqAddForm;
//import com.zhuzhou.framework.result.Result;
//import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
//import com.zhuzhou.framework.utils.stomp.StringUtils;
//import com.zhuzhou.spi.monitor.MonitorAlarmFileService;
//import com.zhuzhou.spi.monitor.MonitorAlarmInfoService;
//import com.zhuzhou.spi.monitor.MonitorRealtimeInfoService;
//import com.zhuzhou.vo.exterior.MonitorAlarmReq;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.concurrent.ConcurrentLinkedQueue;
//
///**
// * @Author: chenzeting
// * Date:     2019/4/24
// * Description: 广义推送机车信息
// */
//@Controller
//@Slf4j
//public class ExteriorPushController {
//
//    @Autowired
//    private MonitorAlarmInfoService monitorAlarmInfoService;
//    @Autowired
//    private MonitorRealtimeInfoService monitorRealtimeInfoService;
//    @Autowired
//    private MonitorAlarmFileService monitorAlarmFileService;
//
//    /**
//     * 外部推送-报警推送接口
//     * @return
//     */
//    @PostMapping("/api/pushAlarmData2")
//    @ResponseBody
//    public Integer pushAlarmData(MonitorAlarmReqAddForm form) {
//        if (StringUtils.isEmpty(form.getDeviceId())) {
//            return 1;
//        }
//        MonitorAlarmReq monitorAlarmReq = new MonitorAlarmReq();
//        ReflectionUtils.copyProperties(monitorAlarmReq, form);
//
//        ConcurrentLinkedQueue<MonitorAlarmReq> queue = SysStorageConst.alarmListLocal;
//        if (queue.size() >= SysStorageConst.MAX_SIZE) {
//            queue.poll();
//        }
//        queue.offer(monitorAlarmReq);
//        SysStorageConst.alarmReqQueue.offer(monitorAlarmReq);
////        MyWebSocket.sendInfo("pushAlarmData");
//        return 0;
//    }
//
//    @PostMapping("/api/delDevice")
//    public Result<Object> delDevice () {
////        System.err.println("接收到外部信息设备：" + device.getDeviceId());
//        return Result.success().setData("0001");
//    }
//}