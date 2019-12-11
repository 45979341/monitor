package com.zhuzhou.impl.video;

import com.zhuzhou.dao.video.PhaseMapper;
import com.zhuzhou.entity.statistic.PhaseAnalysis;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.idx.CabSignalEnum;
import com.zhuzhou.enums.idx.DeviceStatusEnum;
import com.zhuzhou.enums.idx.MessageTypeEnum;
import com.zhuzhou.form.video.PhaseListForm;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.video.PhaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-27
 */
@Service
@Slf4j
public class PhaseServiceImpl extends BaseServiceImpl<PhaseMapper, Phase> implements PhaseService {

    @Autowired
    private PhaseMapper phaseMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Phase add(String recordId, Date beginTime, Date startTime, Date endTime, String beginFile, String file, String type, Phase.PhaseEnum enums) {
        Phase phase = add(recordId, beginTime, startTime, endTime, beginFile, file, type, enums, 1);
        return phase;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Phase add(String recordId, Date beginTime, Date startTime, Date endTime, String beginFile, String file, String type, Phase.PhaseEnum enums, int illegal) {
        Phase phase = new Phase()
                .setRecordId(recordId)
                .setPhaseId(enums.getPhaseId())
                .setCode(enums.getCode())
                .setName(enums.getName())
                .setType(type)
                .setBeginTime(beginTime)
                .setBeginFile(beginFile)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setFile(file)
                .setIllegal(illegal);
        save(phase);
        return phase;
    }

    @Override
    public Phase add(String recordId, Date startTime, Date endTime, Phase.PhaseEnum enums) {
        Phase phase = add(recordId, null, startTime, endTime, null, null, null, enums, 3);
        return phase;
    }

    @Override
    public Phase add(String recordId, Date startTime, Date endTime, Phase.PhaseEnum enums, int illegal) {
        Phase phase = add(recordId, null, startTime, endTime, null, null, null, enums, illegal);
        return phase;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void idxAnalysis(List<Idx> list, String recordId, Date startTime) {
        log.info("idx项点分析开始！id：{}", recordId);
        for (int i = 0; i < list.size();i++) {
            if (i == list.size() - 1) {
                break;
            }
            // LKJ装置开车状态下，机车由运行状态至静止状态，当速度等于0时，装置记录该时点前后若干秒，作为停车事件
            Idx idx = list.get(i);
            String deviceStatus = idx.getDeviceStatus();
            int speed = idx.getSpeed();
            //速度大于0，且为降级调车，置为停车事件
            if (speed > 0 && DeviceStatusEnum.DS_11.getCode().equals(deviceStatus)) {
                Idx temp = list.get(i + 1);
                if (temp.getSpeed() == 0) {
                    //停车事件
                    add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -120),
                            DateUtils.opSecond(temp.getPlatformTime(), 120), Phase.PhaseEnum.ENV_7);
                    i += 1;
                }
            }
        }

        for (int i = 0; i < list.size();i++) {
            if (i == list.size() - 1) {
                break;
            }
            Idx idx = list.get(i);
            int speed = idx.getSpeed();
            //信号灯
            String cabSignal = idx.getCabSignal();
            int tpp = idx.getTrainPipePressure();
            Date platformTime = idx.getPlatformTime();
            String messageType = idx.getMessageType();
            String deviceStatus = idx.getDeviceStatus();
            String driverNum = idx.getDriverNum();

            //跳过的数目
            int tmax = 0;

            //继承交接
            Idx tempIdx = list.get(i + 1);
            //在LKJ输入司机代码时记录交接班情况.(同台机车同时间段司机代码发生变化)
            if (!driverNum.equals(tempIdx.getDriverNum())) {
                Date platform = DateUtils.opSecond(idx.getPlatformTime(), -60);
                if (startTime.getTime() > platform.getTime()) {
                    platform = startTime;
                }
                //继乘交接
                //事件记录时段为，输入司机代码前60秒，后300（120）秒
                add(recordId, platform,
                        DateUtils.opSecond(idx.getPlatformTime(), 120), Phase.PhaseEnum.ENV_K);
                i++;
                break;
            }

            //开车状态
            if(DeviceStatusEnum.DS_00.getCode().equals(deviceStatus)) {
                // LKJ装置开车状态下,机车由静止状态开始运行，速度大于0，当速度达到3公里/小时，装置记录该时点前后若干秒，作为开车事件
                if (speed == 0) {
                    boolean flag = false;
                    Idx temp;
                    int t = 1;
                    do {
                        temp = list.get(i + t);
                        int tempSpeed = temp.getSpeed();
                        if (tempSpeed == 0){
                            ++t;
                            continue;
                        }
                        if (tempSpeed >= 3) {
                            flag = true;
                            break;
                        }
                        ++t;
                    } while (list.size() > i + t + 1);
                    if (flag) {
                        //开车事件
                        //事件记录时段为，速度达到3公里时（开车前120S后120S）
                        add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -120),
                                DateUtils.opSecond(temp.getPlatformTime(), 120), Phase.PhaseEnum.ENV_8);
                        if (tmax < t) {
                            tmax = t;
                        }
//                        i += t;
                    }
                }

                //LKJ装置开车状态下，机车在双黄灯信号下运行的区段，作为侧线运行事件
                if (CabSignalEnum.D_YELLOW.getCode().equals(cabSignal)) {
                    Idx temp;
                    int t = 1;
                    boolean flag = false;
                    do {
                        temp = list.get(i + t);
                        if (!CabSignalEnum.D_YELLOW.getCode().equals(temp.getCabSignal())) {
                            flag = true;
                            break;
                        }
                        ++t;
                    } while (list.size() > i + t + 1);
                    if (flag) {
                        //侧线运行
                        //事件记录时段为，速度达到3公里时（开车前120S后120S）
                        add(recordId, platformTime, temp.getPlatformTime(), Phase.PhaseEnum.ENV_9);
                        if (tmax < t) {
                            tmax = t;
                        }
//                        i += t;
                    }
                }

                //F3类型机
                if (MessageTypeEnum.TYPE_F3.getType().equals(messageType)) {
                    //监控动作
                    //开关视频
                    //内燃停机
                    //打盹报警
                }

                //LKJ装置开车状态下，机车收到红黄灯，装置记录该时点前后若干秒，作为红黄灯事件。
                if (CabSignalEnum.RED_YELLOW.getCode().equals(cabSignal)) {
                    int t = 1;
                    Idx temp;
                    boolean flag = false;
                    do {
                        temp = list.get(i + t);
                        String tempCabSignal = temp.getCabSignal();
                        if (!tempCabSignal.equals(cabSignal)) {
                            flag = true;
                            break;
                        }
                        ++t;
                    } while(list.size() > i + t + 1);
                    if (flag) {
                        //红黄信号
                        //事件记录时段为，收到红黄灯前60秒，后180秒。
                        add(recordId, DateUtils.opSecond(platformTime, -60),
                                DateUtils.opSecond(platformTime, 180), Phase.PhaseEnum.ENV_G);
                        if (tmax < t) {
                            tmax = t;
                        }
//                        i += t;
                    }
                }
            }

