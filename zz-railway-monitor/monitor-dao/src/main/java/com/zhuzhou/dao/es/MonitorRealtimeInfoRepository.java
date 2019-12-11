package com.zhuzhou.dao.es;

import com.zhuzhou.es.monitor.MonitorRealtimeInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author wangxiaokuan
 * @Date 2019-12-05
 * @Description:
 **/
@Repository
public interface MonitorRealtimeInfoRepository extends ElasticsearchRepository<MonitorRealtimeInfo, Long> {
}
