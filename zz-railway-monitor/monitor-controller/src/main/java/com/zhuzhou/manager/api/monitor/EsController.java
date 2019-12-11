package com.zhuzhou.manager.api.monitor;

import com.zhuzhou.dao.es.MonitorAlarmFileRepository;
import com.zhuzhou.es.monitor.MonitorAlarmFile;
import com.zhuzhou.framework.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * @Author chenzeting
 * @Date 2019-11-28
 * @Description:
 **/
@Controller
public class EsController {

    @Autowired
    private MonitorAlarmFileRepository monitorAlarmFileRepository;


    @GetMapping("/test111")
    public Result test1 () {
//        Pageable pageable = PageRequest.of(1, 10);
//        Iterable<MonitorAlarmFile> all = monitorAlarmFileRepository.findAll(pageable);
//        return Result.success().setData(all);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
//                .withIndices("alarm_file")
//                .withTypes("doc")
                .withFields("filePath","fileType")
                .withPageable(PageRequest.of(0, 10))
                .build();

        Page<MonitorAlarmFile> sampleEntities = monitorAlarmFileRepository.search(searchQuery);
        return Result.success().setData(sampleEntities);
    }
}
