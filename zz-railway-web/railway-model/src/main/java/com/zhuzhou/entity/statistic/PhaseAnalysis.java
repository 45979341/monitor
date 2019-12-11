package com.zhuzhou.entity.statistic;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author chenzeting
 * @Date 2019-07-24
 * @Description:
 **/
@Data
public class PhaseAnalysis implements Serializable {

    private String recordId;

    private long score;
}
