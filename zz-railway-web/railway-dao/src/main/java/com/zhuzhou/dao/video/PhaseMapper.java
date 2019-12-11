package com.zhuzhou.dao.video;

import com.zhuzhou.entity.statistic.PhaseAnalysis;
import com.zhuzhou.entity.video.Phase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuzhou.form.video.PhaseListForm;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-27
 */
@Repository
public interface PhaseMapper extends BaseMapper<Phase> {

    /**
     * 获取扣除违章分数
     * @param recordIds
     * @return
     */
    @MapKey("recordId")
    Map<String, PhaseAnalysis> findIllegalScore (List<String> recordIds);

    /**
     * 查询项点列表
     * @param form
     * @return
     */
    List<Phase> findList(PhaseListForm form);
}
