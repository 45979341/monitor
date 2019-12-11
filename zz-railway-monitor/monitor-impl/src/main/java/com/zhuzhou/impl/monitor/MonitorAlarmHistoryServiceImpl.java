package com.zhuzhou.impl.monitor;

import com.zhuzhou.dao.es.MonitorAlarmInfoRepository;
import com.zhuzhou.dao.es.MonitorRealtimeInfoRepository;
import com.zhuzhou.dto.history.AlarmHistoryResp;
import com.zhuzhou.enums.exterior.AlarmTypeEnum;
import com.zhuzhou.es.monitor.MonitorAlarmInfo;
import com.zhuzhou.es.monitor.MonitorRealtimeInfo;
import com.zhuzhou.form.monitor.MonitorAlarmInfoListForm;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.spi.baidu.BaiduGpsService;
import com.zhuzhou.spi.monitor.MonitorAlarmHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 历史信息接口类
 * </p>
 *
 * @author wangxiaokuan
 * @since 2019-12-02
 */
@Service
public class MonitorAlarmHistoryServiceImpl implements MonitorAlarmHistoryService {

    @Autowired
    private BaiduGpsService baiduGpsService;

    @Autowired
    private MonitorAlarmInfoRepository monitorAlarmInfoRepository;

    @Autowired
    private MonitorRealtimeInfoRepository monitorRealtimeRepository;

    private static final String[] results = new String[]{"deviceId", "gpsLng", "gpsTidu", "ccStr", "trainCode", "alarmTime", "alarmEvent"};

    @Override
    public Map<String, List<AlarmHistoryResp>> getHistoryAlarmTrail(MonitorAlarmInfoListForm form) {

        /**
         * 先获得报警信息
         */
        RangeQueryBuilder range = rangeMathQuery("alarmTime", form.getAlarmTime().getTime()+"", DateUtils.opSecond(form.getAlarmTime(),60*60*24).getTime()+"");
        BoolQueryBuilder boolQueryBuilder = orMatchUnionWithList(form);
        boolQueryBuilder.must(range);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(0, 1000))
                .withFields(results)
                .build();

        Page<MonitorAlarmInfo> search = monitorAlarmInfoRepository.search(build);
        Map<String , List<AlarmHistoryResp>> map = new HashMap<>();
        ArrayList<AlarmHistoryResp> list = null;
        Iterator<MonitorAlarmInfo> iterator = search.iterator();

        while (iterator.hasNext()) {
            MonitorAlarmInfo alarmInfo = iterator.next();
            AlarmHistoryResp resp = new AlarmHistoryResp(null, alarmInfo.getDeviceId(), alarmInfo.getGpsLng(), alarmInfo.getGpsTidu()
                    , alarmInfo.getCcStr(), alarmInfo.getTrainCode(), alarmInfo.getAlarmTime(), alarmInfo.getAlarmEvent()
                    , AlarmTypeEnum.getById(alarmInfo.getAlarmEvent()).alarmEvent);
            if (map.containsKey(resp.getDeviceId()) && resp.getDeviceId() != null) {
                map.get(resp.getDeviceId()).add(resp);
            } else {
                list = new ArrayList<>();
                list.add(resp);
                map.put(resp.getDeviceId(),list);
            }
        }

        /**
         * 再去历史轨迹中获取
         */
        Set<String> deviceIds = map.keySet();
        for (String deviceId : deviceIds) {
            form.setDeviceId(deviceId);
            boolQueryBuilder = orMatchUnionWithList(form);
            boolQueryBuilder.must(range);
            build = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withPageable(PageRequest.of(0, 1000))
                    .withFields(results)
                    .build();

            Page<MonitorRealtimeInfo> infos = monitorRealtimeRepository.search(build);

            Iterator<MonitorRealtimeInfo> iterator1 = infos.iterator();

            while (iterator1.hasNext()) {
                MonitorRealtimeInfo realtimeInfo = iterator1.next();
                AlarmHistoryResp resp = new AlarmHistoryResp(null, realtimeInfo.getDeviceId(), realtimeInfo.getGpsLng(), realtimeInfo.getGpsTidu()
                        , realtimeInfo.getCcStr(), realtimeInfo.getTrainCode(), realtimeInfo.getAlarmTime(), 0
                        , "");
                if (map.containsKey(resp.getDeviceId()) && resp.getDeviceId() != null) {
                    map.get(resp.getDeviceId()).add(resp);
                } else {
                    list = new ArrayList<>();
                    list.add(resp);
                    map.put(resp.getDeviceId(),list);
                }
            }
        }

        for (String deviceId : deviceIds) {
            map.get(deviceId).sort(new Comparator<AlarmHistoryResp>() {
                @Override
                public int compare(AlarmHistoryResp o1, AlarmHistoryResp o2) {
                    return o1.getAlarmTime().compareTo(o2.getAlarmTime());
                }
            });
        }

        return map;
    }

    /**
     * 范围查询，左右都是闭集
     * @param fieldKey
     * @param start
     * @param end
     * @return
     */
    public RangeQueryBuilder rangeMathQuery(String fieldKey, String start, String end){
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldKey);
        rangeQueryBuilder.gte(start);
        rangeQueryBuilder.lt(end);
        return rangeQueryBuilder;
    }

    /**
     * 多条件检索并集
     * @param form
     * @return
     */
    public BoolQueryBuilder orMatchUnionWithList(MonitorAlarmInfoListForm form){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (form.getDeviceId() != null && StringUtils.isNotEmpty(form.getDeviceId())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("deviceId",form.getDeviceId()));
        }
        if (form.getCcStr()!=null && StringUtils.isNotEmpty(form.getCcStr())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("ccStr",form.getCcStr()));
        }
        if (form.getDriverId()!=null) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("driverId",form.getDriverId()));
        }
        if (form.getTrainCode() != null && StringUtils.isNotEmpty(form.getTrainCode())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("trainCode",form.getTrainCode()));
        }
        return boolQueryBuilder;
    }
}
