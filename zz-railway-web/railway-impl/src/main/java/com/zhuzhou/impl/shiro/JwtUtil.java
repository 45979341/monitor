package com.zhuzhou.impl.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhuzhou.entity.sys.SysUser;
import com.zhuzhou.framework.utils.stomp.ConstUtils;
import com.zhuzhou.spi.sys.SysUserService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtUtil {

    /**
     * 过期时间2小时
     */
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    /**
     * token过期前5分钟有请求就刷新token
     */
    static final long REFRESH_TIME = 5 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    static boolean verify(String token, String loginName, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("loginName", loginName)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("token校验有误", e);
        }
        return false;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getLoginName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("loginName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }

    }

    /**
     * 获得过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpire(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    public static boolean isTokenExpired (String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @param sysUserService
     * @return
     */
    public static SysUser getCurUser(HttpServletRequest request, SysUserService sysUserService) {
        String authorization = request.getHeader(ConstUtils.AUTH_HEADER);
        String loginName = getLoginName(authorization);
        return sysUserService.getByLoginName(loginName);
    }

    /**
     * 生成签名,设定过期时间
     *
     * @param loginName 用户名
     * @return 加密的token
     */
    public static String sign(String loginName, String salt) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 附带username信息
        return JWT.create()
                .withClaim("loginName", loginName)
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(salt));
    }
}
