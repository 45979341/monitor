package com.zhuzhou.manager.api.ai;


import com.zhuzhou.entity.ai.MachineRoom;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.ai.RecordIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.DateUtils;
import com.zhuzhou.spi.ai.MachineRoomService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-04-11
 * Description:
 */
@Controller
public class MachineRoomController {
    @Autowired
    private MachineRoomService machineRoomService;

    /**
     * 获取机械间列表
     * @return Result
     */
    @RequiresPermissions("room:list")
    @GetMapping(name = "获取机械间列表", value = "/room/list")
    public Result list(RecordIdForm form) {
        MachineRoom machineRoom = new MachineRoom();
        machineRoom.setRecordId(form.getRecordId());
        List<MachineRoom> list = machineRoomService.list(machineRoom);
        return Result.success().setData(list);
    }
}
