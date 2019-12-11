package scheduled;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.consts.SysStorageConst;
import com.zhuzhou.entity.monitor.MonitorAlarmFile;
import com.zhuzhou.entity.monitor.MonitorAlarmInfo;
import com.zhuzhou.entity.monitor.MonitorRealtimeInfo;
import com.zhuzhou.enums.exterior.AlarmTypeEnum;
import com.zhuzhou.enums.exterior.AuditStatusEnum;
import com.zhuzhou.enums.exterior.FileTypeEnum;
import com.zhuzhou.enums.exterior.StatusEnum;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.spi.monitor.MonitorAlarmFileService;
import com.zhuzhou.spi.monitor.MonitorAlarmInfoService;
import com.zhuzhou.spi.monitor.MonitorRealtimeInfoService;
import com.zhuzhou.vo.exterior.MonitorAlarmReq;
import com.zhuzhou.vo.exterior.RealtimeReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author chenzeting
 * @Date 2019-06-18
 * @Description:
 **/
@Component
@Slf4j
public class StorageDBSchedul {

    /**
     * 并发处理
     */
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private MonitorAlarmInfoService monitorAlarmInfoService;
    @Autowired
    private MonitorRealtimeInfoService monitorRealtimeInfoService;
    @Autowired
    private MonitorAlarmFileService monitorAlarmFileService;

