package com.zhuzhou.page.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiechonghu
 * @date 2019/8/13 16:38
 * @description: 项点统计
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchPage extends Page implements Form {
    /**
     * 名字
     */
    private String name;
    /**
     * 周 月 年
     */
    @ValueSet({"year","month","week"})
    private String type;
    /**
     * 周 月 年 值
     */
    private String typeValue;
}
