package com.zhuzhou.manager.api.ai;

import com.zhuzhou.entity.ai.Phone;
import com.zhuzhou.entity.ai.Smoking;
import com.zhuzhou.form.ai.RecordIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.ai.SmokingService;

import java.util.List;

/**
 * <p>
 * ai识别-吸烟 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-08-06
 * Description:
 * @menu 处理列表
 */
@Controller
public class SmokingController {
    @Autowired
    private SmokingService smokingService;

    /**
     * 抽烟列表
     * @return
     */
    @RequiresPermissions("smoking:list")
    @GetMapping(name = "抽烟列表", value = "/smoking/list")
    public Result<List<Smoking>> list (RecordIdForm form) {
        Smoking smoking = new Smoking();
        smoking.setRecordId(form.getRecordId());
        List<Smoking> list = smokingService.list(/*smoking*/);
        return Result.<List<Smoking>>success().setData(list);
    }
}
