package com.zhuzhou.manager.api.sys;

import com.zhuzhou.entity.sys.SysMenu;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.sys.menu.SysMenuUpdateForm;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.impl.shiro.JwtUtil;
import com.zhuzhou.manager.util.TreeUtils;
import com.zhuzhou.spi.sys.SysMenuService;
import com.zhuzhou.spi.sys.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 菜单权限表 前端控制器
 *
 * @Author: chenzeting
 * Date:     2019-05-08
 * Description:
 * @menu 菜单模块
 */
@Controller
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 菜单查找
     *
     * @return
     */
//    @RequiresAuthentication
    @RequiresPermissions("sys:menu:list")
    @GetMapping(name = "菜单查找",value = "/sys/menu/list")
    public Result page() {
        List<SysMenu> list = sysMenuService.menuList();
        List<SysMenu> menuTree = TreeUtils.getChildPerms(list, 0);
        return Result.success().setData(menuTree);
    }

    /**
     * 菜单添加
     *
     * @param form
     * @return
     */
    @RequiresPermissions("sys:menu:add")
    @PostMapping(name = "菜单添加",value = "/sys/menu/add")
    public Result add(SysMenuUpdateForm form, HttpServletRequest request) {
        SysUser curUser = JwtUtil.getCurUser(request, sysUserService);
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(form, menu);
        Optional.of(curUser).ifPresent(s -> {
            curUser.setCreateBy(s.getUserName());
        });
        sysMenuService.save(menu);
        return Result.success().setData(menu);
    }

    /**
     * 菜单编辑
     *
     * @param form
     * @return
     */
    @RequiresPermissions("sys:menu:edit")
    @PostMapping(name = "菜单编辑",value = "/sys/menu/edit")
    public Result edit(SysMenuUpdateForm form) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(form, menu);
        sysMenuService.updateById(menu);
        return Result.success().setData(menu);
    }

    /**
     * 菜单删除
     * @param form
     * @return
     */
    @RequiresPermissions("sys:menu:del")
    @PostMapping(name = "菜单删除",value = "/sys/menu/del")
    public Result delete(IdForm form) {
        sysMenuService.delete(form.getId());
        return Result.success();
    }


    /**
     * 查询当前用户菜单树
     *
     * @param request
     * @return
     */
    @RequiresPermissions("sys:menu:menuTree")
    @GetMapping(name = "查询当前用户菜单树",value = "/sys/menu/menuTree")
    public Result menuTree(HttpServletRequest request) {
        SysUser curUser = JwtUtil.getCurUser(request, sysUserService);
        List<SysMenu> sysMenu = sysMenuService.menuTree(curUser.getId());
        List<SysMenu> menuTree = TreeUtils.getChildPerms(sysMenu, 0);
        return Result.success().setData(menuTree);
    }
}
