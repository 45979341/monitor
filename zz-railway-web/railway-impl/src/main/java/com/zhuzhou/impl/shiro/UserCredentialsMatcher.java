package com.zhuzhou.impl.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.exception.ApplicationException;
import com.zhuzhou.framework.exception.ErrorResponseMsgEnum;
import com.zhuzhou.framework.utils.encoder.MD5Encoder;
import com.zhuzhou.spi.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author chenzeting
 * @Date 2019-09-09
 * @Description:
 **/
@Slf4j
public class UserCredentialsMatcher implements CredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //获取鉴权用户
        SysUser curUser = (SysUser) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        if (Optional.ofNullable(curUser).isPresent()) {
            //获取鉴权密码
            String pwd = String.valueOf((char[])authenticationToken.getCredentials());
            if (!MD5Encoder.isPasswordValidDiscuz(curUser.getPassword(), pwd, curUser.getSalt())) {
                throw new ApplicationException(ErrorResponseMsgEnum.USER_OR_PASS_ERROR);
            }
            return true;
        } else {
            throw new ApplicationException(ErrorResponseMsgEnum.USER_OR_PASS_ERROR);
        }
    }
}
