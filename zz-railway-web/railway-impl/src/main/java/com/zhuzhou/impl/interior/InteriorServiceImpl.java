package com.zhuzhou.impl.interior;

import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.*;
import com.zhuzhou.form.interior.IdxPhaseAnalysisForm;
import com.zhuzhou.form.video.Mp4ListForm;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.interior.InteriorService;
import com.zhuzhou.spi.lkj.LkjIndexService;
import com.zhuzhou.spi.video.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-05-24
 * @Description:
 **/
@Service
@Slf4j
public class InteriorServiceImpl implements InteriorService {

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private Mp4Service mp4Service;

    @Autowired
    private LkjIndexService lkjIndexService;

    @Autowired
    private IdxIndexService idxIndexService;

    @Autowired
    private LkjService lkjService;

    @Autowired
    private RecordStatusService recordStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void analysisIdx(IdxPhaseAnalysisForm form) {
        IdxIndex idxIndex = idxIndexService.getById(form.getId());
        Date startTime = DateUtils.parse(DateUtils.sdf, idxIndex.getStartTime());
        //视频项点
        phaseService.idxAnalysis(form.getList(), form.getRecordId(), startTime);
    }

    @Override
    public void analysisAiPhase (LkjHead lkjHead, String recordId, List<Lkj> list, String url) {
        //获取mp4列表
        Mp4ListForm form = new Mp4ListForm();
        form.setRecordId(recordId);
        List<Mp4> mp4List = mp4Service.list(form);
        //手势分析项点
        lkjService.analysisPhase(recordId, list, url, mp4List, lkjHead);
        //机械间分析
        lkjService.analysisMachine(recordId, list, url, mp4List, lkjHead);
        //前方障碍物分析
        lkjService.analysisFront(recordId, list, url, mp4List, lkjHead);
        //遮挡分析
        lkjService.analysisCover(recordId, list, url, mp4List, lkjHead);
        //新增状态记录
        RecordStatus recordStatus = new RecordStatus();
        recordStatus.setRecordId(recordId);
        recordStatusService.save(recordStatus);
    }
}
