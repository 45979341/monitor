package com.zhuzhou.framework.result;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 自定义JSON序列化器
 */
public class ResultJsonSerializer extends ObjectMapper {

	private static final long serialVersionUID = -4507425084024099525L;

    public ResultJsonSerializer() {
        //不序列化空对象
        this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //支持JSON格式提交可以多字段或少字段
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.addMixIn(Object.class, DynamicFilterMixIn.class);
        this.setFilterProvider(new DynamicFilterProvider());
    }

    @JsonFilter(DynamicFilterProvider.FILTER_ID)
    interface DynamicFilterMixIn {
    }

}
