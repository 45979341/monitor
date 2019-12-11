package com.zhuzhou.impl.video;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.dao.video.UploadRecordMapper;
import com.zhuzhou.dto.video.IdxAnalyzeResult;
import com.zhuzhou.dto.video.LkjAnalyzeResult;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.entity.video.*;
import com.zhuzhou.enums.video.FileSufEnum;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.framework.utils.v2.HttpUtils;
import com.zhuzhou.framework.utils.v2.ResponseWrap;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.config.ConfigStationService;
import com.zhuzhou.spi.config.ConfigStewardService;
import com.zhuzhou.spi.video.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
@Slf4j
@Service
public class UploadRecordServiceImpl extends BaseServiceImpl<UploadRecordMapper, UploadRecord> implements UploadRecordService {
    @Autowired
    private UploadRecordMapper uploadRecordMapper;
    @Autowired
    private IdxIndexService idxIndexService;
    @Autowired
    private Mp4Service mp4Service;
    @Autowired
    private LkjService lkjService;
    @Autowired
    private PhaseService phaseService;
    @Autowired
    private RecordStatusService recordStatusService;
    @Autowired
    private ConfigStationService configStationService;
    @Autowired
    private ConfigStewardService configStewardService;

    @Value("${file.uri}")
    private String uri;

    @Value("${file.source}")
    private String source;

    @Value("${url.idx}")
    private String IDX_URL;

    @Value("${url.lkj}")
    private String LKJ_URL;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void upload(MultipartFile[] files) {

        //将上传文件整理出来
        String idxFile = null;
        List<String> mp4Files = new ArrayList<>();
        String lkjFile = null;
        String dir = null;
        for (MultipartFile f : files) {
            //传输文件
            File dest = new File(source + File.separator + f.getOriginalFilename());
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                //上传文件传输到本地
                f.transferTo(dest);
                log.info(source + File.separator + f.getOriginalFilename() + " 文件传输成功!");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("文件传输失败", e);
            }
            //目录
            dir = f.getOriginalFilename().substring(0, f.getOriginalFilename().lastIndexOf("/"));
            String fileName = f.getOriginalFilename().substring(f.getOriginalFilename().lastIndexOf("/") + 1);
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
            if (FileSufEnum.IDX.getSuf().equals(suffixName)) {
                idxFile = fileName;
            } else if (FileSufEnum.MP4.getSuf().equals(suffixName)) {
                mp4Files.add(prefixName);
//                fileExecutorService.exec(f.getOriginalFilename());
            } else {
                lkjFile = fileName;
            }
        }

        String fileDir = uri + "/source/" + dir + "/";
        //转换MP4列表
        List<Mp4> mp4List = mp4Files.stream().filter(f -> {
            String[] under = f.split(SymbolConst.UNDER);
            if (under.length <= 1) {
                under = f.split(SymbolConst.RAIL);
            }
            return under.length > 1;
        }).map(m -> {
            String[] under = m.split(SymbolConst.UNDER);
            Mp4 mp4 = new Mp4().setCabNum(under[0]).setFactoryName(under[1]).setChannelNum(under[2])
                    .setChannelName(under[3]).setPrefixDate(under[4]).setSuffixDate(under[5]);
            return mp4;
        }).collect(Collectors.toList());

        Map<String, List<Mp4>> collect = mp4List.stream().collect(Collectors.toMap(Mp4::getChannelNum,
                v -> {
                    List<Mp4> objects = new ArrayList<>();
                    objects.add(v);
                    return objects;
                }, (List<Mp4> newValueList, List<Mp4> oldValueList) -> {
                    oldValueList.addAll(newValueList);
                    return oldValueList;
                }
        ));
        String startTime = mp4List.stream().min(Comparator.comparing(Mp4::getSuffixDate)).get().getSuffixDate();
        String endTime = mp4List.stream().max(Comparator.comparing(Mp4::getSuffixDate)).get().getSuffixDate();
        Mp4 mp4 = mp4List.get(0);
        UploadRecord.Builder builder = new UploadRecord.Builder().create()
                .setStartTime(startTime).setEndTime(endTime).setDate(mp4.getPrefixDate());

        builder.setDir(fileDir);

        //idx,lkj文件路径
        builder.setIdxFile(idxFile);
        builder.setLkjFile(lkjFile);

        //获取idx数据
        ResponseWrap idxWrap = HttpUtils.getInstance().get(IDX_URL)
                .addParameter("url", fileDir + idxFile).execute();
        IdxAnalyzeResult idxResult = idxWrap.getJson(IdxAnalyzeResult.class);
        List<Idx> idxList = idxResult.getData();

