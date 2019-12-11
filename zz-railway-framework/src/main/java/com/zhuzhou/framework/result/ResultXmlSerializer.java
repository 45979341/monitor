package com.zhuzhou.framework.result;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 自定义XML序列化器
 */
public class ResultXmlSerializer extends XmlMapper {
    private static final long serialVersionUID = 7405627054385318155L;

    public ResultXmlSerializer() {
        //不序列化空对象
        this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //支持JSON格式提交可以多字段或少字段
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.addMixIn(Object.class, DynamicFilterMixIn.class);
        this.setFilterProvider(new DynamicFilterProvider());
    }

    public ResultXmlSerializer(JacksonXmlModule module) {
        super(module);
    }

    @JsonFilter(DynamicFilterProvider.FILTER_ID)
    interface DynamicFilterMixIn {
    }

}
