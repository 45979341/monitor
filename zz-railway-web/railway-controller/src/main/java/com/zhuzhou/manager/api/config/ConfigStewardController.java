package com.zhuzhou.manager.api.config;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.entity.config.ConfigSteward;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.page.config.ConfigStationPageForm;
import com.zhuzhou.page.config.ConfigStewardPageForm;
import com.zhuzhou.spi.config.ConfigStewardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 司机数据表
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-04-18
 * Description:
 * @menu 配置管理
 */
@Controller
public class ConfigStewardController {
    @Autowired
    private ConfigStewardService configStewardService;

    /**
     * 司机信息查找
     *
     * @return
     */
    @RequiresPermissions("config:steward:find")
    @GetMapping(name = "司机信息查找", value = "/config/steward/find")
    public Result<IPage<ConfigSteward>> find(ConfigStewardPageForm form) {
        IPage<ConfigSteward> page = configStewardService.pageForm(form);
        return Result.<IPage<ConfigSteward>>success().setData(page);
    }

    /**
     * 导入司机数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("config:steward:import")
    @PostMapping(name = "导入司机数据", value = "/config/steward/import")
    public Result<ConfigSteward> importExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<ConfigSteward> excelList = ExcelUtils.read(inputStream, ConfigSteward.class, setRosterIntegerMapping());
        configStewardService.importExcel(excelList);
        return Result.success();
    }

    private Map<Integer, String> setRosterIntegerMapping() {
        Map<Integer, String> mapping = new LinkedHashMap<>();
        mapping.put(0, "railway");
        mapping.put(1, "locomotiveDepot");
        mapping.put(2, "workshop");
        mapping.put(3, "jobNum");
        mapping.put(4, "name");
        mapping.put(5, "pos");
        mapping.put(6, "tel");
        return mapping;
    }
}
