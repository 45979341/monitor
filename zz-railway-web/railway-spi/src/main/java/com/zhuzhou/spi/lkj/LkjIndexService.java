package com.zhuzhou.spi.lkj;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.form.lkj.LkjIndexListForm;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-06
 */
public interface LkjIndexService extends BaseService<LkjIndex> {

    /**
     * 内网调用获取lkj信息
     * @param filePath
     * @return
     */
    List<Lkj> remoteList(String filePath);

    /**
     * 获取首页lkj列表
     * @param form
     * @return
     */
    IPage<LkjIndex> index(LkjIndexListForm form);

    /**
     * 获取Lkj_index的id与Idx_index的record_id相同的id值
     */
    List<String> getProcessIds();
}
