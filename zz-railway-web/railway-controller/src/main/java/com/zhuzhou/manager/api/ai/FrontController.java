package com.zhuzhou.manager.api.ai;

import com.zhuzhou.entity.ai.Front;
import com.zhuzhou.form.ai.RecordIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.ai.FrontService;
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
 * Date:     2019-06-27
 * Description:
 * @menu 处理列表
 */
@Controller
public class FrontController {
    @Autowired
    private FrontService frontService;

    /**
     * 前方障碍物列表
     * @return
     */
    @RequiresPermissions("front:list")
    @GetMapping(name = "前方障碍物列表", value = "/front/list")
    public Result<List<Front>> list(RecordIdForm form) {
        Front front = new Front();
        front.setRecordId(form.getRecordId());
        List<Front> list = frontService.list(front);
        return Result.<List<Front>>success().setData(list);
    }
}
