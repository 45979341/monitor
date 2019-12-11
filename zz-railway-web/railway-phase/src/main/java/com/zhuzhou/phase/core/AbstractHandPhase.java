package com.zhuzhou.phase.core;

import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.enums.video.FileSufEnum;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.video.PhaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: chenzeting
 * Date:     2019/10/29
 * Description:
 */
@Component
@Slf4j
public abstract class AbstractHandPhase implements IHandPhaseService {

    @Autowired
    protected PhaseService phaseService;

    @Override
    public void hand(String recordId, String dir, List<Mp4> mp4List, String frontStatus, Date start, Date lkjTime, Date arg1, Date arg2, String type, Phase.PhaseEnum phase) {
        String beginFile = null;
        if (start != null) {
            if (StringUtils.isNotEmpty(front(start, mp4List, frontStatus))) {
                beginFile = dir + front(start, mp4List, frontStatus);
            }
        }
        String front;
        if (StringUtils.isEmpty(front = front(lkjTime, mp4List, frontStatus))) {
            phaseService.add(recordId, start, arg1, arg2, beginFile, front, type, phase, 2);
            return;
        }
        phaseService.add(recordId, start, arg1, arg2, beginFile, dir + front, type, phase);
    }

    /**
     * 获取指定视频对应的项点识别
     *
     * @param date
     * @param mp4List
     * @param frontBehind
     * @return if 返回为空，代表无此视频文件
     */
    private String front(Date date, List<Mp4> mp4List, String frontBehind) {
        String hms = DateUtils.format(DateUtils.all, date);
        List<Mp4> frontList = new ArrayList<>();
        List<Mp4> behindList = new ArrayList<>();
        for (Mp4 m : mp4List) {
            String channelName = m.getChannelName();
            if (StringUtils.isNotEmpty(channelName) && channelName.contains("一端司机室")) {
                frontList.add(m);
            }
            if (StringUtils.isNotEmpty(channelName) && channelName.contains("二端司机室")) {
                behindList.add(m);
            }
        }

        if ("前".equals(frontBehind)) {
            return getList(hms, frontList);
        } else if ("后".equals(frontBehind)) {
            return getList(hms, behindList);
        }
        return null;
    }


    /**
     * 获取指定视频对应的项点识别
     *
     * @param hms
     * @param frontList
     * @return if 返回为空，代表无此视频文件
     */
    private String getList(String hms, List<Mp4> frontList) {
        try {
            for (int k = 0; k < frontList.size(); k++) {
                Mp4 m = frontList.get(k);
                if (k == frontList.size() - 1) {
                    String prefixDate = m.getPrefixDate();
                    String suffixDate = m.getSuffixDate();
                    int compare = hms.compareTo(prefixDate + suffixDate);
                    if (compare < 0) {
                        return mp4Result(frontList.get(k - 1));
                    }
                    return mp4Result(m);
                } else {
                    String prefixDate = m.getPrefixDate();
                    String suffixDate = m.getSuffixDate();
                    int compare = hms.compareTo(prefixDate + suffixDate);
                    if (compare < 0) {
                        return mp4Result(frontList.get(k - 1));
                    }
                }
            }
        } catch (Exception e) {
            log.info("视频文件缺失,跳过", e);
            return null;
        }
        return null;
    }

    /**
     * 拼接视频完整名称
     *
     * @param m
     * @return
     */
    private String mp4Result(Mp4 m) {
        return m.getCabNum() + SymbolConst.UNDER + m.getFactoryName() + SymbolConst.UNDER + m.getChannelNum() +
                SymbolConst.UNDER + m.getChannelName() + SymbolConst.UNDER + m.getPrefixDate() +
                SymbolConst.UNDER + m.getSuffixDate() + SymbolConst.DOT + FileSufEnum.MP4.getSuf();
    }

    /**
     * 注入项点规则
     * @param list LKJ列表
     * @param head lkj信息头
     * @param lkjId lkjId
     * @param dir 文件目录
     * @param mp4List mp4列表
     */
    public abstract void exec(List<Lkj> list, LkjHead head, String lkjId, String dir, List<Mp4> mp4List);
}
