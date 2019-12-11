package com.zhuzhou.framework.interceptor;

import com.zhuzhou.framework.result.Result;
import com.zhuzhou.framework.utils.stomp.ConstUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

/**
 * 超级管理员拦截器
 * @author chzeting
 */
public class SuperInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute(ConstUtils.SESSION_USER);
        if (object == null) {
            request.getRequestDispatcher("/error/" + Result.NOT_OAUTH).forward(request, response);
            return false;
        }
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField("roleIds");
        field.setAccessible(true);
        String roleIds = (String)field.get(object);
        if (StringUtils.isNotEmpty(roleIds)) {
            String[] split = roleIds.split(",");
            for (String roleId : split) {
                // 超级管理员
                if ("1".equals(roleId)) {
                    return true;
                }
            }
        }
        request.getRequestDispatcher("/error/" + Result.SUPER_PERMISSION_DENIED).forward(request, response);
        return false;
    }
}
