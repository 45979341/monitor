package com.zhuzhou.page.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiechonghu
 * @date 2019/8/19 10:59
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptStatisticPage extends Page implements Form {
    /**
     * 车间 段 局
     */
    @ValueSet({"workshop","locomotive_depot","railway"})
    private String dept;
    /**
     * 车间 段 局名
     */
    private String name;
}
