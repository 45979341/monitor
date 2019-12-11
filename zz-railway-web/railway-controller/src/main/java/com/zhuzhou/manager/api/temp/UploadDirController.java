package com.zhuzhou.manager.api.temp;

import com.zhuzhou.entity.temp.UploadDir;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.temp.UploadDirService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-06-21
 * Description:
 */
@Controller
public class UploadDirController {
    @Autowired
    private UploadDirService uploadDirService;

    @RequiresPermissions("upload:add")
    @PostMapping(name = "upload add", value = "/upload/add")
    public Result add (@RequestBody UploadDir dir) {
        dir.setStatus(0);
        dir.setIdxStatus(0);
        uploadDirService.save(dir);
        return Result.success();
    }
}
