package com.zhuzhou.phase.model;

import lombok.Data;

@Data
public class GarageAsist {
    private Integer constPipe;  //定压值
    private Integer subMin;     //库内最大减压下限
    private Integer subMax;     //库内最大减压上限
}
