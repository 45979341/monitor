package com.zhuzhou.entity.statistic;

import com.zhuzhou.entity.idx.IdxAnalysisLog;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
@Data
public class RailAnalysis extends IdxAnalysisLog {

    /**
     * 项点数量
     */
    private Integer num;
    /**
     * 违章数量
     */
    private Integer illegalNum;
    /**
     * 机务段
     */
    private String locomotiveDepot;
    /**
     * 运用车间
     */
    private String workshop;
    /**
     * 铁路局
     */
    private String railway;

}
