package com.zhuzhou.impl.lkj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.dao.lkj.LkjIndexMapper;
import com.zhuzhou.dao.video.IdxIndexMapper;
import com.zhuzhou.dto.video.LkjAnalyzeResult;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.statistic.PhaseAnalysis;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.form.lkj.LkjIndexListForm;
import com.zhuzhou.framework.utils.v2.HttpUtils;
import com.zhuzhou.framework.utils.v2.ResponseWrap;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.lkj.LkjIndexService;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.spi.video.PhaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-06
 */
@Service
public class LkjIndexServiceImpl extends BaseServiceImpl<LkjIndexMapper, LkjIndex> implements LkjIndexService {

    @Autowired
    private PhaseService phaseService;
    @Autowired
    private IdxIndexMapper idxIndexMapper;
    @Autowired
    private LkjIndexMapper lkjIndexMapper;

    @Value("${url.lkj}")
    private String LKJ_URL;

    @Override
    public List<Lkj> remoteList(String filePath) {
        ResponseWrap lkjWrap = HttpUtils.getInstance().get(LKJ_URL)
                .addParameter("url", filePath).execute();
        LkjAnalyzeResult lkjResult = lkjWrap.getJson(LkjAnalyzeResult.class);
        List<Lkj> list = lkjResult.getData();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        list.stream().forEach(f -> {
            String signalMachine = f.getSignalMachine();
            if (StringUtils.isEmpty(signalMachine)) {
                return;
            }
            Optional.ofNullable(ConfigConst.stationMap.get(signalMachine)).ifPresent(i -> {
                f.setSignalMachine(ConfigConst.stationMap.get(signalMachine).getStationName());
            });
        });
        return list;
    }

    @Override
    public IPage<LkjIndex> index(LkjIndexListForm form) {
        IPage<LkjIndex> page = idxIndexMapper.index(form);
        List<LkjIndex> tempList = page.getRecords();
        if (CollectionUtils.isEmpty(tempList)) {
            return page;
        }
        List<String> collect = tempList.stream().map(m -> m.getId()).collect(Collectors.toList());
        Map<String, PhaseAnalysis> map = phaseService.findIllegalScore(collect);
        tempList.stream().peek(f -> Optional.ofNullable(map.get(f.getId())).ifPresent(i -> f.setScore(100 - i.getScore()))).collect(Collectors.toList());
        return page;
    }

    @Override
    public List<String> getProcessIds() {

        return lkjIndexMapper.getProcessIds();
    }
}