    /**
     * 同步报警数据至数据库
     */
//    @Scheduled(cron = "0/20 * * * * ?")
    public void execAlarm() {
        this.lock.lock();
        try {
            ConcurrentLinkedQueue<MonitorAlarmReq> alarmReqQueue = SysStorageConst.alarmReqQueue;
            if (alarmReqQueue.isEmpty()) {
                return;
            }
            List<MonitorAlarmInfo> alarmInfoList = new ArrayList();
            List<MonitorAlarmFile> alarmFiles = new ArrayList();
            List<MonitorRealtimeInfo> realtimeInfos = new ArrayList();
            for (int i = 0; i < 5000; i++) {
                MonitorAlarmReq monitorAlarmReq = alarmReqQueue.poll();
                AlarmTypeEnum alarmTypeEnum = AlarmTypeEnum.getById(monitorAlarmReq.getItemType());

                Integer maxAlarmId = this.monitorAlarmInfoService.selectMaxId();
                if (alarmTypeEnum == null) {
                    log.error("项点未找到,对应项点:{}", monitorAlarmReq.getItemType());
                } else {
                    MonitorAlarmInfo tempAlarm = new MonitorAlarmInfo();
                    tempAlarm.setId(String.valueOf(maxAlarmId));
                    tempAlarm.setDeviceId(monitorAlarmReq.getDeviceId());

                    RealtimeReq rt = monitorAlarmReq.getRt();
                    MonitorRealtimeInfo tempRt = new MonitorRealtimeInfo();
                    ReflectionUtils.copyProperties(rt, tempRt);

                    tempAlarm.setTrainCode(rt.getTrainStr());
                    Optional.ofNullable(tempRt).ifPresent(p -> {
                        tempAlarm.setCcStr(p.getCcStr());
                        tempAlarm.setDriverId(p.getDriverId());
                        tempAlarm.setViceDriverId(p.getViceDriverId());
                        tempAlarm.setAlarmTime(p.getAlarmTime());
                        tempAlarm.setGpsLng(p.getGpsLng());
                        tempAlarm.setGpsTidu(p.getGpsTidu());
                        MonitorRealtimeInfo temp = new MonitorRealtimeInfo();
                        ReflectionUtils.copyProperties(temp, p);
                        realtimeInfos.add(temp);
                    });
                    tempAlarm.setAlarmType(alarmTypeEnum.getTypeId());
                    tempAlarm.setAlarmEvent(alarmTypeEnum.getId());

                    ReflectionUtils.copyProperties(monitorAlarmReq, tempAlarm);
//                    tempAlarm.setWorkCondition(monitorAlarmReq.getWorkCondition());
//                    tempAlarm.setAlarmChn(monitorAlarmReq.getAlarmChn());
//                    tempAlarm.setStartTime(monitorAlarmReq.getStartTime());
//                    tempAlarm.setEndTime(monitorAlarmReq.getEndTime());
//                    tempAlarm.setAnalyzeStatus(monitorAlarmReq.getAnalyzeStatus());
                    tempAlarm.setAuditStatus(AuditStatusEnum.UN_AUDIT.getId());
                    tempAlarm.setStatus(StatusEnum.NORMAL.getId());
                    tempAlarm.setIsDelete(StatusEnum.NORMAL.getId());
                    alarmInfoList.add(tempAlarm);

                    List<String> filePaths = monitorAlarmReq.getAlarmFilePaths();
                    if (CollectionUtils.isNotEmpty(filePaths)) {
                        for (String file : filePaths) {
                            MonitorAlarmFile alarmFile = new MonitorAlarmFile();
                            alarmFile.setAlarmId(String.valueOf(maxAlarmId));
                            alarmFile.setDeviceId(monitorAlarmReq.getDeviceId());
                            alarmFile.setFilePath(file);
                            FileTypeEnum fileType = FileTypeEnum.getFileType(FilenameUtils.getExtension(file));
                            Integer fileTypeId = 0;
                            if (Optional.ofNullable(fileType).isPresent()) {
                                fileTypeId = fileType.getId();
                            }
                            alarmFile.setFileType(fileTypeId);
                            alarmFile.setIsDelete(StatusEnum.NORMAL.getId());
                            alarmFile.setStatus(StatusEnum.NORMAL.getId());
                            alarmFiles.add(alarmFile);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(alarmInfoList)) {
                monitorAlarmInfoService.saveBatch(alarmInfoList);
            }
            if (CollectionUtils.isNotEmpty(alarmFiles)) {
                monitorAlarmFileService.saveBatch(alarmFiles);
            }
            if (CollectionUtils.isNotEmpty(alarmFiles)) {
                monitorRealtimeInfoService.saveBatch(realtimeInfos);
            }
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 同步实时信息至数据库
     */
    @Scheduled(cron = "0/5 * * * * ? ")
    public void execRealTime() {
//        ConcurrentLinkedQueue<RealtimeReq> realtimeReqQueue = SystemCache.realtimeReqQueue;
//
//        List<RealtimeInfo> realtimeInfos = new ArrayList();
//        Map<String, DeviceLocation> locationMap = new HashMap();
//        Map<String, DeviceLocationLog> locationLogMap = new HashMap();
//
//        StringBuffer keySbf = new StringBuffer();
//        String keyAppend = "&&";
//        String dateFormat = "yyyyMMddHHmm";
//        for (int i = 0; i < 20000; i++) {
//            if (realtimeReqQueue.isEmpty()) {
//                break;
//            }
//            RealtimeReq realtimeReq = (RealtimeReq) realtimeReqQueue.poll();
//            RealtimeInfo realtimeInfo = new RealtimeInfo();
//            BeanUtil.copyRealtimeInfo(realtimeReq, realtimeInfo);
//            realtimeInfos.add(realtimeInfo);
//
//            if ((StringUtils.isNotNull(realtimeInfo.getGpsLng())) && (StringUtils.isNotEmpty(realtimeInfo.getDeviceId()))) {
//                DeviceLocation deviceLocation = new DeviceLocation();
//                BeanUtil.copyRealtimeInfo(realtimeInfo, deviceLocation);
//                locationMap.put(realtimeInfo.getDeviceId(), deviceLocation);
//
//                String dateStr = DateUtil.format(realtimeInfo.getAlarmTime(), dateFormat);
//                keySbf.setLength(0);
//                keySbf.append(realtimeInfo.getDeviceId());
//                keySbf.append(keyAppend);
//                keySbf.append(dateStr);
//                DeviceLocationLog deviceLocationLog = new DeviceLocationLog();
//                BeanUtil.copyRealtimeInfo(realtimeInfo, deviceLocationLog);
//                deviceLocationLog.setTimeUnique(dateStr);
//                locationLogMap.put(keySbf.toString(), deviceLocationLog);
//            }
//        }
//
//        if (StringUtils.isNotEmpty(realtimeInfos)) {
//            insertBatch(realtimeInfos);
//        }
//        if (StringUtils.isNotEmpty(locationMap)) {
//            this.systemManualMapper.insertLocationBatch(locationMap.values());
//        }
//        if (StringUtils.isNotEmpty(locationLogMap)) {
//            List<DeviceLocationLog> locationLogs = new ArrayList();
//            locationLogs.addAll(locationLogMap.values());
//            this.systemManualMapper.insertLocationLogBatch(locationLogs);
//        }
    }
}
