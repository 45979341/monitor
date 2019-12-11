package com.zhuzhou.manager.api.ai;

import com.zhuzhou.entity.ai.Front;
import com.zhuzhou.entity.ai.Phone;
import com.zhuzhou.form.ai.RecordIdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.ai.PhoneService;

import java.util.List;

/**
 * <p>
 * ai识别-打手机 前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-08-06
 * Description:
 * @menu 处理列表
 */
@Controller
public class PhoneController {
    @Autowired
    private PhoneService phoneService;

    /**
     * 打手机列表
     * @return
     */
    @RequiresPermissions("phone:list")
    @GetMapping(name = "打手机列表", value = "/phone/list")
    public Result<List<Phone>> list(RecordIdForm form) {
        Phone phone = new Phone();
        phone.setRecordId(form.getRecordId());
        List<Phone> list = phoneService.list(/*phone*/);
        return Result.<List<Phone>>success().setData(list);
    }
}
