package com.zhuzhou.page.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.framework.form.Form;
import com.zhuzhou.framework.validate.annotation.ValueSet;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author xiechonghu
 * @date 2019/8/19 10:57
 * @description: 按司机工号名字查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DriverStatisticPage extends Page implements Form {
    /**
     * 工号
     */
    private Integer jobNum;
    /**
     * 司机名
     */
    private String name;
    /**
     * 查询方式（年月周）
     */
    @ValueSet({"year", "month", "week"})
    private String type;
    /**
     * 年月周对应的值
     */
    private String typeValue;
}