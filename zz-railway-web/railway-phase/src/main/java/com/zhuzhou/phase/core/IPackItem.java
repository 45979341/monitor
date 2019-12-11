package com.zhuzhou.phase.core;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.phase.LkjHead;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-08-20
 * @Description:
 **/
public interface IPackItem {
    /**
     * <p>
     *  注入各个类型项点
     * </p>
     *
     * @param list lkj全程记录
     * @param lkjHead lkj头信息
     * @param lkjId lkj记录id
     */
    void inspectInject(List<Lkj> list, LkjHead lkjHead, String lkjId);

    /**
     * 手势项点注入
     * @param list
     * @param lkjHead
     * @param lkjId
     * @param dir
     * @param mp4List
     */
    void injectHand(List<Lkj> list, LkjHead lkjHead, String lkjId, String dir, List<Mp4> mp4List);
}
