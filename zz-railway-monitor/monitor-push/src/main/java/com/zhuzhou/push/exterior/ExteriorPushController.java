package com.zhuzhou.push.exterior;

import com.zhuzhou.consts.SysStorageConst;
import com.zhuzhou.entity.monitor.MonitorAlarmFile;
import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.entity.monitor.MonitorRealtimeInfo;
import com.zhuzhou.form.exterior.MonitorAlarmReqAddForm;
import com.zhuzhou.form.exterior.MonitorRealTimeAddForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.JsonUtils;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.push.utils.ThreadPoolUtil;
import com.zhuzhou.spi.monitor.MonitorAlarmFileService;
import com.zhuzhou.spi.monitor.MonitorAlarmInfoService;
import com.zhuzhou.spi.monitor.MonitorRealtimeInfoService;
import com.zhuzhou.vo.exterior.MonitorAlarmReq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LiuXinwu
 * Date:     2019/11/27
 * Description: 对外推送接口
 */
@RestController
public class ExteriorPushController {

    private Logger logger = LoggerFactory.getLogger(ExteriorPushController.class);

    @Autowired
    private MonitorAlarmInfoService monitorAlarmInfoService;
    @Autowired
    private MonitorRealtimeInfoService monitorRealtimeInfoService;
    @Autowired
    private MonitorAlarmFileService monitorAlarmFileService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 外部推送-报警推送接口
     * @return
     */
    @PostMapping("/api/pushAlarmData")
    public Integer pushAlarmData(MonitorAlarmReqAddForm form) {
        if (StringUtils.isEmpty(form.getDeviceId())) {
            return 1;
        }
        form.getRt().setDeviceId(form.getDeviceId());

        //接收报警数据
        MonitorAlarmReq monitorAlarmReq = new MonitorAlarmReq();
        ReflectionUtils.copyProperties(monitorAlarmReq, form);

        //放入缓存队列中
        ConcurrentLinkedQueue<MonitorAlarmReq> queue = SysStorageConst.alarmListLocal;
        if (queue.size() >= SysStorageConst.MAX_SIZE) {
            queue.poll();
        }
        queue.offer(monitorAlarmReq);

        //放入待处理的队列中
        SysStorageConst.alarmReqQueue.offer(monitorAlarmReq);
        insertData(SysStorageConst.alarmReqQueue.poll());
        return 0;
    }

    private void insertData(MonitorAlarmReq poll){
        //入库(如果连接容易超时则用线程池去入库)
        if(poll!=null){

            ThreadPoolUtil.execute(()->{
                //报警实时信息,monitor_realtime_info
                MonitorRealtimeInfo rt = poll.getRt();
                monitorRealtimeInfoService.save(rt);

                //入参的基本信息处理,monitor_alarm_info
                MonitorAlarmInfo info = new MonitorAlarmInfo();
                BeanUtils.copyProperties(poll,info);
                info.setTrainCode(rt.getTrainCode());
                //info.setManufactor();
                info.setCcStr(rt.getCcStr());
                //info.setAlarmType();
                //info.setAlarmEvent();
                info.setAlarmTime(rt.getAlarmTime());
                info.setGpsLng(rt.getGpsLng());
                info.setGpsTidu(rt.getGpsTidu());
                //info.setAuditOpinion()
                monitorAlarmInfoService.save(info);

                //报警文件列表信息,monitor_alarm_file
                List<String> alarmFilePaths = poll.getAlarmFilePaths();
                List<MonitorAlarmFile> list = new ArrayList<>();
                for(String path : alarmFilePaths){
                    if(StringUtils.isNotBlank(path)){
                        MonitorAlarmFile f = new MonitorAlarmFile();
                        f.setFilePath(path);
                        f.setDeviceId(poll.getDeviceId());
                        f.setStatus(0);
                        f.setIsDelete(0);
                        //f.setAlarmId()
                        f.setFileType(getFileType(path));//1:视频 2:图片 3:音频
                        list.add(f);
                    }
                }
                monitorAlarmFileService.saveBatch(list);
            });

        }
    }

    private int getFileType(String path){
        int flag = 0;
        if(path.endsWith("mp4")){
            flag = 1;
        }else if(path.endsWith("png")||path.endsWith("jpg")){
            flag = 2;
        }else if(path.endsWith("mp3")){
            flag = 3;
        }
        return flag;
    }

    @PostMapping("/api/delDevice")
    public Result<Object> delDevice () {
//        System.err.println("接收到外部信息设备：" + device.getDeviceId());
        return Result.success().setData("0001");
    }

    @PostMapping("/api/pushRealTimeData")
    public int pushRealTimeData(MonitorRealTimeAddForm form){
        logger.info("pushRealTimeData start");
        MonitorRealtimeInfo realtimeInfo = new MonitorRealtimeInfo();
        BeanUtils.copyProperties(form,realtimeInfo);
        try{
            long current = System.currentTimeMillis();
            realtimeInfo.setCreateTime(new Timestamp(current));
            monitorRealtimeInfoService.save(realtimeInfo);
            //存储到redis
            redisTemplate.opsForValue().set(current+"", JsonUtils.toJson(realtimeInfo));
            redisTemplate.expire(current+"", 24, TimeUnit.HOURS);
        }catch (Exception e){
            logger.error("pushRealTimeData error",e);
            return 1;
        }
        logger.info("pushRealTimeData success");
        return 0;
    }

    @GetMapping("/getRealPush")
    public  Result<Object> getRealPush(){
        long current = System.currentTimeMillis();
        long begin = current - 30*1000;
        //找出符合条件的key
        List<MonitorRealtimeInfo> list = new ArrayList<>();
        for(long i=begin;i<=current;i++){
            String dto = (String) redisTemplate.opsForValue().get(i + "");
            if(StringUtils.isNotBlank(dto)){
                list.add(JsonUtils.fromJson(dto, MonitorRealtimeInfo.class));
            }
        }
        return Result.success().setData(list);
    }


    @GetMapping("/getAlarmPush")
    public  Result<Object> getAlarmPush(){
        ConcurrentLinkedQueue<MonitorAlarmReq> queue = SysStorageConst.alarmListLocal;

        return Result.success().setData(queue);
    }
}