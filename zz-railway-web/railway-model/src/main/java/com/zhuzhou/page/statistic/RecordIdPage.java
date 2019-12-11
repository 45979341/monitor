package com.zhuzhou.page.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiechonghu
 * @date 2019/8/9 17:45
 * @description: 每一趟分析统计查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecordIdPage extends Page {

    /**
     * lkj_index id
     */
    private String recordId;
}
