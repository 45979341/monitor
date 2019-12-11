package com.zhuzhou.manager.api.video;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.framework.utils.stomp.ServletUtils;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.lkj.LkjIndexService;
import com.zhuzhou.spi.video.LkjService;
import com.zhuzhou.spi.video.Mp4Service;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-03-20
 * Description:
 */
@Controller
public class LkjController {
    @Autowired
    private LkjService lkjService;
    @Autowired
    private LkjIndexService lkjIndexService;

    @Value("${url.lkj}")
    private String LKJ_URL;

    /**
     * 获取lkj数据
     * @param idForm
     */
    @RequiresPermissions("lkj:list")
    @GetMapping(name = "获取lkj数据", value = "/lkj/list")
    public Result<Object> list(IdForm idForm) {
        LkjIndex lkj = lkjIndexService.getById(idForm.getId());
        if (lkj == null) {
            return Result.success();
        }
        List<Lkj> list = lkjIndexService.remoteList(lkj.getFilePath());
        return Result.success().setData(list);
    }

    /**
     * lkj运行曲线
     * @param idForm
     */
    @RequiresPermissions("lkj:curve")
    @GetMapping(name = "lkj运行曲线", value = "/lkj/curve")
    public Result<Object> curve(IdForm idForm) {
        LkjIndex lkjIndex = lkjIndexService.getById(idForm.getId());
        if (lkjIndex == null) {
            return Result.success();
        }
        List<Lkj> list = lkjIndexService.remoteList(lkjIndex.getFilePath());
        if (CollectionUtils.isEmpty(list)) {
            return Result.success();
        }

        //获取第一个数值
        int t = 1;
        List<Lkj> tempList = new ArrayList<>();
        do {
            boolean rateFlag = false;
            Lkj temp = list.get(t);
            String tempRateLimit = temp.getRateLimit();
            if ("0".equals(tempRateLimit)) {
            } else {
                rateFlag = true;
            }
            if (rateFlag) {
                tempList.add(temp);
            }
            ++t;
        } while (list.size() > t + 1);

        return Result.success().setData(tempList);

    }

    /**
     * lkj全程记录导出
     */
//    @RequiresPermissions("lkj:export")
    @GetMapping(name = "lkj全程记录导出", value = "/lkj/export")
    public ResponseEntity<byte[]> export(IdForm idForm) throws IOException {
        LkjIndex lkjIndex = lkjIndexService.getById(idForm.getId());
        if (lkjIndex == null) {
            return null;
        }
        String filePath = lkjIndex.getFilePath();
        List<Lkj> list = lkjIndexService.remoteList(filePath);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //导出表格
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(i + 1);
        ExcelUtils.write(list, setLkjMapping(), "LKJ全程记录", stream);
        ResponseEntity<byte[]> response = ServletUtils.getFileResponse(fileName + ".xls", stream.toByteArray());
        stream.close();
        return response;
    }

    private Map<String, String> setLkjMapping() {
        Map<String, String> mapping = new LinkedHashMap<>();
        mapping.put("序号", "lkjNo");
        mapping.put("事件", "eventItem");
        mapping.put("时间", "time");
        mapping.put("公里标", "kiloMeter");
        mapping.put("其它", "other");
        mapping.put("距离", "distance");
        mapping.put("信号机", "signalMachine");
        mapping.put("信号", "signals");
        mapping.put("速度", "speed");
        mapping.put("限速", "rateLimit");
        mapping.put("零位", "zeroBit");
        mapping.put("牵引", "transaction");
        mapping.put("前后", "frontBehind");
        mapping.put("管压", "pipePressure");
        mapping.put("缸压", "cylinderPressure");
        mapping.put("转速电流", "speedElectricity");
        mapping.put("均缸1", "cylinder1");
        mapping.put("均缸2", "cylinder2");
        return mapping;
    }

    @Value("${file.uri}")
    private String uri;

    @Autowired
    private Mp4Service mp4Service;

    @GetMapping(name = "lkj test", value = "/lkj/test")
    public Result<Object> test(IdForm idForm) throws IOException {
        Lkj lkj = new Lkj();
        lkj.setRecordId(idForm.getId());
        LkjIndex index = lkjIndexService.getById(idForm.getId());
        List<Lkj> list = lkjIndexService.remoteList(index.getFilePath());
        Mp4 mp4 = new Mp4();
        mp4.setRecordId(idForm.getId());
        List<Mp4> mp4List = mp4Service.list(mp4);
        LkjHead lkjHead = lkjService.processLkj(list);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        ExcelUtils.write(list, setLkjMapping(), "LKJ全程记录", stream);
//        ResponseEntity<byte[]> response = ServletUtils.getFileResponse("xx.xls", stream.toByteArray());
//        stream.close();
//        return response;
        lkjService.analysisPhase(idForm.getId(), list, index.getFilePath(), mp4List, lkjHead);
        return Result.success();
    }


    /**
     * 分析lkj项点接口
     * @param form
     * @return
     */
//    @RequiresPermissions("lkj:data:test")
    @GetMapping(name = "分析lkj项点接口", value = "/lkj/data/test")
    public Result analysisLkjPhase(IdForm form) {
        Lkj lkj = new Lkj();
        lkj.setRecordId(form.getId());
        LkjIndex index = lkjIndexService.getById(form.getId());
        List<Lkj> list = lkjIndexService.remoteList(index.getFilePath());
        lkjService.analysisLkjPhase(list, form.getId());
        return Result.success();
    }
}