            //监控状态
            if (DeviceStatusEnum.DS_00.getCode().equals(deviceStatus) || DeviceStatusEnum.DS_01.getCode().equals(deviceStatus)) {
                // 监控状态下，收到绿灯转黄灯或者收到黄灯信号
                // 事件记录时段为，收到绿灯转黄灯或者收到黄灯信号前60秒，后300秒。
                if (CabSignalEnum.YELLOW.getCode().equals(cabSignal)) {
                    int t = 1;
                    Idx temp;
                    boolean flag = false;
                    do {
                        temp = list.get(i + t);
                        String tempCabSignal = temp.getCabSignal();
                        if (!CabSignalEnum.YELLOW.getCode().equals(tempCabSignal)) {
                            flag = true;
                            break;
                        }
                        ++t;
                    } while(list.size() > i + t + 1);
                    if (flag) {
                        //黄灯信号
                        //事件记录时段为，收到绿灯转黄灯或者收到黄灯信号前60秒，后300秒。
                        add(recordId, DateUtils.opSecond(platformTime, -60),
                                DateUtils.opSecond(platformTime, 300), Phase.PhaseEnum.ENV_O);
                        if (tmax < t) {
                            tmax = t;
                        }
//                        i += t;
                    }
                }
            }

            if (speed > 0) {
                //机车速度非零时，司机人为紧急制动，致使列车管压急骤下降，下降一定值时，装置记录该时点前后若干秒，作为人为紧急事件
                for (int k = 1;k <= 5;k++) {
                    if (i + k > list.size() - 1) {
                       break;
                    }
                    Idx temp = list.get(i + k);
                    if (DateUtils.diffDate(platformTime, temp.getPlatformTime(), 5)) {
                        if (temp.getTrainPipePressure() - tpp >= 300) {
                            //人为紧急
                            // 事件记录时段为，5秒内管压骤降达300KPA以上，该时点前60秒，后300秒
                            add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -60),
                                    DateUtils.opSecond(temp.getPlatformTime(), 300), Phase.PhaseEnum.ENV_A);
                            if (tmax < k) {
                                tmax = k;
                            }
//                            i += k;
                            break;
                        }
                        //机车速度非零时，司机人为减压制动，致使列车管压下降，下降一定值时，装置记录该时点前后若干秒，作为减压制动事件；
                        if (temp.getTrainPipePressure() - tpp >= 50) {
                            //减压制动
                            // 事件记录时段为，5秒内管压降达50KPA以上，该时点前60秒，后300秒
                            add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -60),
                                    DateUtils.opSecond(temp.getPlatformTime(), 300), Phase.PhaseEnum.ENV_R);
                            if (tmax < k) {
                                tmax = k;
                            }
//                            i += k;
                            break;
                        }
                    }
                }
            }

            // 机车静止状态下
            if (speed == 0) {
                Idx temp = list.get(i + 1);
                String tempDs = temp.getDeviceStatus();
                //调车状态
                if (DeviceStatusEnum.DS_01.getCode().equals(deviceStatus)
                        || DeviceStatusEnum.DS_11.getCode().equals(deviceStatus)) {
                    //机车静止状态下，LKJ装置由调车进入非调车状态时
                    if (DeviceStatusEnum.DS_00.getCode().equals(tempDs)
                            || DeviceStatusEnum.DS_10.getCode().equals(tempDs)) {
                        //退出调车
                        //事件记录时段为，收到LKJ装置退出调车状态信息前60秒，后600（120）秒
                        add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -60),
                                DateUtils.opSecond(temp.getPlatformTime(), 120), Phase.PhaseEnum.ENV_C);
                    }
                } else {
                    //机车静止状态下，LKJ装置由非调车进入调车状态时
                    if (DeviceStatusEnum.DS_01.getCode().equals(deviceStatus)
                            || DeviceStatusEnum.DS_11.getCode().equals(deviceStatus)) {
                        //进入调车
                        //事件记录时段为，收到LKJ装置进入调车状态信息前60秒，后120秒
                        add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -60),
                                DateUtils.opSecond(temp.getPlatformTime(), 120), Phase.PhaseEnum.ENV_B);
                    }
                }
            }

            //调车状态
            if (DeviceStatusEnum.DS_01.getCode().equals(deviceStatus)
                    || DeviceStatusEnum.DS_11.getCode().equals(deviceStatus)) {
                //LKJ装置调车状态下,机车由静止状态开始运行，机车加载，速度大于0，
                // 当速度达到3公里/小时，装置记录该时点前后若干秒，作为调车开行事件。
                if (speed == 0) {
                    boolean flag = false;
                    Idx temp;
                    int t = 1;
                    do {
                        temp = list.get(i + t);
                        if (temp.getSpeed() == 0){
                            break;
                        }
                        if (temp.getSpeed() >= 3) {
                            flag = true;
                            break;
                        }
                        ++t;
                    } while (list.size() > i + t + 1);
                    if (flag) {
                        //调车开行
                        //事件记录时段为，速度达到3公里时前60（120）秒，后600（120）秒。
                        add(recordId, DateUtils.opSecond(temp.getPlatformTime(), -120),
                                DateUtils.opSecond(temp.getPlatformTime(), 120), Phase.PhaseEnum.ENV_F);
                        if (tmax < t) {
                            tmax = t;
                        }
//                        i += t;
                    }
                }
            }
            i += tmax;
        }
        log.info("idx项点分析结束！id：{}", recordId);
    }

    @Override
    public Map<String, PhaseAnalysis> findIllegalScore(List<String> recordIds) {
        return phaseMapper.findIllegalScore(recordIds);
    }

    @Override
    public List<Phase> findList(PhaseListForm form) {
        return phaseMapper.findList(form);
    }
}
