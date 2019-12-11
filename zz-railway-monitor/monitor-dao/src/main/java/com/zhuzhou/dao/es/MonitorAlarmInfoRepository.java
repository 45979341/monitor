package com.zhuzhou.dao.es;

import com.zhuzhou.es.monitor.MonitorAlarmInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author wangxiaokuan
 * @Date 2019-12-05
 * @Description:
 **/
@Repository
public interface MonitorAlarmInfoRepository extends ElasticsearchRepository<MonitorAlarmInfo, Long> {
}
