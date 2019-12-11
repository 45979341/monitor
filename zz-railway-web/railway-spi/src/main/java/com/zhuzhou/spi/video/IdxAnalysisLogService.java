package com.zhuzhou.spi.video;

import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.form.video.IdxAnalysisLogAddForm;
import com.zhuzhou.spi.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
public interface IdxAnalysisLogService extends BaseService<IdxAnalysisLog> {

    /**
     * 添加分析日志， 修改违章项点
     * @param idxAnalysisLog
     * @param phaseId
     * @param illegal
     */
    void add(IdxAnalysisLog idxAnalysisLog, String phaseId, Integer illegal);
}
