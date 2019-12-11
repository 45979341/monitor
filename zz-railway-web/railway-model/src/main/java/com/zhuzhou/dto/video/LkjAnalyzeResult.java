package com.zhuzhou.dto.video;

import com.zhuzhou.entity.video.Lkj;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author chenzeting
 * @Date 2019-03-18
 * @Description: 解析LKJ 实体类返回
 **/
@Data
public class LkjAnalyzeResult implements Serializable {

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
    private List<Lkj> data;
}
