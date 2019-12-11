package com.zhuzhou.phase.core;

import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.phase.model.PhaseAssist;

import java.util.Date;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-08-21
 * @Description:
 **/
public interface IHandPhaseService {

    /**
     * 处理项点添加
     *
     * @param recordId
     * @param dir
     * @param mp4List
     * @param frontStatus
     * @param start
     * @param lkjTime
     * @param arg1
     * @param arg2
     * @param type        信号机类型
     * @param phase
     */
    void hand(String recordId, String dir, List<Mp4> mp4List, String frontStatus, Date start, Date lkjTime, Date arg1, Date arg2, String type, Phase.PhaseEnum phase);
}
