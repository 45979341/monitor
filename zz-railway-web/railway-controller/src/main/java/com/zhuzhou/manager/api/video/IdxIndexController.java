package com.zhuzhou.manager.api.video;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.video.IdxIndexListForm;
import com.zhuzhou.form.video.IdxListForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.framework.utils.stomp.ServletUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.video.IdxIndexService;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-05-17
 * Description:
 * @menu 视频分析首页模块
 */
@Controller
public class IdxIndexController {
    @Autowired
    private IdxIndexService idxIndexService;

    /**
     * lkj追踪idx信息
     */
    @RequiresPermissions("idx:index")
    @GetMapping(name = "lkj追踪idx信息", value = "/idx/index")
    public Result<IPage<IdxIndex>> list(IdxIndexListForm form) {
        IPage<IdxIndex> page;
        IdxIndex idxIndex = new IdxIndex();
        ReflectionUtils.copyProperties(idxIndex, form);
        if (StringUtils.isEmpty(form.getId())) {
            List<IdxIndex> list = idxIndexService.idxIndexNone(idxIndex, form.offset(), form.getSize());
            form.setRecords(list);
            Long total = idxIndexService.idxIndexNoneCount(idxIndex);
            form.setTotal(Optional.ofNullable(total).orElse(0L));
            page = form;
        } else {
            form.setRecordId(form.getId());
            page = idxIndexService.pageForm(form);
        }
        return Result.<IPage<IdxIndex>>success().setData(page);
    }

    /**
     * idx记录详情
     * @param idForm
     * @return
     */
    @RequiresPermissions("idx:index:detail")
    @GetMapping(name = "idx记录详情", value = "/idx/index/detail")
    public Result upload(IdForm idForm) {
        //上传后的视频文件
        IdxIndex detail = idxIndexService.getById(idForm.getId());
        return Result.success().setData(detail);
    }


    /**
     * idx实时数据
     * @return
     */
    @RequiresPermissions("idx:list")
    @GetMapping(name = "idx实时数据", value = "/idx/list")
    public Result find(IdxListForm form) {
        List<Idx> list = idxIndexService.remoteList(form.getId());
        return Result.success().setData(list);
    }


    /**
     * idx实时数据
     * @return
     */
//    @RequiresPermissions("idx:test:export")
    @GetMapping(name = "lkj追踪idx信息", value = "/idx/test/export")
    public ResponseEntity<byte[]> export(IdxListForm form) throws IOException {
        List<Idx> list = idxIndexService.remoteList(form.getId());
        //导出表格
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ExcelUtils.write(list, setLkjMapping(), "LKJ全程记录", stream);
        ResponseEntity<byte[]> response = ServletUtils.getFileResponse("xx.xls", stream.toByteArray());
        stream.close();
        return response;
    }

    private Map<String, String> setLkjMapping() {
        Map<String, String> mapping = new LinkedHashMap<>();
        mapping.put("中央处理平台时间", "platformTime");
        mapping.put("中央处理平台车号", "platformWagonNum");
        mapping.put("车次字母部分", "alpTrainNum");
        mapping.put("车次数字部分", "digTrainNum");
        mapping.put("车站号", "stationNum");
        mapping.put("车站名", "stationName");
        mapping.put("司机号", "driverNum");
        mapping.put("副司机号", "assisDriverNum");
        mapping.put("实际交路号", "crossRoadNum");
        mapping.put("客/货", "goodsNum");
        mapping.put("速度", "speed");
        mapping.put("限速", "rateLimit");
        mapping.put("机车信号", "cabSignal");
        mapping.put("信号机编号", "signalNum");
        mapping.put("信号机种类", "signalType");
        mapping.put("公里标", "kilometre");
        mapping.put("列车管压力", "trainPipePressure");
        mapping.put("装置状态", "deviceStatus");
        return mapping;
    }
}
