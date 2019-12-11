package com.zhuzhou.spi.interior;

import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.form.interior.IdxPhaseAnalysisForm;
import com.zhuzhou.phase.LkjHead;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-05-24
 * @Description:
 **/
public interface InteriorService {
    /**
     * 分析视频项点
     *
     * @param form
     */
    void analysisIdx(IdxPhaseAnalysisForm form);

    /**
     * 分析手势项点
     * 分析机械间
     * 分析遮挡
     * 分析前方障碍物
     * @param lkjHead
     * @param recordId
     * @param list
     * @param url
     */
    void analysisAiPhase (LkjHead lkjHead, String recordId, List<Lkj> list, String url);
}
