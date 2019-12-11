package com.zhuzhou.manager.api.video;


import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.entity.video.Phase;
import com.zhuzhou.form.video.PhaseListForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.video.IdxAnalysisLogService;
import com.zhuzhou.spi.video.PhaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-03-27
 * Description:
 * @menu 处理列表
 */
@Controller
public class PhaseController {
    @Autowired
    private PhaseService phaseService;

    @Value("${file.uri}")
    private String uri;

    /**
     * 相点列表ces
     * @return
     */
    @RequiresPermissions("phase:list")
    @GetMapping(name = "相点列表ces", value = "/phase/list")
    public Result list(PhaseListForm form) {
        List<Phase> list = phaseService.list(form);
        return Result.success().setData(list);
    }

    /**
     * 相点列表
     * @return
     */
    @RequiresPermissions("phase:hand:list")
    @GetMapping(name = "相点列表", value = "/phase/hand/list")
    public Result hand(PhaseListForm form) {
        form.setCode("H");
        List<Phase> list = phaseService.findList(form);
        return Result.success().setData(list);
    }

    /**
     * 临时新增相点列表
     * @return
     */
    @GetMapping(name = "临时相点列表", value = "/phase/temp/list")
    public Result tempHand(PhaseListForm form) {
        form.setCode("H");
        List<Phase> list = phaseService.findList(form);
        return Result.success().setData(list);
    }
}
