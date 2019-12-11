package com.zhuzhou.manager.api.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.dto.system.DeviceInfoResp;
import com.zhuzhou.dto.system.SystemDeptResp;
import com.zhuzhou.entity.system.MonitorDeviceInfo;
import com.zhuzhou.entity.system.SystemDept;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.StringUtils;
import com.zhuzhou.spi.system.MonitorDeviceInfoService;
import com.zhuzhou.spi.system.SystemDeptService;
import com.zhuzhou.vo.exterior.SystemReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统管理模块
 * </p>
 * @Author: wangxiaokuan
 * Date:     2019-12-04
 * Description:
 */
@Controller
public class SystemController {
    @Autowired
    private MonitorDeviceInfoService monitorDeviceInfoService;
    @Autowired
    private SystemDeptService systemDeptService;

    @RequestMapping("/system/deviceList")
    public Result deviceList(SystemReq req) {
        QueryWrapper<MonitorDeviceInfo> qw = new QueryWrapper<>();
        qw.eq(!StringUtils.isEmpty(req.getDeviceId()),"device_id",req.getDeviceId());
        qw.eq("is_delete",0);
        IPage<MonitorDeviceInfo> page1 = new Page<>(req.getCurrentPage(), req.getPageSize());
        IPage<MonitorDeviceInfo> page = monitorDeviceInfoService.page(page1,qw);
        DeviceInfoResp resp = new DeviceInfoResp();
        resp.setCurrentPage(page.getCurrent());
        resp.setData(page.getRecords());
        resp.setPageSize(page.getSize());
        resp.setTotal(page.getTotal());
        return Result.success().setData(resp);
    }

    @RequestMapping("/system/delDevice")
    public Result delDevice(@RequestBody MonitorDeviceInfo info) {
        info.setIsDelete(1);
        monitorDeviceInfoService.saveOrUpdate(info);
        return Result.success();
    }

    @RequestMapping("/system/saveDevice")
    public Result saveDevice(@RequestBody MonitorDeviceInfo info) {
        if (!StringUtils.isEmpty(info.getDeviceId())) {
            Integer id = info.getDeptId();
            if (id != null) {
                Map<String, String> ancestor = systemDeptService.getAncestor(id);
                info.setFirstOrgName(ancestor.get("first"));
                info.setSeconOrgName(ancestor.get("second"));
                info.setThirdOrgName(ancestor.get("third"));
            }
            monitorDeviceInfoService.saveOrUpdate(info);
            return Result.success();
        } else {
            return Result.invalidParam();
        }
    }

    @RequestMapping("/system/getDeptTreeData")
    public Result getDeptTreeData() {
        List<SystemDeptResp> list = new ArrayList<>();
        for (SystemDept dept : systemDeptService.list()) {
            SystemDeptResp resp = new SystemDeptResp();
            resp.setId(dept.getId());
            resp.setPId(dept.getParentId());
            resp.setName(dept.getDeptName());
            list.add(resp);
        }
        return Result.success().setData(list);
    }

    @RequestMapping("/system/getDeviceId")
    public Result getDeviceId () {
        return Result.success().setData(monitorDeviceInfoService.getDiviceId());
    }
}
