package com.zhuzhou.impl.video;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.consts.ConfigConst;
import com.zhuzhou.dto.video.IdxAnalyzeResult;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.dao.video.IdxIndexMapper;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.framework.utils.v2.HttpUtils;
import com.zhuzhou.framework.utils.v2.ResponseWrap;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-17
 */
@Service
public class IdxIndexServiceImpl extends BaseServiceImpl<IdxIndexMapper, IdxIndex> implements IdxIndexService {
    @Autowired
    private IdxIndexMapper idxIndexMapper;

    @Value("${url.idx}")
    private String IDX_URL;

    @Override
    public List<IdxIndex> idxIndexNone(IdxIndex idxIndex, long offset, long size) {
        return idxIndexMapper.idxIndexNone(idxIndex, offset, size);
    }

    @Override
    public Long idxIndexNoneCount(IdxIndex idxIndex) {
        return idxIndexMapper.idxIndexNoneCount(idxIndex);
    }


    @Override
    public List<Idx> remoteList(String id) {
        IdxIndex idx = getById(id);
        if (idx == null) {
            return null;
        }
        String[] split = idx.getFileName().split("\\|");
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < split.length;i++) {
            sb.append(idx.getDir()).append(split[i]);
            if (split.length - 1 != i) {
                sb.append("|");
            }
        }

        //获取idx数据
        ResponseWrap idxWrap = HttpUtils.getInstance().get(IDX_URL)
                .addParameter("url", sb.toString()).execute();
        IdxAnalyzeResult idxResult = idxWrap.getJson(IdxAnalyzeResult.class);
        List<Idx> list = idxResult.getData();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        long start = DateUtils.parse(DateUtils.sdf, idx.getStartTime()).getTime();
        long end = DateUtils.parse(DateUtils.sdf, idx.getEndTime()).getTime();
        List<Idx> tempList = new ArrayList<>();
        for (Idx i : list) {
            long platformTime = i.getPlatformTime().getTime();
            //过滤无用时间段数据
            if (!(platformTime > start && platformTime < end)) {
                continue;
            }

            Optional.ofNullable(ConfigConst.stationMap.get(i.getCrossRoadNum() + " -" + i.getStationNum())).ifPresent(v -> i.setStationName(v.getStationName()));
            Optional.ofNullable(ConfigConst.stewardMap.get(i.getDriverNum())).ifPresent(v -> i.setDriverName(v.getName()));
            Optional.ofNullable(ConfigConst.stewardMap.get(i.getAssisDriverNum())).ifPresent(v -> i.setAssisDriverName(v.getName()));
            tempList.add(i);
        }
        return tempList;
    }
}