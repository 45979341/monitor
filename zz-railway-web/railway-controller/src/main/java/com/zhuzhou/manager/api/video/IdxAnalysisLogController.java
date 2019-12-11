package com.zhuzhou.manager.api.video;

import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.entity.idx.IdxAnalysisLog;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.entity.video.Idx;
import com.zhuzhou.form.video.IdxAnalysisLogAddForm;
import com.zhuzhou.form.video.IdxAnalysisLogDetailForm;
import com.zhuzhou.form.video.IdxAnalysisLogEditForm;
import com.zhuzhou.form.video.IdxAnalysisLogListForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ExcelUtils;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.framework.utils.stomp.ServletUtils;
import com.zhuzhou.framework.utils.stomp.UUIDUtils;
import com.zhuzhou.spi.video.IdxAnalysisLogService;
import com.zhuzhou.spi.video.Mp4Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-05-17
 * Description:
 * @menu 视频信息
 */
@Controller
@Slf4j
public class IdxAnalysisLogController {
    @Autowired
    private IdxAnalysisLogService idxAnalysisLogService;
    @Autowired
    private Mp4Service mp4Service;

    @Value("${file.source}")
    private String file;

    @Value("${file.uri}")
    private String uri;

    /**
     * 项点签功能添加
     * @param form
     * @return
     */
    @RequiresPermissions("idx:analysis:add")
    @PostMapping(name = "项点签功能添加", value = "/idx/analysis/add")
    public Result add (IdxAnalysisLogAddForm form, @OAuth SysUser sysUser) {
        IdxAnalysisLog idxAnalysisLog = new IdxAnalysisLog();
        Date startTime = form.getStartTime();
        if (startTime != null) {
            Idx idx = mp4Service.startTimeLocation(form.getIdxId(), form.getStartTime());
            if (idx != null) {
                idxAnalysisLog.setDriverNum(idx.getDriverNum());
                idxAnalysisLog.setAssisDriverNum(idx.getAssisDriverNum());
            }
        }
        String base64Pic = form.getBase64Pic();
        //如果有传图片  处理图片存入服务器
        if (StringUtils.isNotEmpty(base64Pic)) {
            int i = base64Pic.indexOf(SymbolConst.COMMA);
            if (i > 0) {
                String base64 = base64Pic.substring(i);
                byte[] bytes = Base64.decodeBase64(base64);
                String fileName = UUIDUtils.randomUUID();
                try {
                    FileUtils.writeByteArrayToFile(new File(file + File.separator + fileName + ".jpeg"), bytes);
                    idxAnalysisLog.setPic(uri + "/source/" + fileName + ".jpeg");
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("文件写入异常", e);
                    throw new ApplicationException(ErrorResponseMsgEnum.FILE_COPY_ERROR);
                }
            }
        }
        ReflectionUtils.copyProperties(idxAnalysisLog, form);
        idxAnalysisLog.setCreateBy(sysUser.getUserName());
        idxAnalysisLogService.add(idxAnalysisLog, form.getPhaseId(), form.getIllegal());
        return Result.success();
    }

    /**
     * 日志分析记录详情
     * @param form
     * @return
     */
    @RequiresPermissions("idx:analysis:detail")
    @GetMapping(name = "日志分析记录详情", value = "/idx/analysis/detail")
        public Result detail (IdxAnalysisLogDetailForm form) {
        IdxAnalysisLog detail = idxAnalysisLogService.getOne(form);
        return Result.success().setData(detail);
    }

    /**
     * 日志分析记录修改
     * @param form
     * @return
     */
    @RequiresPermissions("idx:analysis:edit")
    @PostMapping(name = "报警记录列表", value = "/idx/analysis/edit")
    public Result edit (IdxAnalysisLogEditForm form) {
        IdxAnalysisLog idxAnalysisLog = new IdxAnalysisLog();
        ReflectionUtils.copyProperties(idxAnalysisLog, form);
        idxAnalysisLogService.updateById(idxAnalysisLog);
        return Result.success();
    }

    /**
     * 日志分析列表
     * @param form
     * @return
     */
    @RequiresPermissions("idx:analysis:list")
    @GetMapping(name = "日志分析列表", value = "/idx/analysis/list")
    public Result list (IdxAnalysisLogListForm form, @OAuth SysUser sysUser) {
        String createBy = form.getCreateBy();
        if ("1".equals(createBy)) {
            form.setCreateBy(sysUser.getUserName());
        } else {
            form.setCreateBy(null);
        }
        List<IdxAnalysisLog> list = idxAnalysisLogService.list(form);
        return Result.success().setData(list);
    }

    /**
     * 日志分析列表
     * @param form
     * @return
     */
//    @RequiresPermissions("idx:analysis:export")
    @GetMapping(name = "日志分析列表", value = "/idx/analysis/export")
    public ResponseEntity<byte[]> export (IdxAnalysisLogListForm form, @OAuth SysUser sysUser) throws IOException {
        String createBy = form.getCreateBy();
        if ("1".equals(createBy)) {
            form.setCreateBy(sysUser.getUserName());
        } else {
            form.setCreateBy(null);
        }
        List<IdxAnalysisLog> list = idxAnalysisLogService.list(form);
        //导出表格
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ExcelUtils.write(list, setAnalysisMapping(), "分析日志报表", stream);
        ResponseEntity<byte[]> response = ServletUtils.getFileResponse( "分析日志报表.xls", stream.toByteArray());
        stream.close();
        return response;
    }

    private Map<String, String> setAnalysisMapping() {
        Map<String,String> mapping = new LinkedHashMap<>();
        mapping.put("序号", "id");
        mapping.put("司机号", "driverNum");
        mapping.put("副司机号", "assisDriverNum");
        mapping.put("车次", "trainNum");
        mapping.put("机车号", "cabNum");
        mapping.put("分析项目", "item");
        mapping.put("分析内容", "content");
        mapping.put("存在问题", "question");
        mapping.put("分析意见", "opinion");
        mapping.put("创建人", "createBy");
        return mapping;
    }
}
