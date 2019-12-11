package com.zhuzhou.phase.core;

import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.phase.model.PhaseAssist;
import com.zhuzhou.spi.lkj.ItemRecordService;
import com.zhuzhou.spi.video.LkjService;
import com.zhuzhou.spi.video.PhaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-07-30
 * @Description: 定义项点抽象工厂，执行项点规则
 **/
@Component
@Slf4j
public abstract class AbstractPhase implements IPhaseService {
    @Autowired
    protected PhaseService phaseService;
    @Autowired
    protected ItemRecordService itemRecordService;
    @Autowired
    protected LkjService lkjService;

    /**
     * 添加lkj记录,添加项点记录,添加项点
     * @param assist 构建builder,统一处理层
     */
    @Override
    public void add(PhaseAssist assist) {
        assist.getLkj().setRecordId(assist.getLkjId());
        lkjService.save(assist.getLkj());
        itemRecordService.add(assist.getLkjId(), assist.getItemEnum(), assist.getLkj().getId(), assist.getItemRecordIllegal());
        phaseService.add(assist.getLkjId(), assist.getStart(), assist.getEnd(), assist.getPhaseEnum(), assist.getPhaseIllegal());
    }

    /**
     * 注入项点规则
     * @param list  LKJ列表
     * @param head  lkj头信息
     * @param lkjId lkjId
     * @param start 开始位置的索引
     * @param end   结束位置的索引
     */
    public abstract void exec(List<Lkj> list, LkjHead head, String lkjId, int start, int end);
}
