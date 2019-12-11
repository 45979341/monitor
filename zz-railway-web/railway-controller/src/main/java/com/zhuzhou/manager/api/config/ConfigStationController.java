package com.zhuzhou.manager.api.config;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.config.ConfigStation;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.page.config.ConfigStationPageForm;
import com.zhuzhou.spi.config.ConfigStationService;
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
 * 车站表控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-04-11
 * Description:
 * @menu 配置管理
 */
@Controller
public class ConfigStationController {
    @Autowired
    private ConfigStationService configStationService;

    /**
     * 车站信息查找
     *
     * @return
     */
    @RequiresPermissions("config:station:find")
    @GetMapping(name = "车站信息查找", value = "/config/station/find")
    public Result<IPage<ConfigStation>> find(ConfigStationPageForm form) {
        IPage<ConfigStation> page = configStationService.pageForm(form);
        return Result.<IPage<ConfigStation>>success().setData(page);
    }

    /**
     * 导入车站数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequiresPermissions("config:station:import")
    @PostMapping(name = "导入车站数据", value = "/config/station/import")
    public Result<ConfigStation> importExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<ConfigStation> excelList = ExcelUtils.read(inputStream, ConfigStation.class, setRosterIntegerMapping());
        configStationService.importExcel(excelList);
        return Result.success();
    }

    private Map<Integer, String> setRosterIntegerMapping() {
        Map<Integer, String> mapping = new LinkedHashMap<>();
        mapping.put(0, "roadNum");
        mapping.put(1, "stationNum");
        mapping.put(2, "stationName");
        mapping.put(3, "lineNum");
        mapping.put(4, "tmisNum");
        mapping.put(5, "upEnter");
        mapping.put(6, "upExit");
        mapping.put(7, "downEnter");
        mapping.put(8, "downExit");
        mapping.put(9, "g0");
        mapping.put(10, "g1");
        mapping.put(11, "g2");
        mapping.put(12, "g3");
        return mapping;
    }
}
