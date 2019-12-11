package com.zhuzhou.manager.api.ai;


import com.zhuzhou.entity.ai.Cover;
import com.zhuzhou.form.ai.RecordIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.ai.CoverService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-04-11
 * Description:
 */
@Controller
public class CoverController {
    @Autowired
    private CoverService coverService;

    /**
     * 获取遮挡列表
     * @return
     */
    @RequiresPermissions("cover:list")
    @GetMapping(name = "获取遮挡列表", value = "/cover/list")
    public Result<List<Cover>> list(RecordIdForm form) {
        Cover cover = new Cover();
        cover.setRecordId(form.getRecordId());
        List<Cover> list = coverService.list(cover);
        return Result.<List<Cover>>success().setData(list);
    }
}
