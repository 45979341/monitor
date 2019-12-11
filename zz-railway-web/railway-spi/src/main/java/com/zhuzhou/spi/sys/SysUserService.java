package com.zhuzhou.spi.sys;

import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.spi.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-08
 */
public interface SysUserService extends BaseService<SysUser> {

    /**
     * 用户登录
     * @param sysUser
     * @param response
     */
    void login (SysUser sysUser, HttpServletResponse response);

    /**
     * 注册用户
     * @param sysUser
     */
    void register (SysUser sysUser);

    /**
     * 根据名称查询用户
     * @param loginName
     * @return
     */
    SysUser getByLoginName(String loginName);

    /**
     * 修改用户信息
     * @param user
     */
    void edit(SysUser user);

    /**
     * 修改用户密码
     * @param user
     */
    void editPass(SysUser user);

    /**
     * 根据用户id获取其权限
     * @param userId
     * @return
     */
    List<String> getPermissionByUserId(String userId);

    /**
     * 根据用户id获取其角色
     * @param userId
     * @return
     */
    List<String> getRoleByUserId(String userId);
}
