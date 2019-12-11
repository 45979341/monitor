package com.zhuzhou.manager.api.lkj;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.form.lkj.LkjIndexListForm;
import com.zhuzhou.form.lkj.LkjIndexProcessListForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.lkj.LkjIndexService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-04-06
 * Description:
 * @menu LKJ信息
 */
@Controller
public class LkjIndexController {
    @Autowired
    private LkjIndexService lkjIndexService;

    /**
     * 监控分析首页
     * @param form
     * @return
     */
    @RequiresPermissions("lkj:index")
    @GetMapping(name = "监控分析首页", value = "/lkj/index")
    public Result<IPage<LkjIndex>> list(LkjIndexListForm form) {
        IPage<LkjIndex> page = lkjIndexService.index(form);
        return Result.<IPage<LkjIndex>>success().setData(page);
    }



    /**
     * lkj处理
     * @param form
     */
    @RequiresPermissions("idx:process:list")
    @GetMapping(name = "lkj处理", value = "/idx/process/list")
    public Result<Object> list(LkjIndexProcessListForm form) {
        if (form.getCount() == null || form.getCount() == 0) {
            return Result.success().setData(lkjIndexService.list(form));
        }
        int count = form.getCount();
        Date date = LocalDateTime.now().plusDays(-count).withTime(0, 0, 0, 0).toDate();
        form.setCreateTime(date);
        List<LkjIndex> list = lkjIndexService.list(form);
        return Result.success().setData(list);
    }
}
