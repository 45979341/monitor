package com.zhuzhou.manager.api.video;


import com.zhuzhou.entity.video.UploadRecord;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.video.UploadRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-03-20
 * Description:
 */
@Controller
public class UploadRecordController {
    @Autowired
    private UploadRecordService uploadRecordService;

    /**
     * 视频文件上传
     *
     * @param files
     * @return
     */
    @RequiresPermissions("upload")
    @PostMapping(name = "视频文件上传", value = "/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile[] files) {
        //上传后的视频文件
        uploadRecordService.upload(files);
        return Result.success();
    }

    /**
     * 上传记录详情
     *
     * @param idForm
     * @return
     */
    @RequiresPermissions("upload:detail")
    @GetMapping(name = "上传记录详情", value = "/upload/detail")
    public Result upload(IdForm idForm) {
        //上传后的视频文件
        UploadRecord detail = uploadRecordService.getById(idForm.getId());
        return Result.success().setData(detail);
    }
}