        String bStart = builder.getDate() + SymbolConst.BLANK + builder.getStartTime();
        String bEnd = builder.getDate() + SymbolConst.BLANK + builder.getEndTime();
        DateTimeFormatter format = DateTimeFormat.forPattern(DateUtils.ymdstr);
        long start = DateTime.parse(bStart, format).toDate().getTime();
        long end = DateTime.parse(bEnd, format).toDate().getTime();

        //存取可用车次
        Map<String, List<Idx>> map = new HashMap<>(4);
        for (Idx i : idxList) {
            long platformTime = i.getPlatformTime().getTime();
            //过滤无用时间段数据
            if (!(platformTime > start && platformTime < end)) {
                continue;
            }
            String trainNum;
            if (StringUtils.isEmpty(i.getAlpTrainNum())) {
                trainNum = i.getDigTrainNum();
            } else {
                trainNum = i.getAlpTrainNum() + i.getDigTrainNum();
            }
            List<Idx> list = map.get(trainNum);
            if (CollectionUtils.isEmpty(list)) {
                list = new ArrayList<>();
            }

            Map<String, ConfigStation> detail = configStationService.listToMap();
            ConfigStation configStation = detail.get(i.getCrossRoadNum() + SymbolConst.UNDER + i.getStationNum());
            Optional.ofNullable(configStation).ifPresent(c -> {
                i.setStationName(c.getStationName());
            });
            list.add(i);
            map.put(trainNum, list);
        }

        List<UploadRecord> uploadRecords = new ArrayList<>();
        //上传记录，多个车次
        for (Map.Entry<String, List<Idx>> m : map.entrySet()) {
            //设置机车号
            builder.setCabNum(mp4.getCabNum()).setFileNum(files.length);
            List<Idx> data = m.getValue();
            Date max = data.stream().max(Comparator.comparing(Idx::getPlatformTime)).get().getPlatformTime();
            Date min = data.stream().min(Comparator.comparing(Idx::getPlatformTime)).get().getPlatformTime();
            builder.setStartTime(min);
            builder.setEndTime(max);
            builder.setTrainNum(m.getKey());
            Idx idx = data.get(0);
            builder.setDriverNum(idx.getDriverNum());
            ConfigSteward configSteward = new ConfigSteward();
            configSteward.setJobNum(Integer.parseInt(idx.getDriverNum()));
            ConfigSteward one = configStewardService.getOne(configSteward);
            if (one != null) {
                builder.setDriverName(one.getName());
            } else {
                builder.setDriverName(idx.getDriverNum());
            }
            uploadRecords.add(builder.build());

            //新增上传记录
            UploadRecord uploadRecord = builder.build();
            save(uploadRecord);
            log.info("新增上传记录成功！record_id:{}", uploadRecord.getId());

            //新增状态记录
            RecordStatus recordStatus = new RecordStatus();
            recordStatus.setRecordId(uploadRecord.getId());
            recordStatusService.save(recordStatus);
            log.info("新增状态机成功！record_id:{}", uploadRecord.getId());

            //相点分析
            phaseService.idxAnalysis(data, uploadRecord.getId(), null);

            //新增idx
            data = data.stream().map(p -> p.setRecordId(uploadRecord.getId())).collect(Collectors.toList());
//            idxIndexService.saveBatch(data);
            log.info("新增idx成功！record_id:{}", uploadRecord.getId());

            //新增视频记录
            mp4List = mp4List.stream().filter(f -> {
                long parse = DateUtils.parse(DateUtils.ymdhms, f.getPrefixDate() + SymbolConst.BLANK + f.getSuffixDate()).getTime();
                if (end >= parse && start <= parse) {
                    return true;
                }
                return false;
            }).map(p -> p.setRecordId(uploadRecord.getId())).collect(Collectors.toList());
            mp4Service.saveBatch(mp4List);
            log.info("新增视频记录成功！record_id:{}", uploadRecord.getId());

            ResponseWrap lkjWrap = HttpUtils.getInstance().get(LKJ_URL)
                    .addParameter("url", fileDir + lkjFile).execute();
            LkjAnalyzeResult lkjResult = lkjWrap.getJson(LkjAnalyzeResult.class);
            List<Lkj> datas = lkjResult.getData();

            datas = datas.stream().map(p -> p.setRecordId(uploadRecord.getId())).collect(Collectors.toList());
            lkjService.saveBatch(datas);
            log.info("新增lkj数据成功！record_id:{}", uploadRecord.getId());

//            lkjService.analysisPhase(uploadRecord.getId(), datas, fileDir, mp4List);
            log.info("手势相点分析成功！record_id:{}", uploadRecord.getId());
        }

        log.info("文件处理完成！");
    }
}
