package com.zhuzhou.page.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiechonghu
 * @date 2019/8/12 11:11
 * @description: 趟违章统计表分页对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RailStatisticPage extends Page {

    /**
     * 查询开始日期
     */
    private String startTime;

    /**
     * 查询结束时期
     */
    private String endTime;
}
