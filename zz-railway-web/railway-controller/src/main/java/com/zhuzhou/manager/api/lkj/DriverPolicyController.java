package com.zhuzhou.manager.api.lkj;

import com.zhuzhou.entity.lkj.DriverPolicy;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.lkj.DriverPolicyService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-05-10
 * Description:
 */
@Controller
public class DriverPolicyController {
    @Autowired
    private DriverPolicyService driverPolicyService;

    /**
     * 保单列表
     *
     * @param idForm
     * @return
     */
    @RequiresPermissions("policy:list")
    @GetMapping(name = "保单列表", value = "/policy/list")
    public Result list(IdForm idForm) {
        DriverPolicy driverPolicy = new DriverPolicy();
        driverPolicy.setLkjIndexId(idForm.getId());
        List<DriverPolicy> list = driverPolicyService.list(driverPolicy);
        return Result.success().setData(list);
    }
}
