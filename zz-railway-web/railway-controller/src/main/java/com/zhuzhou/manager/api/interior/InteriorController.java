package com.zhuzhou.manager.api.interior;

import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.form.interior.IdxPhaseAnalysisForm;
import com.zhuzhou.form.interior.LkjPhaseAnalysisForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.spi.interior.InteriorService;
import com.zhuzhou.spi.lkj.LkjIndexService;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.spi.video.LkjService;
import com.zhuzhou.spi.video.Mp4Service;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chenzeting
 * @Date 2019-05-20
 * @Description:
 **/
@Controller
public class InteriorController {

    @Autowired
    private LkjService lkjService;

    @Autowired
    private InteriorService interiorService;

    @Value("${url.lkj}")
    private String LKJ_URL;

    /**
     * 分析lkj项点接口
     *
     * @param form
     * @return
     */
//    @RequiresPermissions("api:lkj:phase")
    @PostMapping(name = "分析lkj项点接口", value = "/api/lkj/phase")
    public Result analysisLkjPhase(LkjPhaseAnalysisForm form) {
        lkjService.analysisLkjPhase(form.getList(), form.getId());
        return Result.success();
    }

    /**
     * 分析idx项点，手势项点接口，机械间
     *
     * @return
     */
//    @RequiresPermissions("api:idx:phase")
    @PostMapping(name = "分析idx项点，手势项点接口，机械间", value = "/api/idx/phase")
    public Result find(IdxPhaseAnalysisForm form) {
        interiorService.analysisIdx(form);
        return Result.success();
    }

    @Autowired
    private LkjIndexService lkjIndexService;
    @Autowired
    private Mp4Service mp4Service;
    @Autowired
    private IdxIndexService idxIndexService;

    @RequiresPermissions("test:import")
    @PostMapping(name = "测试/导入", value = "/test/import")
    public Result importExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<Lkj> excelList = ExcelUtils.read(inputStream, Lkj.class, setRosterIntegerMapping());
        lkjService.analysisLkjPhase(excelList, "");
        return Result.success();
    }

    private Map<Integer, String> setRosterIntegerMapping() {
        Map<Integer, String> mapping = new LinkedHashMap<>();
        mapping.put(0, "lkjNo");
        mapping.put(1, "eventItem");
        mapping.put(2, "time");
        mapping.put(3, "kiloMeter");
        mapping.put(4, "other");
        mapping.put(5, "distance");
        mapping.put(6, "signalMachine");
        mapping.put(7, "signals");
        mapping.put(8, "speed");
        mapping.put(9, "rateLimit");
        mapping.put(10, "zeroBit");
        mapping.put(11, "transaction");
        mapping.put(12, "frontBehind");
        mapping.put(13, "pipePressure");
        mapping.put(14, "cylinderPressure");
        mapping.put(15, "speedElectricity");
        mapping.put(16, "cylinder1");
        mapping.put(17, "cylinder2");
        return mapping;
    }
}
