package com.zhuzhou.impl.video;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.dao.video.IdxAnalysisLogMapper;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.spi.video.IdxAnalysisLogService;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.video.PhaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
@Service
public class IdxAnalysisLogServiceImpl extends BaseServiceImpl<IdxAnalysisLogMapper, IdxAnalysisLog> implements IdxAnalysisLogService {
    @Autowired
    private PhaseService phaseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(IdxAnalysisLog idxAnalysisLog, String phaseId, Integer illegal) {
        //添加日志分析
        save(idxAnalysisLog);
        //修改违章项点
        if (ObjectUtils.isNotEmpty(phaseId) && ObjectUtils.isNotEmpty(illegal)) {
            Phase phase = new Phase();
            phase.setId(phaseId);
            phase.setIllegal(illegal);
            phaseService.updateById(phase);
        }
    }
}
