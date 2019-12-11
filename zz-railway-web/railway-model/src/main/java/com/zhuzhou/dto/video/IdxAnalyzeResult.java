package com.zhuzhou.dto.video;

import com.zhuzhou.entity.video.Idx;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-03-18
 * @Description: 解析IDX 实体类返回
 **/
@Data
public class IdxAnalyzeResult implements Serializable {

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private List<Idx> data;
}
