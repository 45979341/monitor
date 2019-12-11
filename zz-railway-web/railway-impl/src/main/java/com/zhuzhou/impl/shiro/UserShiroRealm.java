package com.zhuzhou.impl.shiro;

import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.spi.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

/**
 * @Author chenzeting
 * @Date 2019-09-17
 * @Description:
 **/
@Slf4j
@Service
public class UserShiroRealm extends AuthorizingRealm {

    private SysUserService sysUserService;

    public UserShiroRealm(SysUserService sysUserService) {

        this.sysUserService = sysUserService;
        this.setCredentialsMatcher(new UserCredentialsMatcher());
    }
    /**
     *  找它的原因是这个方法返回true
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
    /**
     *  这一步我们根据token给的用户名，去数据库查出加密过用户密码，然后把加密后的密码和盐值一起发给shiro，让它做比对
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        String loginName = upToken.getUsername();
        SysUser user = sysUserService.getByLoginName(loginName);
        if(user == null) {
            throw new ApplicationException(ErrorResponseMsgEnum.USER_OR_PASS_ERROR);
        }
        if (user.getStatus() == 0) {
            throw new ApplicationException(ErrorResponseMsgEnum.USER_DISABLED);
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), "dbRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
