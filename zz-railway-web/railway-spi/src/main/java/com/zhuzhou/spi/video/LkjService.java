package com.zhuzhou.spi.video;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-03-20
 */
public interface LkjService extends BaseService<Lkj> {
    /**
     * 手势分析项点
     * @param recordId
     * @param list
     * @param dir
     * @param mp4List
     */
    void analysisPhase(String recordId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead);

    /**
     * lkj项点分析
     * @param list   lkj列表
     * @param lkjId  lkj首页id
     */
    void analysisLkjPhase(List<Lkj> list, String lkjId);

    /**
     * 分析机械间
     * @param recordId
     * @param list
     * @param dir
     * @param mp4List
     */
    void analysisMachine(String recordId, List<Lkj> list, String dir, List<Mp4> mp4List, LkjHead lkjHead);

    /**
     * 分析前方障碍物
     * @param lkjId
     * @param list
     * @param mp4List
     */
    void analysisFront(String lkjId, List<Lkj> list, String dir,  List<Mp4> mp4List, LkjHead lkjHead);

    /**
     * 分析遮挡
     * @param lkjId
     * @param list
     * @param mp4List
     */
    void analysisCover(String lkjId, List<Lkj> list, String dir,  List<Mp4> mp4List, LkjHead lkjHead);

    /**
     * 处理lkj文件，主要是限速，距离不规范都去除，方便做查询
     * @param list
     * @return
     */
    LkjHead processLkj (List<Lkj> list);
}
