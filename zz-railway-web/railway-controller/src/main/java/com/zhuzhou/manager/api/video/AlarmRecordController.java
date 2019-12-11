package com.zhuzhou.manager.api.video;

import com.zhuzhou.entity.video.AlarmRecord;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.spi.video.AlarmRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-05-22
 * Description:
 */
@Controller
public class AlarmRecordController {
    @Autowired
    private AlarmRecordService alarmRecordService;

    /**
     * 报警记录列表
     * @param form
     * @return
     */
    @RequiresPermissions("idx:alarm:list")
    @GetMapping(name = "报警记录列表", value = "/idx/alarm/list")
    public Result list(IdForm form) {
        AlarmRecord ar = new AlarmRecord();
        ar.setRecordId(form.getId());
        List<AlarmRecord> list = alarmRecordService.list(ar);
        return Result.success().setData(list);
    }
}
