package com.zhuzhou.impl.shiro;

import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.spi.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class JwtShiroRealm extends AuthorizingRealm {

    protected SysUserService sysUserService;

    public JwtShiroRealm(SysUserService sysUserService){
        this.sysUserService = sysUserService;
        //这里使用我们自定义的Matcher
        this.setCredentialsMatcher(new JwtCredentialsMatcher());
    }

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sysUser = (SysUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //角色
//        List<String> roles = sysUserService.getRoleByUserId(sysUser.getId());
//        simpleAuthorizationInfo.addRoles(roles);
        //权限 permission集合不能有空字符串
        List<String> permission = sysUserService.getPermissionByUserId(sysUser.getId());
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;

    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String loginName = JwtUtil.getLoginName(token);
        if (loginName == null) {
            throw new ApplicationException(ErrorResponseMsgEnum.TOKEN_INVALID);
        }
        SysUser user = sysUserService.getByLoginName(loginName);
        if (user == null) {
            throw new ApplicationException(ErrorResponseMsgEnum.USER_NOT_EXISTS);
        }
        return new SimpleAuthenticationInfo(user, user.getSalt(), this.getName());
    }
}
