package com.zhuzhou.entity.statistic;

import com.zhuzhou.entity.lkj.LkjIndex;
import lombok.Data;

/**
 * @Author chenzeting
 * @Date 2019-06-27
 * @Description:
 **/
@Data
public class ItemAnalysis extends LkjIndex {

    /**
     * 项点统计
     */
    private Integer itemNum;

    /**
     * 违章数量
     */
    private Integer illegalNum;
}
