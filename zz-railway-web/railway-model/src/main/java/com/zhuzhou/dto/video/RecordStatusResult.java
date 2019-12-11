package com.zhuzhou.dto.video;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordStatusResult implements Serializable {
    private Integer noExecute = 0;  //未处理
    private Integer executed = 0;   //已处理
    private Integer executing = 0;  //处理中
}
