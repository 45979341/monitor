package com.zhuzhou.impl.shiro;

import lombok.Getter;
import org.apache.shiro.authc.AuthenticationToken;

@Getter
public class JwtToken implements AuthenticationToken {

    /**
     * 密钥token
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
