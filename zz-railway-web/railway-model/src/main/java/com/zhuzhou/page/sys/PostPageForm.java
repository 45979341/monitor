package com.zhuzhou.page.sys;

import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.framework.jdbc.Condition;
import com.zhuzhou.framework.jdbc.Operator;
import com.zhuzhou.framework.jdbc.Order;
import com.zhuzhou.page.PageForm;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author chenzeting
 * @Date 2019-05-08
 * @Description:
 * @see com.zhuzhou.entity.sys.SysPost
 **/
@Data
public class PostPageForm extends PageForm<SysPost> {

    /**
     * 岗位名称
     */
    @Condition(operator = Operator.LIKE)
    private String name;

    /**
     * 更新时间
     */
    @Order
    private Timestamp createTime;
}
