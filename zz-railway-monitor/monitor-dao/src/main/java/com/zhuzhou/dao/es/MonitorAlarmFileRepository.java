package com.zhuzhou.dao.es;

import com.zhuzhou.es.monitor.MonitorAlarmFile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author chenzeting
 * @Date 2019-12-04
 * @Description:
 **/
@Repository
public interface MonitorAlarmFileRepository extends ElasticsearchRepository<MonitorAlarmFile, Long> {

}
