package com.zhuzhou.manager.api.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.sys.SysRole;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.sys.role.SysRoleAddForm;
import com.zhuzhou.form.sys.role.SysRoleEditForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.IPv4Util;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.page.sys.RolePageForm;
import com.zhuzhou.spi.sys.SysRoleService;
import com.zhuzhou.spi.sys.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-05-08
 * Description:
 */
@Controller
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 角色添加
     *
     * @param form
     * @return
     */
    @RequiresPermissions("sys:role:add")
    @PostMapping(name = "角色添加", value = "/sys/role/add")
    public Result add(SysRoleAddForm form, @OAuth SysUser sysUser) {
        SysRole role = new SysRole();
        ReflectionUtils.copyProperties(role, form);
        //ip转换
        role.setServerIp(IPv4Util.ipToInt(form.getServerIp()));
        role.setEServerIp(IPv4Util.ipToInt(form.getEServerIp()));
        role.setStatus(0);
        role.setCreateBy(sysUser.getUserName());
        sysRoleService.save(role);
        return Result.success();
    }

    /**
     * 角色修改
     *
     * @param form password 前端需要传密码 MD5
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @PostMapping(name = "角色修改", value = "/sys/role/edit")
    public Result edit(SysRoleEditForm form, @OAuth SysUser sysUser) {
        SysRole role = new SysRole();
        ReflectionUtils.copyProperties(role, form);
        //ip转换
        role.setServerIp(IPv4Util.ipToInt(form.getServerIp()));
        role.setEServerIp(IPv4Util.ipToInt(form.getEServerIp()));
        role.setUpdateBy(sysUser.getUserName());
        sysRoleService.updateById(role);
        return Result.success();
    }

    /**
     * 角色查找
     *
     * @return
     */
    @RequiresPermissions("sys:role:find")
    @GetMapping(name = "角色查找", value = "/sys/role/find")
    public Result page(RolePageForm form) {
        IPage<SysRole> page = sysRoleService.pageForm(form);
        for (SysRole role : page.getRecords()) {
            role.setTempServerIp(IPv4Util.intToIp(role.getServerIp()));
            role.setTempEServerIp(IPv4Util.intToIp(role.getEServerIp()));
        }
        return Result.success().setData(page);
    }


    /**
     * 角色删除
     *
     * @return
     */
    @RequiresPermissions("sys:role:del")
    @PostMapping(name = "角色删除", value = "/sys/role/del")
    public Result<Object> edit(IdForm form) {
        SysUser sysUser = new SysUser();
        sysUser.setRoleIds(form.getId());
        SysUser one = sysUserService.getOne(sysUser);
        if (one == null) {
            sysRoleService.removeById(form.getId());
        } else {
            throw new ApplicationException(ErrorResponseMsgEnum.ROLE_DEL_EXISTS);
        }
        return Result.success();
    }

    /**
     * 角色列表
     *
     * @return
     */
    @GetMapping(name = "角色列表",value = "/sys/role/list")
    public Result list() {
        List<SysRole> list = sysRoleService.list();
        return Result.success().setData(list);
    }
}
