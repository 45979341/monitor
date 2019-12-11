package com.zhuzhou.manager.api.config;

import com.zhuzhou.entity.config.ConfigPhase;
import com.zhuzhou.form.config.ConfigPhaseListForm;
import com.zhuzhou.form.config.ConfigPhaseScoreEditForm;
import com.zhuzhou.form.config.ConfigPhaseStatusEditForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.spi.config.ConfigPhaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 项点分数表 前端控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-07-24
 * Description:
 */
@Controller
public class ConfigPhaseController {
    @Autowired
    private ConfigPhaseService configPhaseService;

    /**
     * 项点配置列表
     *
     * @return
     */
    @RequiresPermissions("config:phase:list")
    @GetMapping(name = "项点配置列表", value = "/config/phase/list")
    public Result<List<ConfigPhase>> list(ConfigPhaseListForm form) {
        List<ConfigPhase> list = configPhaseService.list(form);
        return Result.<List<ConfigPhase>>success().setData(list);
    }

    /**
     * 项点分数修改
     *
     * @return
     */
    @RequiresPermissions("config:phase:scoreEdit")
    @PostMapping(name = "项点分数修改", value = "/config/phase/scoreEdit")
    public Result edit(ConfigPhaseScoreEditForm form) {
        ConfigPhase cps = new ConfigPhase();
        ReflectionUtils.copyProperties(cps, form);
        configPhaseService.updateById(cps);
        return Result.success();
    }

    /**
     * 查询选中必分析项点
     *
     * @return
     */
    @RequiresPermissions("basis:phase:check")
    @GetMapping(name = "查询选中必分析项点", value = "/basis/phase/check")
    public Result<List<ConfigPhase>> check() {
        List<ConfigPhase> list = configPhaseService.list();
        return Result.<List<ConfigPhase>>success().setData(list);
    }

    /**
     * 勾选必分析项点
     */
    //@RequiresPermissions("config:phase:statusEdit")
    @PostMapping(name = "勾选必分析项点", value = "/config/phase/statusEdit")
    public Result<Object> edit(ConfigPhaseStatusEditForm form) {
        ConfigPhase configPhase = new ConfigPhase();
        ReflectionUtils.copyProperties(configPhase, form);
        configPhaseService.updateById(configPhase);
        return Result.success();
    }
}
