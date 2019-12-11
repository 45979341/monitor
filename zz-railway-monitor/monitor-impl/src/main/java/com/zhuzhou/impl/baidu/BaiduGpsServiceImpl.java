package com.zhuzhou.impl.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuzhou.dto.baidu.CoordInfo;
import com.zhuzhou.framework.utils.v2.HttpUtils;
import com.zhuzhou.framework.utils.v2.RequestWrap;
import com.zhuzhou.framework.utils.v2.ResponseWrap;
import com.zhuzhou.spi.baidu.BaiduGpsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-20
 * @Description:
 **/
@Service
public class BaiduGpsServiceImpl implements BaiduGpsService {

    private static final String GEOCONV_URL = "http://api.map.baidu.com/geoconv/v1/?coords=%s&from=1&to=5&ak=%s";
    private static final int pageSize = 100;

    @Value("${baidu.ak}")
    public String baiduAk;

    @Override
    public List<CoordInfo> convertToBaidu(List<String> list) {
        List<CoordInfo> coordInfos = new ArrayList();
        for (int i = 0; i < list.size(); i+=100) {
            List<String> strings;
            if (i + 100 > list.size()) {
                strings = list.subList(i, list.size());
            } else {
                strings = list.subList(i, i + 100);
            }
            RequestWrap requestWrap = HttpUtils.getInstance().get(GEOCONV_URL);
            String join = StringUtils.join(strings.toArray(), ";");
            requestWrap.addParameter("coords", join);
            requestWrap.addParameter("ak", "Mak2KhQbHL0YHX5TmPKtwtKnzwbBzF8A");
            ResponseWrap execute = requestWrap.execute();
            String respContent = execute.getString();
            if (!StringUtils.isEmpty(respContent)) {
                JSONObject jsonObject = JSONObject.parseObject(respContent);
                if (jsonObject.containsKey("result")) {
                    coordInfos.addAll((Collection) JSON.parseObject(jsonObject.getString("result")));
                }
            }
        }
        return coordInfos;
    }
//
//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        list.add("114.21892734521,29.575429778924");
//        list.add("114.21892734522,29.575429778924");
//        list.add("114.21892734523,29.575429778924");
//        list.add("114.21892734524,29.575429778924");
//        list.add("114.21892734525,29.575429778924");
//        convertToBaidu(list);
//    }
}
