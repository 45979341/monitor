package com.zhuzhou.manager.api.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhuzhou.entity.sys.SysPost;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.form.IdForm;
import com.zhuzhou.form.sys.post.PostAddForm;
import com.zhuzhou.form.sys.post.PostEditForm;
import com.zhuzhou.framework.annotation.OAuth;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ReflectionUtils;
import com.zhuzhou.page.sys.PostPageForm;
import com.zhuzhou.spi.sys.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuzhou.spi.sys.SysPostService;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @Author: chenzeting
 * Date:     2019-05-08
 * Description:
 */
@Controller
public class SysPostController {
    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 岗位查找
     * @return
     */
    @RequiresPermissions("sys:post:find")
    @GetMapping(name = "岗位查找",value = "/sys/post/find")
    public Result page (PostPageForm form) {
        IPage<SysPost> page = sysPostService.pageForm(form);
        return Result.success().setData(page);
    }

    /**
     * 岗位添加
     * @return
     */
    @RequiresPermissions("sys:post:add")
    @PostMapping(name = "岗位添加",value = "/sys/post/add")
    public Result<Object> add (PostAddForm form, @OAuth SysUser sysUser) {
        SysPost sysPost = new SysPost();
        ReflectionUtils.copyProperties(sysPost, form);
        sysPost.setCreateBy(sysUser.getUserName());
        sysPostService.save(sysPost);
        return Result.success();
    }

    /**
     * 岗位修改
     * @return
     */
    @RequiresPermissions("sys:post:edit")
    @PostMapping(name = "岗位修改",value = "/sys/post/edit")
    public Result<Object> edit (PostEditForm form, @OAuth SysUser sysUser) {
        SysPost sysPost = new SysPost();
        ReflectionUtils.copyProperties(sysPost, form);
        sysPost.setUpdateBy(sysUser.getUserName());
        sysPostService.updateById(sysPost);
        return Result.success();
    }

    /**
     * 岗位删除
     * @return
     */
    @RequiresPermissions("sys:post:del")
    @PostMapping(name = "岗位删除",value = "/sys/post/del")
    public Result<Object> edit (IdForm form) {
        SysUser sysUser = new SysUser();
        sysUser.setPostId(form.getId());
        SysUser one = sysUserService.getOne(sysUser);
        if (one == null) {
            sysPostService.removeById(form.getId());
        } else {
            throw new ApplicationException(ErrorResponseMsgEnum.ROLE_DEL_EXISTS);
        }
        return Result.success();
    }

    /**
     * 岗位列表
     * @return
     */
    @RequiresPermissions("sys:post:list")
    @GetMapping(name = "岗位列表",value = "/sys/post/list")
    public Result<Object> list () {
        List<SysPost> list = sysPostService.list();
        return Result.success().setData(list);
    }
}
