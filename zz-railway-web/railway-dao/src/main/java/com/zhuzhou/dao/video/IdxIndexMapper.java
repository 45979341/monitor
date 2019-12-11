package com.zhuzhou.dao.video;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.form.lkj.LkjIndexListForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
@Repository
public interface IdxIndexMapper extends BaseMapper<IdxIndex> {

    /**
     * 获取无lkj文件的idx_index，可做查看视频入口
     * @return
     * @param idxIndex
     * @param offset
     * @param size
     */
    List<IdxIndex> idxIndexNone(@Param("i") IdxIndex idxIndex, @Param("offset") long offset, @Param("size") long size);

    /**
     * 获取无lkj文件的idx_index个数
     * @param idxIndex
     * @return
     */
    Long idxIndexNoneCount(IdxIndex idxIndex);

    /**
     * 监控分析
     * @param form
     * @return
     */
    IPage<LkjIndex> index(LkjIndexListForm form);
}
