package com.zhuzhou.spi.video;

import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
public interface IdxIndexService extends BaseService<IdxIndex> {

    /**
     * 获取无lkj文件的idx_index，可做查看视频入口
     * @return
     * @param idxIndex
     * @param offset
     * @param size
     */
    List<IdxIndex> idxIndexNone(IdxIndex idxIndex, long offset, long size);

    /**
     * 获取无lkj文件的idx_index个数
     * @param idxIndex
     * @return
     */
    Long idxIndexNoneCount(IdxIndex idxIndex);


    /**
     * 内网调用获取idx信息
     * @param id
     * @return
     */
    List<Idx> remoteList(String id);
}
