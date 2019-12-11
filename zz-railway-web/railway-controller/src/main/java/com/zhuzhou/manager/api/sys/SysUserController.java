package com.zhuzhou.manager.api.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.consts.SymbolConst;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.sys.user.SysUserAddForm;
import com.zhuzhou.form.sys.user.SysUserEditForm;
import com.zhuzhou.form.sys.user.SysUserEditPasswordForm;
import com.zhuzhou.form.sys.user.SysUserLoginForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.impl.shiro.JwtUtil;
import com.zhuzhou.page.sys.UserPageForm;
import com.zhuzhou.spi.sys.SysUserService;
import com.zhuzhou.vo.sys.SysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @Author: chenzeting
 * Date:     2019-05-08
 * Description:
 */
@Slf4j
@Controller
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录功能
     *
     * @param form
     * @return
     */
//    @RequiresPermissions("sys:user:login")
    @PostMapping(name = "用户登录", value = "/sys/user/login")
    public Result login(SysUserLoginForm form, HttpServletResponse response) {
        SysUser sysUser = new SysUser();
        ReflectionUtils.copyProperties(sysUser, form);
        sysUserService.login(sysUser, response);
        return Result.success();
    }

    /**
     * 退出功能
     *
     * @return
     */
    @RequiresPermissions("sys:user:logout")
    @PostMapping(name = "用户退出", value = "/sys/user/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    /**
     * 用户注册
     *
     * @param form password 前端需要传密码 MD5
     * @return
     */
    @RequiresPermissions("sys:user:register")
    @PostMapping(name = "用户注册", value = "/sys/user/register")
    public Result register(SysUserAddForm form, HttpServletRequest request) {
        SysUser curUser = JwtUtil.getCurUser(request, sysUserService);
        SysUser user = new SysUser();
        ReflectionUtils.copyProperties(user, form);
        user.setRoleIds(StringUtils.join(form.getRoleIds(), SymbolConst.COMMA));
        Optional.ofNullable(curUser).ifPresent(s -> {
            user.setCreateBy(s.getUserName());
        });

        sysUserService.register(user);
        return Result.success().setData(user);
    }

    /**
     * 用户基本信息修改
     *
     * @param form password 前端需要传密码 MD5
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @PostMapping(name = "用户基本信息修改", value = "/sys/user/edit")
    public Result edit(SysUserEditForm form, HttpServletRequest request) {
        SysUser curUser = JwtUtil.getCurUser(request, sysUserService);
        SysUser user = new SysUser();
        ReflectionUtils.copyProperties(user, form);
        user.setRoleIds(StringUtils.join(form.getRoleIds(), SymbolConst.COMMA));

        Optional.ofNullable(curUser).ifPresent(s -> {
            user.setUpdateBy(s.getUserName());
        });
        sysUserService.edit(user);
        return Result.success();
    }

    /**
     * 用户密码修改
     *
     * @param form password 前端需要传密码 MD5
     * @return
     */
    @RequiresPermissions("sys:user:pass")
    @PostMapping(name = "用户密码修改", value = "/sys/user/pass")
    public Result editPass(SysUserEditPasswordForm form) {
        SysUser user = new SysUser();
        ReflectionUtils.copyProperties(user, form);
        sysUserService.editPass(user);
        return Result.success();
    }

    /**
     * 用户查找
     *
     * @return
     */
    @RequiresPermissions("sys:user:find")
    @GetMapping(name = "用户查找", value = "/sys/user/find")
    public Result page(UserPageForm form) {
        IPage page = sysUserService.pageForm(form);
        List<SysUser> records = page.getRecords();
        List<SysUserVo> userList = records.stream().map(sysUser -> {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(sysUser, sysUserVo);
            String roleIds = sysUser.getRoleIds();
            String[] split = roleIds.split(SymbolConst.COMMA);
            sysUserVo.setRoleIds(split);
            return sysUserVo;
        }).collect(Collectors.toList());
        page.setRecords(userList);
        return Result.success().setData(page);
    }

    /**
     * 用户删除
     *
     * @return
     */
    @RequiresPermissions("sys:user:del")
    @PostMapping(name = "用户删除", value = "/sys/user/del")
    public Result<Object> edit(IdForm form) {
        sysUserService.removeById(form.getId());
        return Result.success();
    }

    /**
     * 用户详情
     *
     * @return {@link SysUser}
     */
    @RequiresUser
    @RequiresPermissions(value = "sys:user:detail")
    @GetMapping(name = "用户详情", value = "/sys/user/detail")
    public Result detail(@OAuth SysUser sysUser) {
        return Result.success().setData(sysUser);
    }
}