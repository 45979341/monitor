package com.zhuzhou.spi.baidu;

import com.zhuzhou.dto.baidu.CoordInfo;

import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-06-20
 * @Description:
 **/
public interface BaiduGpsService {
    /**
     * 装换百度经纬度
     * @param list
     * @return
     */
    List<CoordInfo> convertToBaidu(List<String> list);
}
