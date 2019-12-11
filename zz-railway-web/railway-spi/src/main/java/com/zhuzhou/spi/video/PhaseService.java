package com.zhuzhou.spi.video;

import com.zhuzhou.entity.statistic.PhaseAnalysis;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.form.video.PhaseListForm;
import com.zhuzhou.spi.BaseService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-27
 */
public interface PhaseService extends BaseService<Phase> {

    /**
     * 初始化添加相点
     * @param recordId
     * @param beginTime
     * @param startTime
     * @param endTime
     * @param beginFile
     * @param file
     * @param enums
     */
    Phase add(String recordId, Date beginTime, Date startTime, Date endTime, String beginFile, String file, String type, Phase.PhaseEnum enums);

    /**
     * 重载 初始化添加相点
     * @param recordId
     * @param beginTime
     * @param startTime
     * @param endTime
     * @param beginFile
     * @param file
     * @param enums
     * @param illegal 违章， {@link Phase#getIllegal()}
     * @return
     */
    Phase add(String recordId, Date beginTime, Date startTime, Date endTime, String beginFile, String file, String type, Phase.PhaseEnum enums, int illegal);

    /**
     *
     * @param recordId
     * @param startTime
     * @param endTime
     * @param enums
     */
    Phase add(String recordId, Date startTime, Date endTime, Phase.PhaseEnum enums);

    /**
     * 重载 初始化添加相点
     * @param recordId
     * @param startTime
     * @param endTime
     * @param enums
     * @param illegal 违章， {@link Phase#getIllegal()}
     * @return
     */
    Phase add(String recordId, Date startTime, Date endTime, Phase.PhaseEnum enums, int illegal);

    /**
     * idx 相点解析
     * @param data
     * @param recordId  idx 关联lkj ,RecordId
     * @param startTime
     */
    void idxAnalysis(List<Idx> data, String recordId, Date startTime);

    /**
     * 获取扣除违章分数
     * @param recordIds
     * @return
     */
    Map<String, PhaseAnalysis> findIllegalScore (List<String> recordIds);

    /**
     * 查询项点列表
     * @param form
     * @return
     */
    List<Phase> findList(PhaseListForm form);
}
