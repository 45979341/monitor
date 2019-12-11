package com.zhuzhou.manager.api.video;


import com.zhuzhou.dto.video.RecordStatusResult;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.video.RecordStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-04-02
 * Description:
 */
@Controller
public class RecordStatusController {
    @Autowired
    private RecordStatusService recordStatusService;

    @GetMapping("/record/getStatusNum")
    public Result getStatusNum() {
        RecordStatusResult record = recordStatusService.getCountForRecord();
        return Result.success().setData(record);
    }
}
