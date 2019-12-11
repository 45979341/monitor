package com.zhuzhou.impl.monitor;


import com.zhuzhou.dao.es.MonitorAlarmInfoRepository;
import com.zhuzhou.dao.monitor.MonitorAlarmInfoMapper;
import com.zhuzhou.dto.history.AlarmHistoryInfoResp;
import com.zhuzhou.dto.history.AlarmHistoryResp;
import com.zhuzhou.es.monitor.MonitorAlarmInfo;
import com.zhuzhou.form.monitor.MonitorAlarmInfoListForm;
import com.zhuzhou.impl.BaseServiceImpl;
import com.zhuzhou.spi.monitor.MonitorAlarmInfoService;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报警信息表 服务实现类
 * </p>
 * @author chenzeting
 * @since 2019-06-17
 */
@Service
public class MonitorAlarmInfoServiceImpl extends BaseServiceImpl<MonitorAlarmInfoMapper, com.zhuzhou.entity.monitor.MonitorAlarmInfo> implements MonitorAlarmInfoService {
    @Autowired
    private MonitorAlarmInfoRepository monitorAlarmInfoRepository;

    private static final String[] results = new String[]{"deviceId", "trainCode", "ccStr", "driverId", "startTime"};

    @Override
    public List<AlarmHistoryInfoResp> getAlarmHistoryInfo(MonitorAlarmInfoListForm form) {
        System.out.println("执行");
        MatchAllQueryBuilder allQuery = QueryBuilders.matchAllQuery();
        NativeSearchQueryBuilder nativeBuilder = new NativeSearchQueryBuilder();
        TermsAggregationBuilder aggs =
                AggregationBuilders.terms("deviceId").field("deviceId.keyword")
                        .subAggregation(AggregationBuilders.terms("trainCode").field("trainCode.keyword")
                                .subAggregation(AggregationBuilders.terms("ccStr").field("ccStr.keyword")
                                        .subAggregation(AggregationBuilders.terms("driverId").field("driverId")
                                                .subAggregation(AggregationBuilders.min("startTime").field("startTime"))
                                                .subAggregation(AggregationBuilders.min("startTime").field("startTime"))
                                        )));

// AggregationBuilders.min("startTime").field("startTime")
        NativeSearchQuery build = nativeBuilder.withQuery(allQuery)
                .withFields(results)
                .addAggregation(aggs)
                .withPageable(PageRequest.of(0, 50))
                .build();
        Page<MonitorAlarmInfo> search = monitorAlarmInfoRepository.search(build);

        List<MonitorAlarmInfo> content = search.getContent();
        content.forEach(System.out::println);
        return null;
    }
}
